import {Injectable} from "@angular/core";
import {BaseService} from "../base.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ToastrNotificationService} from "../toastr/toastr-notification.service";
import {environment} from "../../../../environment";
import {Observable, catchError} from "rxjs";
import {getFromLocalStorage} from "../../app-util";
import {Quiz} from "../../models/quiz.interface";
import {Settings} from "../../models/settings.interface";
@Injectable({
  providedIn: "root"
})
export class QuizService extends BaseService {
  readonly URL: string = `${environment.apiUrl}/api/quiz`;

  constructor(private http: HttpClient, protected override notificationService: ToastrNotificationService) {
    super(notificationService);
  }

  public getAllUserQuizzes(): Observable<any> {
    return this.http
      .get<any>(`${this.URL}/${getFromLocalStorage("id")}`, {
        headers: new HttpHeaders({
          Authorization: `Bearer ${getFromLocalStorage("token")}`
        })
      })
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public getQuizzQuestions(id: number): Observable<Quiz[]> {
    return this.http
      .get<Quiz[]>(`${this.URL}/${id}/questions`, {
        headers: new HttpHeaders({
          Authorization: `Bearer ${getFromLocalStorage("token")}`
        })
      })
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public create(title: string, settings: Settings, userId: string, token: string): Observable<any> {
    return this.http
      .post<any>(
        `${this.URL}/create`,
        {title: title, settings: settings, userId: userId},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + token
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public update(quiz: Quiz, userId: string, token: string): Observable<any> {
    return this.http
      .put<any>(
        `${this.URL}/edit`,
        {id: quiz.id, title: quiz.title, settings: quiz.settings, userId: userId, questions: quiz.questions},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + token
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }
}

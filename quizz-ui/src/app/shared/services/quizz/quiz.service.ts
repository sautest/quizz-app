import {Injectable} from "@angular/core";
import {BaseService} from "../base.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ToastrNotificationService} from "../toastr/toastr-notification.service";
import {environment} from "../../../../environment";
import {Observable, catchError} from "rxjs";
import {getDate, getFromLocalStorage} from "../../app-util";
import {Quiz} from "../../models/quiz.interface";
import {Settings} from "../../models/settings.interface";
import {Theme} from "../../models/theme.interface";
import {Survey} from "../../models/survey.interface";
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

  public getAllQuizzes(): Observable<any> {
    return this.http.get<any>(`${this.URL}/`).pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public getQuizzQuestions(id: number, byUuid?: boolean): Observable<Quiz[]> {
    if (byUuid) {
      return this.http.get<Quiz[]>(`${this.URL}/${id}/questions/uuid`).pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
    } else {
      return this.http
        .get<Quiz[]>(`${this.URL}/${id}/questions`, {
          headers: new HttpHeaders({
            Authorization: `Bearer ${getFromLocalStorage("token")}`
          })
        })
        .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
    }
  }

  public create(title: string, settings: Settings, theme: Theme, userId: string, token: string): Observable<any> {
    return this.http
      .post<any>(
        `${this.URL}/create`,
        {title: title, dateCreated: getDate(), settings: settings, theme: theme, userId: userId},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + token
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public generateGraph(quiz: Quiz, isVerticalAlignment: boolean): Observable<any> {
    return this.http
      .post<any>(
        `${this.URL}/generate`,
        {quiz: quiz, survey: null, isVerticalAlignment: isVerticalAlignment},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + getFromLocalStorage("token")
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public update(quiz: Quiz, userId: string, token: string): Observable<any> {
    return this.http
      .put<any>(`${this.URL}/edit`, {
        id: quiz.id,
        title: quiz.title,
        responses: quiz.responses,
        status: quiz.status,
        settings: quiz.settings,
        theme: quiz.theme,
        userId: userId,
        questions: quiz.questions,
        logo: quiz.logo
      })
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public delete(id: number, token: string): Observable<any> {
    return this.http
      .delete<any>(`${this.URL}/delete/${id}`, {
        headers: new HttpHeaders({
          Authorization: "Bearer " + token
        })
      })
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }
}

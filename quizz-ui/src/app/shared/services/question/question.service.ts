import {Injectable} from "@angular/core";
import {BaseService} from "../base.service";
import {environment} from "../../../../environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ToastrNotificationService} from "../toastr/toastr-notification.service";
import {Observable, catchError} from "rxjs";
import {Question} from "../../models/question.interface";
import {ProjectType} from "../../../views/new-project-view/components/init-project-dialog/init-project-dialog.component";
import {getFromLocalStorage} from "../../app-util";

@Injectable({
  providedIn: "root"
})
export class QuestionService extends BaseService {
  readonly URL: string = `${environment.apiUrl}/api/question`;

  constructor(private http: HttpClient, protected override notificationService: ToastrNotificationService) {
    super(notificationService);
  }

  public get(id: number): Observable<any> {
    return this.http
      .get<any>(`${this.URL}/${id}`, {
        headers: new HttpHeaders({
          Authorization: "Bearer " + getFromLocalStorage("token")
        })
      })
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public create(question: Question, projectId: number, type: string, token: string): Observable<any> {
    return this.http
      .post<any>(
        `${this.URL}/create`,
        {question: question, projectId: +projectId, type: type === "quiz" ? ProjectType.QUIZ : ProjectType.SURVEY},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + token
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public update(question: Question, token: string): Observable<any> {
    return this.http
      .post<any>(
        `${this.URL}/edit`,
        {question: question},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + token
          })
        }
      )
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

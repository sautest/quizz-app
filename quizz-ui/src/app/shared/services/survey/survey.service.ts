import {Injectable} from "@angular/core";
import {environment} from "../../../../environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ToastrNotificationService} from "../toastr/toastr-notification.service";
import {Observable, catchError} from "rxjs";
import {BaseService} from "../base.service";
import {getFromLocalStorage} from "../../app-util";
import {Survey} from "../../models/survey.interface";
import {Settings} from "../../models/settings.interface";

@Injectable({
  providedIn: "root"
})
export class SurveyService extends BaseService {
  readonly URL: string = `${environment.apiUrl}/api/survey`;

  constructor(private http: HttpClient, protected override notificationService: ToastrNotificationService) {
    super(notificationService);
  }

  public getAllUserSurveys(): Observable<any> {
    return this.http
      .get<any>(`${this.URL}/${getFromLocalStorage("id")}`, {
        headers: new HttpHeaders({
          Authorization: `Bearer ${getFromLocalStorage("token")}`
        })
      })
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public getSurveyQuestions(id: number): Observable<Survey[]> {
    return this.http
      .get<Survey[]>(`${this.URL}/${id}/questions`, {
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

  public update(survey: Survey, userId: string, token: string): Observable<any> {
    return this.http
      .put<any>(
        `${this.URL}/edit`,
        {id: survey.id, title: survey.title, settings: survey.settings, userId: userId, questions: survey.questions},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + token
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }
}

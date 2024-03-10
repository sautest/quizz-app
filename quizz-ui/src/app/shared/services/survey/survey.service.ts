import {Injectable} from "@angular/core";
import {environment} from "../../../../environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {ToastrNotificationService} from "../toastr/toastr-notification.service";
import {Observable, catchError} from "rxjs";
import {BaseService} from "../base.service";
import {getDate, getFromLocalStorage} from "../../app-util";
import {Survey} from "../../models/survey.interface";
import {Settings} from "../../models/settings.interface";
import {Theme} from "../../models/theme.interface";

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

  public getAllSurveys(): Observable<any> {
    return this.http.get<any>(`${this.URL}/`).pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public getSurveyQuestions(id: number, byUuid?: boolean): Observable<Survey[]> {
    if (byUuid) {
      return this.http.get<Survey[]>(`${this.URL}/${id}/questions/uuid`).pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
    } else {
      return this.http
        .get<Survey[]>(`${this.URL}/${id}/questions`, {
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

  public generateGraph(survey: Survey, isVerticalAlignment: boolean): Observable<any> {
    return this.http
      .post<any>(
        `${this.URL}/generate`,
        {survey: survey, quiz: null, isVerticalAlignment: isVerticalAlignment},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + getFromLocalStorage("token")
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public update(survey: Survey, userId: string, token: string): Observable<any> {
    return this.http
      .put<any>(
        `${this.URL}/edit`,
        {
          id: survey.id,
          title: survey.title,
          responses: survey.responses,
          status: survey.status,
          settings: survey.settings,
          theme: survey.theme,
          userId: userId,
          questions: survey.questions
        },
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

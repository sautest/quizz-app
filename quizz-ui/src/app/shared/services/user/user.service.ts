import {Injectable} from "@angular/core";
import {environment} from "../../../../environment";
import {Observable, catchError} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BaseService} from "../base.service";
import {ToastrNotificationService} from "../toastr/toastr-notification.service";
import {getDate, getFromLocalStorage} from "../../app-util";
import {User} from "../../models/user.interface";

@Injectable({
  providedIn: "root"
})
export class UserService extends BaseService {
  readonly URL: string = `${environment.apiUrl}/api/user`;

  constructor(private http: HttpClient, protected override notificationService: ToastrNotificationService) {
    super(notificationService);
  }

  public create(email: string, username: string, password: string): Observable<any> {
    return this.http
      .post<any>(`${this.URL}/create`, {email: email, username: username, password: password, dateJoined: getDate()})
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public login(username: string, password: string): Observable<any> {
    return this.http
      .post<any>(`${this.URL}/login`, {username: username, password: password})
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err, err.error)));
  }

  public edit(id: string, email: string, username: string, blocked: boolean): Observable<any> {
    return this.http
      .put<any>(
        `${this.URL}/edit`,
        {id: id, email: email, username: username, blocked: blocked},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + getFromLocalStorage("token")
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public getUser(): Observable<User> {
    const userId = window.location.pathname.split("/").pop();
    return this.http
      .get<any>(`${this.URL}/getUsers/${userId}`, {
        headers: new HttpHeaders({
          Authorization: "Bearer " + getFromLocalStorage("token")
        })
      })
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public getUsers(): Observable<any[]> {
    return this.http.get<any>(`${this.URL}/getUsers`, {
      headers: new HttpHeaders({
        Authorization: "Bearer " + getFromLocalStorage("token")
      })
    });
  }
}

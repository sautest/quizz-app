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
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err, "Incorrect credentials!")));
  }

  public edit(email: string, username: string): Observable<any> {
    return this.http
      .put<any>(
        `${this.URL}/edit`,
        {id: getFromLocalStorage("id"), email: email, username: username},
        {
          headers: new HttpHeaders({
            Authorization: "Bearer " + getFromLocalStorage("token")
          })
        }
      )
      .pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public getUser(): Observable<User> {
    return this.http.get<any>(`${this.URL}/getUsers/${getFromLocalStorage("id")}`, {
      headers: new HttpHeaders({
        Authorization: "Bearer " + getFromLocalStorage("token")
      })
    });
  }

  public getUsers(): Observable<string> {
    return this.http.get<any>(`${this.URL}/getUsers`);
  }
}

import {Injectable} from "@angular/core";
import {BaseService} from "../base.service";
import {environment} from "../../../../environment";
import {HttpClient} from "@angular/common/http";
import {ToastrNotificationService} from "../toastr/toastr-notification.service";
import {Observable, catchError} from "rxjs";
import {Answer} from "../../models/answer.interface";

@Injectable({
  providedIn: "root"
})
export class AnswerService extends BaseService {
  readonly URL: string = `${environment.apiUrl}/api/answer`;

  constructor(private http: HttpClient, protected override notificationService: ToastrNotificationService) {
    super(notificationService);
  }

  public create(answers: Answer[]): Observable<any> {
    return this.http.post<any>(`${this.URL}/create`, answers).pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }

  public get(id: number): Observable<any> {
    return this.http.get<any>(`${this.URL}/getAnswers/${id}`).pipe(catchError(err => this.httpCatchErrorWithResponse(err)));
  }
}

import {Injectable} from "@angular/core";
import {of, throwError} from "rxjs";
import {ToastrNotificationService} from "./toastr/toastr-notification.service";
@Injectable({
  providedIn: "root"
})
export class BaseService {
  constructor(protected notificationService: ToastrNotificationService) {}

  protected httpCatchError(error: any, msg?: string) {
    return this.httpCatchErrorWithResponse(error, msg);
  }

  protected httpCatchErrorWithResponse(error: any, msg?: string) {
    this.notificationService.error(error, msg);

    const errorSub = throwError(() => {
      return new Error(error);
    });

    errorSub.subscribe();
    return of(error.error);
  }
}

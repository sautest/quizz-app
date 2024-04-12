import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {ActiveToast, ToastrService} from "ngx-toastr";
import {LogLevel, ResponseType} from "../../app-util";

@Injectable({
  providedIn: "root"
})
export class ToastrNotificationService {
  constructor(protected toastr: ToastrService, protected router: Router) {}

  error(error: any, msg?: String) {
    this.notify(LogLevel.error, this.handleMessage(error, msg, ResponseType.JSON));
  }

  success(message: any) {
    this.notify(LogLevel.success, this.handleMessage(null, message, ResponseType.JSON));
  }

  private handleMessage(error: any, msg: any, responseType: ResponseType): string {
    if (typeof msg === "string") {
      return msg;
    }

    if (error?.status === 403) {
      return `403 Forbidden`;
    }

    if (error?.status === 404) {
      return `404 Not Found`;
    }

    if (error?.status === 500) {
      return `500 Internal Server Error`;
    }

    return "";
  }

  protected notify(level: LogLevel, message: string): ActiveToast<any> {
    if (this.toastr.currentlyActive > 0) {
      return <any>null;
    }
    return (<any>this.toastr)[level](message, "");
  }
}

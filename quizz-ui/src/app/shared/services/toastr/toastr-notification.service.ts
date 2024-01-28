import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {ActiveToast, ToastrService} from "ngx-toastr";
import {LogLevel, ResponseType} from "../../app-util";

@Injectable({
  providedIn: "root"
})
export class ToastrNotificationService {
  constructor(protected toastr: ToastrService, protected router: Router) {}

  error(message: any, msg?: String) {
    this.notify(LogLevel.error, this.handleMessage(msg, ResponseType.JSON));
  }

  success(message: any) {
    this.notify(LogLevel.success, this.handleMessage(message, ResponseType.JSON));
  }

  private handleMessage(msg: any, responseType: ResponseType): string {
    if (typeof msg === "string") {
      return msg;
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

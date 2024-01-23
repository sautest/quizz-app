import {Component} from "@angular/core";
import {Router} from "@angular/router";

@Component({
  selector: "app-guest-view",
  templateUrl: "./guest-view.component.html",
  styleUrl: "./guest-view.component.scss"
})
export class GuestViewComponent {
  constructor(private router: Router) {}

  showSignUpDialog: boolean = false;
  showSignInDialog: boolean = false;

  onQuizzesBtnClick() {
    this.router.navigate(["/home"]);
    this.showSignUpDialog = false;
    this.showSignInDialog = false;
  }

  onSignInBtnClick() {
    this.router.navigate(["/sign-in"]);
    this.showSignUpDialog = false;
    this.showSignInDialog = true;
  }

  onSignUpBtnClick() {
    this.router.navigate(["/sign-up"]);
    this.showSignInDialog = false;
    this.showSignUpDialog = true;
  }
}

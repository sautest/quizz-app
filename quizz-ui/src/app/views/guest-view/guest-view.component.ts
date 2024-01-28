import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: "app-guest-view",
  templateUrl: "./guest-view.component.html",
  styleUrl: "./guest-view.component.scss"
})
export class GuestViewComponent implements OnInit {
  constructor(private router: Router, private route: ActivatedRoute) {}
  @Input() showSignUpDialog!: boolean;
  @Input() showSignInDialog!: boolean;

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.showSignUpDialog = data["showSignUpDialog"];
      this.showSignInDialog = data["showSignInDialog"];
    });
  }

  onQuizzesBtnClick() {
    this.router.navigate(["/home"]);
  }

  onSignInBtnClick() {
    this.router.navigate(["/sign-in"]);
  }

  onSignUpBtnClick() {
    this.router.navigate(["/sign-up"]);
  }
}

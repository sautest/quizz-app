import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {ProjectStatus, Quiz} from "../../shared/models/quiz.interface";
import {ProjectType} from "../new-project-view/components/init-project-dialog/init-project-dialog.component";
import {Survey} from "../../shared/models/survey.interface";
import {SurveyService} from "../../shared/services/survey/survey.service";
import {MenuItem} from "primeng/api";

@Component({
  selector: "app-guest-view",
  templateUrl: "./guest-view.component.html",
  styleUrl: "./guest-view.component.scss"
})
export class GuestViewComponent implements OnInit {
  @Input() showPublicProjects!: boolean;
  @Input() showSignUpDialog!: boolean;
  @Input() showSignInDialog!: boolean;

  projectStatus = ProjectStatus;

  quizzes: Quiz[] = [];
  surveys: Survey[] = [];

  showSurveys: boolean = false;
  items: MenuItem[];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService
  ) {
    this.items = [
      {label: "Public projects", icon: "fa-solid fa-list-check", command: _event => this.onQuizzesBtnClick()},
      {label: "Sign In", icon: "pi pi-sign-in", command: _event => this.onSignInBtnClick()},
      {label: "Sign Up", icon: "pi pi-user-plus", command: _event => this.onSignUpBtnClick()}
    ];
  }

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      console.log(data);
      this.showPublicProjects = data["showPublicProjects"];
      this.showSignUpDialog = data["showSignUpDialog"];
      this.showSignInDialog = data["showSignInDialog"];

      if (this.showPublicProjects) {
        this.quizService.getAllQuizzes().subscribe((res: Quiz[]) => {
          this.quizzes = res;
        });

        this.surveyService.getAllSurveys().subscribe((res: Survey[]) => {
          this.surveys = res;
        });
      }
    });
  }

  get publicQuizzes(): Quiz[] {
    return this.quizzes.filter(q => q.settings.enablePublic && q.status !== this.projectStatus.IN_DESIGN);
  }

  get publicSurveys(): Survey[] {
    return this.surveys.filter(q => q.settings.enablePublic && q.status !== this.projectStatus.IN_DESIGN);
  }

  onItemClick(label: string, event: any) {
    console.log("Clicked item:", label);
    // Perform actions based on the clicked item
    // For example, navigate to a different page based on the clicked item
    // this.router.navigate(['/other-page']);
  }

  onEnterProject(type: ProjectType, uuid: string) {
    if (!this.showSurveys) {
      this.router.navigate([`quiz/${uuid}`]);
    } else {
      this.router.navigate([`survey/${uuid}`]);
    }
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

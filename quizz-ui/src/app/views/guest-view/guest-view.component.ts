import {Component, Input, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {ProjectStatus, Quiz} from "../../shared/models/quiz.interface";
import {ProjectType} from "../new-project-view/components/init-project-dialog/init-project-dialog.component";
import {Survey} from "../../shared/models/survey.interface";
import {SurveyService} from "../../shared/services/survey/survey.service";

@Component({
  selector: "app-guest-view",
  templateUrl: "./guest-view.component.html",
  styleUrl: "./guest-view.component.scss"
})
export class GuestViewComponent implements OnInit {
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService
  ) {}
  @Input() showPublicProjects!: boolean;
  @Input() showSignUpDialog!: boolean;
  @Input() showSignInDialog!: boolean;

  projectStatus = ProjectStatus;

  quizzes: Quiz[] = [];
  surveys: Survey[] = [];

  showSurveys: boolean = false;

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.showPublicProjects = data["showPublicProjects"];
      this.showSignUpDialog = data["showSignUpDialog"];
      this.showSignInDialog = data["showSignInDialog"];

      if (this.showPublicProjects) {
        this.quizService.getAllQuizzes().subscribe((res: Quiz[]) => {
          this.quizzes = res;
        });

        this.surveyService.getAllSurveys().subscribe((res: Survey[]) => {
          this.surveys = res;
          console.log(this.surveys);
        });
      }
    });
  }

  get publicQuizzes(): Quiz[] {
    return this.quizzes.filter(q => q.settings.enablePublic && q.status !== this.projectStatus.IN_DESIGN);
  }

  get publicSurveys(): Survey[] {
    console.log(this.surveys.filter(q => q.settings.enablePublic && q.status !== this.projectStatus.IN_DESIGN));
    return this.surveys.filter(q => q.settings.enablePublic && q.status !== this.projectStatus.IN_DESIGN);
  }

  onEnterProject(type: ProjectType, uuid: string) {
    this.router.navigate([`quiz/${uuid}`]);
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

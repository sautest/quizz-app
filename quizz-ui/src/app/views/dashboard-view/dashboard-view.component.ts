import {Component, OnInit} from "@angular/core";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {Quiz} from "../../shared/models/quiz.interface";
import {MenuItem} from "primeng/api/menuitem";
import {SurveyService} from "../../shared/services/survey/survey.service";
import {Survey} from "../../shared/models/survey.interface";
import {ActivatedRoute, Router} from "@angular/router";
import {getFromLocalStorage} from "../../shared/app-util";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";
import {ConfirmationService, MessageService} from "primeng/api";

@Component({
  selector: "app-dashboard-view",
  templateUrl: "./dashboard-view.component.html",
  styleUrl: "./dashboard-view.component.scss",
  providers: [ConfirmationService, MessageService]
})
export class DashboardViewComponent implements OnInit {
  showSurveys: boolean = false;

  quizzes: Quiz[] = [];
  surveys: Survey[] = [];

  selectedItemId!: number;

  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService,
    private router: Router,
    private notificationService: ToastrNotificationService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.quizService.getAllUserQuizzes().subscribe(res => {
      this.quizzes = res;
    });

    this.surveyService.getAllUserSurveys().subscribe(res => {
      this.surveys = res;
    });
  }

  onSwitchChange(): void {
    this.showSurveys = !this.showSurveys;
  }

  updateProject(id: number): void {
    this.selectedItemId = id;

    if (this.showSurveys) {
      this.router.navigate([`/create/survey/${this.selectedItemId}`]);
    } else {
      this.router.navigate([`/create/quiz/${this.selectedItemId}`]);
    }
  }

  onDeleteConfirmation(event: any, id: number) {
    this.selectedItemId = id;

    this.confirmationService.confirm({
      target: event.target,
      message: "Do you want to delete this Project?",
      header: "Delete Confirmation",
      icon: "pi pi-info-circle",
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.deleteProject();
      },
      reject: () => {
        this.confirmationService.close();
      }
    });
  }

  deleteProject(): void {
    if (this.showSurveys) {
      this.surveyService.delete(this.selectedItemId, getFromLocalStorage("token")).subscribe(res => {
        this.surveyService.getAllUserSurveys().subscribe(res => {
          this.surveys = res;
          this.notificationService.success("Survey Deleted");
        });
      });
    } else {
      this.quizService.delete(this.selectedItemId, getFromLocalStorage("token")).subscribe(res => {
        this.quizService.getAllUserQuizzes().subscribe(res => {
          this.quizzes = res;
          this.notificationService.success("Quizz Deleted");
        });
      });
    }
  }

  redirectToPreview(id: string) {
    window.open(window.location.origin + `/preview/${!this.showSurveys ? "quiz" : "survey"}/${id}`, "_blank");
  }

  redirectToStatistics(id: string) {
    this.router.navigate([`/statistics/${!this.showSurveys ? "quiz" : "survey"}/${id}`]);
  }
}

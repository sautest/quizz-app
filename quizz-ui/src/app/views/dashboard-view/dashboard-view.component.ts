import {Component, OnInit} from "@angular/core";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {Quiz} from "../../shared/models/quiz.interface";
import {MenuItem} from "primeng/api/menuitem";
import {SurveyService} from "../../shared/services/survey/survey.service";
import {Survey} from "../../shared/models/survey.interface";
import {Router} from "@angular/router";

@Component({
  selector: "app-dashboard-view",
  templateUrl: "./dashboard-view.component.html",
  styleUrl: "./dashboard-view.component.scss"
})
export class DashboardViewComponent implements OnInit {
  showSurveys: boolean = false;

  quizzes: Quiz[] = [];
  surveys: Survey[] = [];
  configureItems: MenuItem[];

  selectedItem: any;

  constructor(private quizService: QuizService, private surveyService: SurveyService, private router: Router) {
    this.configureItems = [
      {
        label: "Edit",
        icon: "pi pi-refresh",
        command: () => {
          this.updateQuiz();
        }
      },
      {
        label: "Delete",
        icon: "pi pi-times",
        command: () => {
          this.deleteQuiz();
        }
      }
    ];
  }

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

  onConfigureBtnClick(item: any): void {
    this.selectedItem = item;
  }

  updateQuiz(): void {
    if (this.showSurveys) {
      this.router.navigate([`/create/survey/${this.selectedItem.id}`]);
    } else {
      this.router.navigate([`/create/quiz/${this.selectedItem.id}`]);
    }
  }
  deleteQuiz(): void {}
}

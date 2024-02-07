import {Component, OnInit} from "@angular/core";
import {Router} from "@angular/router";
import {QuizService} from "../../../../shared/services/quizz/quiz.service";
import {SurveyService} from "../../../../shared/services/survey/survey.service";

import {ToastrNotificationService} from "../../../../shared/services/toastr/toastr-notification.service";
import {getFromLocalStorage} from "../../../../shared/app-util";
import {Settings} from "../../../../shared/models/settings.interface";

export enum ProjectType {
  QUIZ = "QUIZ",
  SURVEY = "SURVEY"
}

export interface IProjectType {
  name: string;
  code: ProjectType;
}

@Component({
  selector: "app-init-project-dialog",
  templateUrl: "./init-project-dialog.component.html",
  styleUrl: "./init-project-dialog.component.scss"
})
export class InitProjectDialogComponent implements OnInit {
  showInitProjectDialog = true;
  projectTypes!: IProjectType[];
  selectedProjectType!: IProjectType;
  projectTitle!: string;

  defaultSettings: Settings = {
    enableTimeLimit: false,
    questionsPerPage: 1,
    enableAnswerNotInOrder: false,
    enableAskForBasicUserInfo: false,
    enableProgressBar: true,
    enablePublic: false,
    enableShowAnswersAtTheEnd: false,
    enableRandomizeQuestions: false
  };

  constructor(
    private router: Router,
    private quizService: QuizService,
    private surveyService: SurveyService,
    private notificationService: ToastrNotificationService
  ) {}

  ngOnInit() {
    this.projectTypes = [
      {name: "Quiz", code: ProjectType.QUIZ},
      {name: "Survey", code: ProjectType.SURVEY}
    ];
  }

  onCancelClick() {
    this.router.navigate([`/dashboard/${getFromLocalStorage("id")}`]);
  }

  onCreateProjectClick() {
    if (this.selectedProjectType.code === ProjectType.QUIZ) {
      this.quizService
        .create(this.projectTitle, this.defaultSettings, getFromLocalStorage("id"), getFromLocalStorage("token"))
        .subscribe(res => {
          this.notificationService.success("Quiz Created!");
          this.router.navigate([`/create/quiz/${res.id}`]);
        });
    } else {
      this.surveyService
        .create(this.projectTitle, this.defaultSettings, getFromLocalStorage("id"), getFromLocalStorage("token"))
        .subscribe(res => {
          this.notificationService.success("Survey Created!");
          this.router.navigate([`/create/survey/${res.id}`]);
        });
    }
  }
}
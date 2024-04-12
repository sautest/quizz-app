import {Component, ElementRef, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {QuizService} from "../../shared/services/quizz/quiz.service";
import {QuestionService} from "../../shared/services/question/question.service";
import {ToastrNotificationService} from "../../shared/services/toastr/toastr-notification.service";
import {Question} from "../../shared/models/question.interface";
import {CdkDragDrop, moveItemInArray} from "@angular/cdk/drag-drop";
import {QuestionType} from "./components/question-dialog/question-dialog.component";
import {ProjectStatus, Quiz} from "../../shared/models/quiz.interface";
import {getFromLocalStorage} from "../../shared/app-util";
import {ConfirmationService, MessageService} from "primeng/api";
import {SurveyService} from "../../shared/services/survey/survey.service";
import {Survey} from "../../shared/models/survey.interface";
import {GraphComponent} from "./components/graph/graph.component";

@Component({
  selector: "app-edit-project-view",
  templateUrl: "./edit-project-view.component.html",
  styleUrl: "./edit-project-view.component.scss",
  providers: [ConfirmationService, MessageService]
})
export class EditProjectViewComponent implements OnInit {
  @ViewChild(GraphComponent) graphComponent!: GraphComponent;

  stateOptions: any[] = [
    {label: "List", value: "list"},
    {label: "Graph", value: "graph"}
  ];

  value: string = "list";

  showQuestionDialog: boolean = false;
  showQuestionBankDialog: boolean = false;
  showLogicDialog: boolean = false;
  showGraph: boolean = false;
  project!: Quiz | Survey;
  editableQuestion!: Question | null;

  readonly QUESTION_TYPE = QuestionType;

  get projectTitle() {
    return this.route.snapshot.params["type"] === "quiz" ? `Quiz: ${this.project.title}` : `Survey: ${this.project.title}`;
  }

  get projectStatusLabel() {
    return this.route.snapshot.params["type"] === "quiz" ? `Quiz Status:` : `Survey Status:`;
  }

  get shareLink() {
    return this.route.snapshot.params["type"] === "quiz"
      ? `http://localhost:4200/quiz/${this.project.uuid}`
      : `http://localhost:4200/survey/${this.project.uuid}`;
  }

  get projectStatusChangeBtnLabel() {
    if (this.project.status === ProjectStatus.IN_DESIGN) {
      return "Open";
    } else if (this.project.status === ProjectStatus.OPEN) {
      return "Close";
    } else {
      return "Reopen";
    }
  }

  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private surveyService: SurveyService,
    private questionService: QuestionService,
    private notificationService: ToastrNotificationService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.fetchQuestions();
  }

  fetchQuestions(): void {
    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.getQuizzQuestions(this.route.snapshot.params["id"]).subscribe(res => {
        if (Array.isArray(res)) {
          this.project = res[0];
          this.graphComponent.renderGraph(this.project, true);
        }
      });
    } else {
      this.surveyService.getSurveyQuestions(this.route.snapshot.params["id"]).subscribe(res => {
        if (Array.isArray(res)) {
          this.project = res[0];
          this.graphComponent.renderGraph(this.project, true);
        }
      });
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.project.questions, event.previousIndex, event.currentIndex);

    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {
        this.graphComponent.renderGraph(this.project, true);
      });
    } else {
      this.surveyService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {});
      this.graphComponent.renderGraph(this.project, false);
    }
  }

  onQuestionMoveInGraph(pair: {source: number; target: number}) {
    [this.project.questions[pair.source], this.project.questions[pair.target]] = [
      this.project.questions[pair.target],
      this.project.questions[pair.source]
    ];

    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {
        this.graphComponent.renderGraph(this.project, true);
      });
    } else {
      this.surveyService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {
        this.graphComponent.renderGraph(this.project, false);
      });
    }
  }

  onAddQuestionClick(): void {
    this.editableQuestion = null;
    this.showQuestionDialog = true;
  }

  onQuestionBankClick(): void {
    this.showQuestionBankDialog = true;
  }

  onShowEditDialog(question: Question): void {
    this.editableQuestion = question;
    this.showQuestionDialog = true;
  }

  onCloseQuestionClick(): void {
    this.fetchQuestions();
    this.showQuestionDialog = false;
  }

  onCloseQuestionBankClick(): void {
    this.showQuestionBankDialog = false;
  }

  onShowLogicDialog(question: Question): void {
    this.editableQuestion = question;
    this.showLogicDialog = true;
  }

  onCloseLogicDialog(): void {
    this.showLogicDialog = false;
  }

  onSaveLogicDialog(): void {
    this.fetchQuestions();
  }

  onDeleteConfirmation(event: Event, question: Question) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: "Do you want to delete this Question?",
      header: "Delete Confirmation",
      icon: "pi pi-info-circle",
      acceptButtonStyleClass: "p-button-danger p-button-text",
      rejectButtonStyleClass: "p-button-text p-button-text",
      acceptIcon: "none",
      rejectIcon: "none",

      accept: () => {
        this.onQuestionDelete(question);
      },
      reject: () => {
        this.confirmationService.close();
      }
    });
  }

  onQuestionDelete(question: Question) {
    this.project.questions.forEach(q => {
      q.logic = q.logic?.filter(logic => {
        return logic.actionOptionId !== question.id;
      });

      this.questionService.update(q, getFromLocalStorage("token")).subscribe(res => {});
    });

    if (question.id) {
      this.questionService.delete(question.id, getFromLocalStorage("token")).subscribe(_res => {
        this.notificationService.success("Question Deleted!");
        this.fetchQuestions();
      });
    }
  }

  onSelectButtonChange(event: any) {
    if (event.value === "list") {
      this.showGraph = false;
    }
    if (event.value === "graph") {
      this.showGraph = true;
      this.graphComponent.renderGraph(this.project, this.route.snapshot.params["type"] === "quiz");
    }
  }

  redirectToPreview() {
    window.open(
      window.location.origin + `/preview/${this.route.snapshot.params["type"] === "quiz" ? "quiz" : "survey"}/${this.project.id}`,
      "_blank"
    );
  }

  onChangeProjectStatus() {
    if (this.project.status === ProjectStatus.IN_DESIGN) {
      this.project.status = ProjectStatus.OPEN;
      this.notificationService.success("Quiz opened!");
    } else if (this.project.status === ProjectStatus.OPEN) {
      this.project.status = ProjectStatus.CLOSED;
      this.notificationService.success("Quiz closed!");
    } else {
      this.project.status = ProjectStatus.OPEN;
      this.notificationService.success("Quiz reopened!");
    }

    if (this.route.snapshot.params["type"] === "quiz") {
      this.quizService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {});
    } else {
      this.surveyService.update(this.project, getFromLocalStorage("id"), getFromLocalStorage("token")).subscribe(res => {});
    }
  }

  onCopyShareLink() {
    navigator.clipboard.writeText(this.shareLink).then(() => {
      this.notificationService.success("Link copied to clipboard!");
    });
  }
}

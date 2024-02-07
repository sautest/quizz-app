import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {Question, QuestionOption} from "../../../../shared/models/question.interface";
import {ActivatedRoute} from "@angular/router";
import {QuestionService} from "../../../../shared/services/question/question.service";
import {getFromLocalStorage} from "../../../../shared/app-util";
import {ToastrNotificationService} from "../../../../shared/services/toastr/toastr-notification.service";
import {QuizService} from "../../../../shared/services/quizz/quiz.service";

export enum QuestionType {
  SINGLE_CHOICE = "SINGLE_CHOICE",
  MULTI_CHOICE = "MULTI_CHOICE"
}

export interface IQuestionType {
  name: string;
  code: QuestionType;
}

@Component({
  selector: "app-question-dialog",
  templateUrl: "./question-dialog.component.html",
  styleUrl: "./question-dialog.component.scss"
})
export class QuestionDialogComponent implements OnInit {
  @Input() editableQuestion!: Question | null;
  @Output() onClose: EventEmitter<void> = new EventEmitter();

  questionTypes: IQuestionType[] = [
    {name: "Single Choice", code: QuestionType.SINGLE_CHOICE},
    {name: "Multiple Choice", code: QuestionType.MULTI_CHOICE}
  ];

  questionId!: number | undefined;

  selectedQuestionType!: IQuestionType;

  htmlContent: string = "";

  options: QuestionOption[] = [{text: "", correct: false}];

  constructor(
    private route: ActivatedRoute,
    private quizService: QuizService,
    private questionService: QuestionService,
    private notificationService: ToastrNotificationService
  ) {}

  ngOnInit(): void {
    if (this.editableQuestion) {
      this.questionId = this.editableQuestion.id;
      this.selectedQuestionType = {
        name: this.editableQuestion.type === QuestionType.SINGLE_CHOICE ? "Single Choice" : "Multiple Choice",
        code: this.editableQuestion.type
      };
      this.htmlContent = this.editableQuestion.text;
      this.options = this.editableQuestion.options;
    }
  }

  onAddQuestionOptionClick() {
    this.options.push({text: "", correct: false});
  }

  onDeleteQuestionOptionClick(index: number) {
    this.options.splice(index, 1);
  }

  onSaveBtnClick() {
    const question: Question = {
      id: this.questionId,
      type: this.selectedQuestionType.code,
      text: this.htmlContent,
      options: this.options,
      quizzes: []
    };

    if (this.editableQuestion) {
      this.questionService.update(question, getFromLocalStorage("token")).subscribe(res => {
        console.log(res);
        this.notificationService.success("Question Updated!");
        this.onClose.emit();
      });
    } else {
      this.questionService
        .create(question, this.route.snapshot.params["id"], this.route.snapshot.params["type"], getFromLocalStorage("token"))
        .subscribe(_res => {
          this.notificationService.success("Question Created!");
          this.onClose.emit();
        });
    }
  }

  onCloseBtnClick() {
    const id = this.route.snapshot.params["id"];
    this.onClose.emit();
  }

  isSaveBtnDisabled(): boolean {
    return !this.selectedQuestionType || !this.htmlContent.trim() || this.options.every(opt => opt.text.trim() === "");
  }
}

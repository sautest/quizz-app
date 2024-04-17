import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {
  ILogicActionType,
  ILogicConditionType,
  LogicActionType,
  LogicConditionType,
  Question,
  QuestionLogic
} from "../../../../shared/models/question.interface";
import {Survey} from "../../../../shared/models/survey.interface";
import {Quiz} from "../../../../shared/models/quiz.interface";
import {getFromLocalStorage} from "../../../../shared/app-util";
import {QuestionService} from "../../../../shared/services/question/question.service";
import {ToastrNotificationService} from "../../../../shared/services/toastr/toastr-notification.service";
export interface QuestionLogicDropdown {
  conditionType: {name: string; code: LogicConditionType};
  conditionOption: {name: string; code: number};
  actionType: {name: string; code: LogicActionType};
  actionOption: {name: string; code: number};
}

@Component({
  selector: "app-logic-dialog",
  templateUrl: "./logic-dialog.component.html",
  styleUrl: "./logic-dialog.component.scss"
})
export class LogicDialogComponent implements OnInit {
  @Input() editableQuestion!: Question | null;
  @Input() project!: Quiz | Survey;
  @Output() onClose: EventEmitter<void> = new EventEmitter();
  @Output() onSave: EventEmitter<void> = new EventEmitter();

  readonly LOGIC_CONDITION_TYPE = LogicConditionType;
  readonly LOGIC_ACTION_TYPE = LogicActionType;

  questionLogic!: QuestionLogicDropdown[];
  logicConditionTypes!: ILogicConditionType[];
  logicActionTypes!: ILogicActionType[];
  questionOptions!: any;
  questions!: any;

  allQuestionOptions(i: number): any {
    return this.questionOptions.filter((opt: any) => !this.questionLogic.slice(0, i).find(cond => cond.conditionOption.code === opt.code));
  }

  constructor(private questionService: QuestionService, private notificationService: ToastrNotificationService) {}

  ngOnInit() {
    this.logicConditionTypes = [
      {name: "When answer is", code: LogicConditionType.WHEN_ANSWER_IS},
      {name: "Always", code: LogicConditionType.ALWAYS}
    ];
    this.logicActionTypes = [
      {name: "Skip to question", code: LogicActionType.SKIP_TO_QUESTION},
      {name: "End questioning", code: LogicActionType.END_QUESTIONING}
    ];
    this.questionOptions = this.editableQuestion?.options.map(option => ({name: option.text, code: option.id}));
    this.questions = this.project.questions.filter(q => q.id !== this.editableQuestion?.id).map(q => ({name: q.text, code: q.id}));

    this.questionLogic =
      this.editableQuestion?.logic?.map(item => ({
        conditionType: {name: this.conditionRuleText(item.conditionType), code: item.conditionType},
        conditionOption: {
          name: this.editableQuestion?.options.find(opt => opt.id === item.conditionOptionId)?.text ?? "",
          code: item.conditionOptionId
        },
        actionType: {name: this.actionRuleText(item.actionType), code: item.actionType},
        actionOption: {name: this.project.questions.find(proj => proj.id === item.actionOptionId)?.text ?? "", code: item.actionOptionId}
      })) ?? [];
  }

  onCloseBtnClick() {
    this.onClose.emit();
  }

  onAddLogicBtnClick() {
    this.questionLogic.push({
      conditionType: {name: "When answer is", code: LogicConditionType.WHEN_ANSWER_IS},
      conditionOption: {name: this.questionOptions[0].name, code: this.questionOptions[0].code},
      actionType: {name: "Skip to question", code: LogicActionType.SKIP_TO_QUESTION},
      actionOption: {name: this.questions[0].name, code: this.questions[0].code}
    });
  }

  onLogicItemDelete(index: number) {
    this.questionLogic.splice(index, 1);
  }

  onSaveBtnClick() {
    if (this.editableQuestion) {
      const logic: QuestionLogic[] = this.questionLogic.map(q => ({
        conditionType: q.conditionType.code,
        conditionOptionId: q.conditionOption.code,
        actionType: q.actionType.code,
        actionOptionId: q.actionOption.code
      }));

      const question: Question = {
        id: this.editableQuestion?.id,
        type: this.editableQuestion?.type,
        text: this.editableQuestion.text,
        inBank: this.editableQuestion.inBank,
        ownerId: this.editableQuestion.ownerId,
        options: this.editableQuestion.options,
        score: this.editableQuestion.score,
        logic: logic,
        quizzes: []
      };

      this.questionService.update(question, getFromLocalStorage("token")).subscribe(res => {
        this.notificationService.success("Logic Updated!");
        this.onClose.emit();
        this.onSave.emit();
      });
    }
  }

  private conditionRuleText(condition: LogicConditionType): string {
    if (condition === LogicConditionType.WHEN_ANSWER_IS) {
      return "When answer is";
    }
    if (condition === LogicConditionType.ALWAYS) {
      return "Always";
    }

    return "NO-TEXT";
  }

  private actionRuleText(condition: LogicActionType): string {
    if (condition === LogicActionType.SKIP_TO_QUESTION) {
      return "Skip to question";
    }
    if (condition === LogicActionType.END_QUESTIONING) {
      return "End questioning";
    }

    return "NO-TEXT";
  }
}

import {QuestionType} from "../../views/edit-project-view/components/question-dialog/question-dialog.component";
import {Quiz} from "./quiz.interface";

export interface Question {
  id?: number;
  type: QuestionType;
  text: string;
  inBank: boolean;
  ownerId: number;
  score?: number;
  options: QuestionOption[];
  logic?: QuestionLogic[];
  targetLogic?: QuestionLogic[];
  quizzes: Quiz[];
  selected?: string;
}

export interface QuestionOption {
  id?: number;
  text: string;
  correct: boolean;
  checked?: boolean;
}

export interface QuestionLogic {
  id?: number;
  conditionType: LogicConditionType;
  conditionOptionId: number;
  actionType: LogicActionType;
  actionOptionId: number;
}

export enum LogicConditionType {
  WHEN_ANSWER_IS = "WHEN_ANSWER_IS",
  ALWAYS = "ALWAYS"
}

export interface ILogicConditionType {
  name: string;
  code: LogicConditionType;
}

export enum LogicActionType {
  SKIP_TO_QUESTION = "SKIP_TO_QUESTION",
  END_QUESTIONING = "END_QUESTIONING"
}

export interface ILogicActionType {
  name: string;
  code: LogicActionType;
}

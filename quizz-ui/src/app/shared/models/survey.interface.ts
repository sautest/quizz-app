import {Question} from "./question.interface";
import {Settings} from "./settings.interface";

export interface Survey {
  id: number;
  title: string;
  questions: Question[];
  settings: Settings;
}

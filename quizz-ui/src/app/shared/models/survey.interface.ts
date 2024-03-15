import {Question} from "./question.interface";
import {ProjectStatus} from "./quiz.interface";
import {Settings} from "./settings.interface";
import {Theme} from "./theme.interface";

export interface Survey {
  id: number;
  uuid: string;
  title: string;
  responses: number;
  status: ProjectStatus;
  questions: Question[];
  settings: Settings;
  theme: Theme;
  logo: string;
}

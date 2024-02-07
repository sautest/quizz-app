export const EMPTY_TAG: string = "<empty>";
export const DOT: string = ".";
export const SPACE: string = " ";
export const TRAILING_DOT_REGEX: string = "\\.$";
export const EMPTY: string = "";
export const COMMA_WITH_SPACE: string = ", ";
export const THREE_DOTS: string = " ...";
export const ID_SEPARATOR: string = "|";
export const JSON_FILE = ".json";

export enum ResponseType {
  JSON,
  TEXT
}

export enum LogLevel {
  debug = "debug",
  success = "success",
  info = "info",
  warning = "warning",
  error = "error"
}

export function getFromLocalStorage(key: string): string {
  return localStorage.getItem(key) ?? "";
}

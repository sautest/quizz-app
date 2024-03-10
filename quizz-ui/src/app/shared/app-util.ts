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

export function shuffleArray(array: any[]) {
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
  return array;
}

export function getDate(): string {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");

  return `${year}-${month}-${day}`;
}

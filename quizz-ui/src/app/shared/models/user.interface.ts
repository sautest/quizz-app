export interface User {
  id?: number;
  username: string;
  email: string;
  dateJoined: string;
}

export interface UserContext {
  id: number | null;
  token: string | null;
}

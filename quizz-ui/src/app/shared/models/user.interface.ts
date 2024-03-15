export interface User {
  id?: number;
  username: string;
  email: string;
  dateJoined: string;
  blocked: boolean;
}

export interface UserContext {
  id: number | null;
  token: string | null;
}

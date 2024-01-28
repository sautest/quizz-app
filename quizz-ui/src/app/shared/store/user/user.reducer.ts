import {createReducer, on} from "@ngrx/store";
import {UserContext} from "../../models/user.interface";
import {setUserContext} from "./user.actions";

export interface UserState {
  userContext: UserContext;
}

const initialState: UserState = {
  userContext: {id: null, token: null}
};

export const userReducer = createReducer(
  initialState,
  on(setUserContext, (state, {userContext}) => ({...state, userContext}))
);

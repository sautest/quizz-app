import {createFeatureSelector, createSelector} from "@ngrx/store";
import {UserState} from "./user.reducer";

export const selectUserState = createFeatureSelector<UserState>("user");

export const selectUserContext = createSelector(selectUserState, (state: UserState) => state.userContext);

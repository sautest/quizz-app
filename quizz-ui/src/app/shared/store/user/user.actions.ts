import {createAction, props} from "@ngrx/store";
import {UserContext} from "../../models/user.interface";

export const setUserContext = createAction("[User] set User Context", props<{userContext: UserContext}>());

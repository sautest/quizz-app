import {NgModule} from "@angular/core";
import {BrowserModule, provideClientHydration} from "@angular/platform-browser";

import {AppRoutingModule} from "./app-routing.module";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

import {AppComponent} from "./app.component";
import {ButtonModule} from "primeng/button";
import {SidebarModule} from "primeng/sidebar";
import {DialogModule} from "primeng/dialog";
import {AvatarModule} from "primeng/avatar";
import {AvatarGroupModule} from "primeng/avatargroup";
import {InputTextModule} from "primeng/inputtext";
import {FormsModule} from "@angular/forms";
import {GuestViewComponent} from "./views/guest-view/guest-view.component";
import {AuthComponent} from "./views/guest-view/components/auth/auth.component";

@NgModule({
  declarations: [AppComponent, GuestViewComponent, AuthComponent],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ButtonModule,
    SidebarModule,
    DialogModule,
    AvatarModule,
    AvatarGroupModule,
    InputTextModule,
    FormsModule
  ],
  providers: [provideClientHydration()],
  bootstrap: [AppComponent]
})
export class AppModule {}

import { NgModule } from '@angular/core';
import { IonicModule } from 'ionic-angular';

// Plugins
//import { GooglePlus } from '@ionic-native/google-plus';
//import { Facebook } from '@ionic-native/facebook';

// Modules
import { SharedModule } from '../shared/shared.module';

// Pages
import { HomePage } from './pages/home/home';
import { LoginPage } from './pages/login/login';
import { RegisterPage } from './pages/register/register';
import { ProfilePage } from './pages/profile/profile';
import { PasswordPage } from './pages/password/password';

// Providers
import { AuthenticationService } from './providers/authentication.service';
import { UserService } from './providers/user.service';

@NgModule({
  declarations: [
    HomePage,
    LoginPage,
    RegisterPage,
    ProfilePage,
    PasswordPage
  ],
  imports: [
    IonicModule,
    SharedModule
  ],
  exports: [
    HomePage,
    LoginPage,
    RegisterPage,
    ProfilePage,
    PasswordPage
  ],
  entryComponents: [
    HomePage,
    LoginPage,
    RegisterPage,
    ProfilePage,
    PasswordPage
  ],
  providers: [
    //GooglePlus,
    //Facebook,
    AuthenticationService,
    UserService,
  ]
})
export class AuthModule { }

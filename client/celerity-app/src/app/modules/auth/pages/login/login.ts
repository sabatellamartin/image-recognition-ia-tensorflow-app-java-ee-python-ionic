import { Component, ViewChild } from '@angular/core';
import { NavController, AlertController } from 'ionic-angular';

// Cryptojs
import * as crypto from 'crypto-js';

// Pages
import { RegisterPage } from '../register/register';
import { HomePage } from '../home/home';

// Providers
import { AuthenticationService } from '../../providers/authentication.service';

@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  @ViewChild("username") username;
  @ViewChild("password") password;

  constructor(
    public navCtrl: NavController,
    public alertCtrl: AlertController,
    private authService: AuthenticationService) {
  }

  signUp() {
    this.navCtrl.push(RegisterPage);
  }

  /* Local */
  signIn() {
    const username = this.username.value!="" ? this.username.value.toLowerCase().trim() : false;
    const password = this.password.value!="" ? crypto.SHA512(this.password.value).toString(crypto.enc.Hex) : false;
    if (username && password) {
      this.authService.login(username, password).then(logged => {
        if (logged) {
          this.popView();
        } else {
          this.showMessage(false);
        }
      });
    } else {
      this.showMessage(false);
    }
  }

  /* Google
  signInGP(){
    this.authService.loginGoogle().then(result => {
      if (result) {
        this.popView();
      } else {
        this.showMessage(false);
      }
    });
  }*/

  /* Facebook
  signInFB(){
    this.authService.loginFacebook().then(result => {
      if (result) {
        this.popView();
      } else {
        this.showMessage(false);
      }
    });
  } */

  private showMessage(logged: boolean) {
    if (!logged) {
      this.alertCtrl.create({
        title: 'Login Unccessful!',
        subTitle: 'Verify your information',
        buttons: ['OK']
      }).present();
    }
  }

  private popView() {
    //this.navCtrl.pop();
    this.navCtrl.setRoot(HomePage);
  }

}

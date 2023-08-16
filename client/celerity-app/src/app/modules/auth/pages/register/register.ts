import { Component, ViewChild } from '@angular/core';
import { NavController, AlertController } from 'ionic-angular';

// Utils
import * as crypto from 'crypto-js';

// Models
import { Client } from '../../models/client';

// Providers
import { UserService } from '../../providers/user.service';
import { AuthenticationService } from '../../providers/authentication.service';

// Pages
import { HomePage } from '../home/home';

// Actions
import { Events } from 'ionic-angular';

@Component({
  selector: 'page-register',
  templateUrl: 'register.html'
})
export class RegisterPage {

  //@ViewChild("username") username;
  @ViewChild("email") email;
  @ViewChild("password") password;
  @ViewChild("repassword") repassword;

  constructor(
    public navCtrl: NavController,
    public alertCtrl: AlertController,
    private userService: UserService,
    private authService: AuthenticationService,
    public events: Events) {
  }

  signUp() {
    const email = this.email.value!="" ? this.email.value.toLowerCase().trim().replace(/\s/g, "") : false;
    const password = this.password.value!="" ? crypto.SHA512(this.password.value).toString(crypto.enc.Hex) : false;
    const repassword = this.repassword.value!="" ? crypto.SHA512(this.repassword.value).toString(crypto.enc.Hex) : false;
    if (email && password && password==repassword) {
      let client: Client = new Client();
      client.email =  email;
      client.password = password;
      this.userService.register(client).then(result => {
        this.showMessage(result);
        this.authService.login(client.email, client.password).then(logged => {
          this.publishSession();
        });
      });
    } else {
      this.showMessage(false);
    }
  }

  private showMessage(result: boolean) {
    if (result) {
      this.alertCtrl.create({
        title: 'Register Successful!',
        subTitle: 'You are sign up',
        buttons: ['OK']
      }).present();
    } else {
      this.alertCtrl.create({
        title: 'Register Unccessful!',
        subTitle: 'Verify your information',
        buttons: ['OK']
      }).present();
    }
  }

  private publishSession() {
    this.authService.getSession().then(session => {
      if (session) {
        this.events.publish('user:created', true, Date.now());
        this.navCtrl.setRoot(HomePage);
      }
    });
  }

}

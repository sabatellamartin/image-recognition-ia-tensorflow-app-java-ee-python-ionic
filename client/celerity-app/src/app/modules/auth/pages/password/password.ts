import { Component, Input, ViewChild } from '@angular/core';
import { NavController, AlertController } from 'ionic-angular';

// Utils
import * as crypto from 'crypto-js';

// Models
import { Client } from '../../models/client';

// Providers
import { UserService } from '../../providers/user.service';
import { AuthenticationService } from '../../providers/authentication.service';

@Component({
  selector: 'page-password',
  templateUrl: 'password.html'
})
export class PasswordPage {

  @ViewChild("password") password;
  @ViewChild("newpassword") newpassword;
  @ViewChild("repassword") repassword;

  @Input() client: Client = new Client();

  constructor(
    public navCtrl: NavController,
    public alertCtrl: AlertController,
    private userService: UserService,
    private authService: AuthenticationService) {
      this.current();
  }

  private current() {
    this.authService.current().then(client => {
      this.client = client;
    });
  }

  private setPassword() {
    if (this.client) {
      const password = this.password.value!="" ? crypto.SHA512(this.password.value).toString(crypto.enc.Hex) : false;
      const newpassword = this.newpassword.value!="" ? crypto.SHA512(this.newpassword.value).toString(crypto.enc.Hex) : false;
      const repassword = this.repassword.value!="" ? crypto.SHA512(this.repassword.value).toString(crypto.enc.Hex) : false;
      if (newpassword==repassword && newpassword!=password) {
        this.client.password = newpassword;
        this.userService.setPassword(this.client).then(result => {
          this.showMessage(result);
        });
      }
    } else {
      this.showMessage(false);
    }
  }

  showConfirm() {
    this.alertCtrl.create({
      title: 'Confirm change?',
      message: 'Do you agree to change password?',
      buttons: [
        {
          text: 'Disagree',
          handler: () => {
            console.log('Disagree clicked');
          }
        },
        {
          text: 'Agree',
          handler: () => {
            this.setPassword();
          }
        }
      ]
    }).present();
  }

  private showMessage(result: boolean) {
    if (result) {
      this.alertCtrl.create({
        title: 'Update Successful!',
        subTitle: 'Your password was changed',
        buttons: ['OK']
      }).present();
      this.popView();
    } else {
      this.alertCtrl.create({
        title: 'Update Unccessful!',
        subTitle: 'No changes in your password',
        buttons: ['OK']
      }).present();
    }
  }

  private popView() {
    this.navCtrl.pop();
  }

}

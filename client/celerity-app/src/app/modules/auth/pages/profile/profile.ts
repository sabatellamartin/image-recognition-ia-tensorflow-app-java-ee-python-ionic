import { Component, Input } from '@angular/core';
import { NavController, AlertController } from 'ionic-angular';

// Models
import { Client } from '../../models/client';

// Providers
import { UserService } from '../../providers/user.service';
import { AuthenticationService } from '../../providers/authentication.service';
import { SharedService } from '../../../shared/providers/shared.service';

// Pages
import { PasswordPage } from '../password/password';

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {

  @Input() client: Client = new Client();

  localAuth: boolean = false;

  constructor(
    public navCtrl: NavController,
    public alertCtrl: AlertController,
    private userService: UserService,
    private authService: AuthenticationService,
    private sharedService: SharedService) {
      this.current();
      this.isLocalAuth();
  }

  private current() {
    this.authService.current().then(client => {
      this.client = client;
    });
  }

  private update() {
    if (this.client) {
      this.userService.update(this.client).then(client => {
        let result = false;
        if (client) {
          this.client = client;
          result = true;
        }
        this.showMessage(result);
      });
    } else {
      this.showMessage(false);
    }
  }

  showConfirm() {
    this.alertCtrl.create({
      title: 'Confirm change?',
      message: 'Do you agree to change your profile information?',
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
            this.update();
          }
        }
      ]
    }).present();
  }

  private showMessage(result: boolean) {
    if (result) {
      this.alertCtrl.create({
        title: 'Update Successful!',
        subTitle: 'Your profile was changed',
        buttons: ['OK']
      }).present();
    } else {
      this.alertCtrl.create({
        title: 'Update Unccessful!',
        subTitle: 'No changes in your profile',
        buttons: ['OK']
      }).present();
    }
  }

  setDefaultImage() {
    this.client.picture = this.sharedService.defaultUserImg;
  }

  private isLocalAuth() {
    this.authService.getSession().then(session => {
      if (session) {
        this.localAuth = session.token_type==="Bearer" ? true : false;
      }
    });
  }

  goToSetPassword() {
    this.navCtrl.push(PasswordPage);
  }

}

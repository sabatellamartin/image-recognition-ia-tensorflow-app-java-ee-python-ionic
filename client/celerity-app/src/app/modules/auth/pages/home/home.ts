import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';

// Plugins
import { LoadingController } from 'ionic-angular';

// Providers
import { AuthenticationService } from '../../providers/authentication.service';
import { GlobalizationService } from '../../../setting/providers/globalization.service';
import { SettingService } from '../../../setting/providers/setting.service';
import { TranslateService } from '@ngx-translate/core';

// Pages
import { LoginPage } from '../login/login';
import { HistoryPage } from '../../../recognition/pages/history/history';
import { RecognitionPage } from '../../../recognition/pages/recognition/recognition';
import { SettingPage } from '../../../setting/pages/setting/setting';

// Actions
import { Events } from 'ionic-angular';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  session: any = false;

  pages: Array<{title: string, component: any}>;

  constructor(
    public navCtrl: NavController,
    public loadingCtrl: LoadingController,
    public events: Events,
  	private authService: AuthenticationService,
    private globalizationService: GlobalizationService,
    private translate: TranslateService,
    private settingService: SettingService) {

    events.subscribe('session:stored', (status, time) => {
      if (status) {
        this.getSession();
        this.settingService.getCurrent().then(setting => {
          let lang = setting.language as string;
          if (lang) {
            this.translate.use(lang);
            this.loadMenu();
          } else {
            this.globalizationService.getPreferredLanguage().then(language => {
              // Set the default language for translation strings, and the current language.
              let lang = language.split("-")[0];
              this.translate.setDefaultLang(lang);
              this.translate.use(lang); // Set your language here
              this.settingService.updateLanguage(lang);
              this.settingService.updateTraduction(lang);
              this.loadMenu();
            });
          }
        });
      } else {
        this.session = false;
        this.globalizationService.getPreferredLanguage().then(language => {
          let lang = language.split("-")[0];
          this.translate.setDefaultLang(lang);
          this.translate.use(lang);
          this.loadMenu();
        });
      }
    });
    // Weit while charge
    this.presentLoading();
    // Check the status of the user session
    this.authService.checkSession();
  }

  private async getSession() {
    this.session = await this.authService.getSession();
  }

  logout(type:string) {
    this.authService.logoutByType(type);
    this.signIn();
  }

  signIn() {
    this.navCtrl.push(LoginPage);
  }

  private loadMenu() {
    this.translate.get("menu").subscribe((menu:any) => {
        // used for an example of ngFor and navigation
        if (this.session) {
          this.pages = [
            { title: menu.items.history, component: HistoryPage },
            { title: menu.items.recognition, component: RecognitionPage },
            { title: menu.items.settings, component: SettingPage }
          ];
        } else {
          this.pages = [
            { title: menu.items.start, component: LoginPage }
          ];
        }
    });
  }

  private presentLoading() {
    const loader = this.loadingCtrl.create({
      content: "Please wait...",
      duration: 2000
    });
    loader.present();
  }

  openPage(page) {
    // Reset the content nav to have just this page
    // we wouldn't want the back button to show in this scenario
    this.navCtrl.setRoot(page.component);
  }

  /*
  saludar() {
    this.testService.hello().then(response => {
        if (response) {
          this.saludo = response;
        }console.log(response);
      }
    );
  }

  ionViewCanEnter(){
		this.nativeStorage.getItem('user').then(data => {
  			this.user = {
          token_type: data.token_type,
          access_token: data.access_token,
          expires_in: data.expires_in,
          email: data.email,
          id: data.id,
          name: data.name,
          first_name: data.first_name,
          last_name: data.last_name,
          picture: data.picture
        };
        this.sesion =  JSON.stringify(this.user);
        this.userReady = true;
        this.events.publish('user:created', this.userReady, Date.now());
		}, error => {
			console.log(error);
		});
	}*/

  /* Borrar cuando quede authService
  private getSession() {
    this.nativeStorage.getItem('user').then(data => {
        this.user = {
          token_type: data.token_type,
          access_token: data.access_token,
          expires_in: data.expires_in,
          email: data.email,
          id: data.id,
          name: data.name,
          first_name: data.first_name,
          last_name: data.last_name,
          picture: data.picture
        };
        this.userReady = true;
    }, error => {
      console.log(error);
    });
  }*/

}

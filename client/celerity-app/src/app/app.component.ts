import { Component, ViewChild } from '@angular/core';
import { Nav, Platform } from 'ionic-angular';

// Plugins
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { AdMob } from "ionic-admob";

// Pages
import { HomePage } from './modules/auth/pages/home/home';
import { LoginPage } from './modules/auth/pages/login/login';
import { RegisterPage } from './modules/auth/pages/register/register';
import { ProfilePage } from './modules/auth/pages/profile/profile';
import { HistoryPage } from './modules/recognition/pages/history/history';
import { SettingPage } from './modules/setting/pages/setting/setting';
import { RecognitionPage } from './modules/recognition/pages/recognition/recognition';

// Actions
import { Events } from 'ionic-angular';

// Providers
import { AuthenticationService } from './modules/auth/providers/authentication.service';
import { GlobalizationService } from './modules/setting/providers/globalization.service';
import { SettingService } from './modules/setting/providers/setting.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) nav: Nav;

  rootPage: any = HomePage;

  pages: Array<{title: string, component: any}>;

  session: any = false;

  constructor(public platform: Platform,
              public statusBar: StatusBar,
              public splashScreen: SplashScreen,
              public events: Events,
              private authService: AuthenticationService,
              private globalizationService: GlobalizationService,
              private translate: TranslateService,
              private settingService: SettingService,
              private admob: AdMob) {

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
        // Get globalization locale and language
        this.globalizationService.getPreferredLanguage().then(language => {
          // Set the default language for translation strings, and the current language.
          let lang = language.split("-")[0];
          this.translate.setDefaultLang(lang);
          this.translate.use(lang); // Set your language here
          this.loadMenu();
        });
      }
    });

    this.initializeApp();
  }

  initializeApp() {
    this.platform.ready().then(/*async*/ () => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      // Check the status of the user session
      this.authService.checkSession();
      // Set Style
      this.statusBar.styleDefault();
      this.splashScreen.hide();
      // Init default translate
      /*const language = await this.initTranslate();*/
      /*if (language) {
        // Load menu
        this.initMenu();
      }*/
      // AdMob Banner
      //this.admob.banner.show({ id: "test" });
      this.admob.banner.show({
        id: {
          // replace with your ad unit IDs
          android: 'test', //'ca-app-pub-1963573377772214~7718327243',
          ios: 'test'
        }
      });

    });
  }

  private async getSession() {
    this.session = await this.authService.getSession();
  }

/*
  initTranslate(): Promise<string> {
    // Get globalization locale and language
    return this.globalizationService.getPreferredLanguage().then(language => {
      // Set the default language for translation strings, and the current language.
      let lang = language.split("-")[0];
      this.translate.setDefaultLang(lang);
      this.translate.use(lang); // Set your language here
      return lang;
    });
  }
*/
  private loadMenu() {
    this.translate.get("menu").subscribe((menu:any) => {
        // used for an example of ngFor and navigation
        if (this.session) {
          this.pages = [
            { title: menu.items.home, component: HomePage },
            { title: menu.items.history, component: HistoryPage },
            { title: menu.items.recognition, component: RecognitionPage },
            /*{ title: menu.items.profile, component: ProfilePage },
            { title: menu.items.password, component: PasswordPage },*/
            { title: menu.items.settings, component: SettingPage }
          ];
        } else {
          this.pages = [
            { title: menu.items.home, component: HomePage },
            { title: menu.items.login, component: LoginPage },
            { title: menu.items.register, component: RegisterPage },
          ];
        }
    });
  }

  openPage(page) {
    // Reset the content nav to have just this page
    // we wouldn't want the back button to show in this scenario
    this.nav.setRoot(page.component);
  }

  logout(type:string) {
    this.authService.logoutByType(type);
    this.nav.setRoot(HomePage);
  }

  goToProfile() {
    this.nav.setRoot(ProfilePage);
  }

}

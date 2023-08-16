import { Component/*, Input*/ } from '@angular/core';
import { NavController } from 'ionic-angular';

// Providers
import { TranslateService } from "@ngx-translate/core";
import { GlobalizationService } from '../../providers/globalization.service';
import { SettingService } from '../../providers/setting.service';

// Models
//import { Setting } from '../../models/setting';
import { Lang } from '../../models/lang';

@Component({
  selector: 'page-setting',
  templateUrl: 'setting.html'
})
export class SettingPage {

  //@Input() setting: Setting = new Setting();

  traductions: Lang[];
  languages: Lang[];

  selectedTraduction: Lang;
  selectedLanguage: Lang;

  constructor(
    public navCtrl: NavController,
    private translate: TranslateService,
    private globalizationService: GlobalizationService,
    private settingService: SettingService) {
      this.initSetting();
  }

  private initSetting() {
    this.settingService.getLanguages().then(traductions => {
      this.traductions = traductions;
      this.getLanguages(); // Get aviaiable languages
    });
  }

  private getSetting() {
    this.settingService.getCurrent().then(setting => {
      this.selectedLanguage = this.getLanguageByCode(setting.language as string);
      this.selectedTraduction = this.getTraductionByCode(setting.traduction as string);
    });
  }

  private getLanguages() {
    this.translate.get("settings").subscribe((settings:any) => {
        this.languages = [
          { ordinal: 1, name: settings.languages.spanish, code: "es" },
          { ordinal: 2, name: settings.languages.english, code: "en" }
        ];
        this.getSetting(); // charge user preferences
    });
  }

  onChangeLanguage(language: Lang) {
    if (language.code) {
      this.changeLanguage(language.code);
      this.settingService.updateLanguage(language.code);
    }
  }

  onChangeTraduction(language: Lang) {
    if (language.code) {
      this.settingService.updateTraduction(language.code);
    }
  }

  public changeLanguage(language) {
    if (!this.existLanguage(language)) {
      language = "en";
    }
    this.translate.use(language);
  }

  private existLanguage(language):boolean {
    for (let l of this.languages) {
      if (l.code == language) {
        return true;
      }
    }
    return false;
  }

  public setDefault() {
    this.globalizationService.getPreferredLanguage().then(language => {
      let defaultLanguage: string = language.split("-")[0] as string;
      this.selectedLanguage = this.getLanguageByCode(defaultLanguage);
      this.selectedTraduction = this.getTraductionByCode(defaultLanguage);
      this.changeLanguage(defaultLanguage);
      this.saveSetting();
    });
  }

  private saveSetting() {
    this.settingService.updateTraduction(this.selectedTraduction.code);
    this.settingService.updateLanguage(this.selectedLanguage.code);
  }

  private getTraductionByCode(code: string):Lang {
    return this.traductions.find(t => t.code == code);
  }

  private getLanguageByCode(code: string): Lang {
    return this.languages.find(l => l.code == code);
  }

}

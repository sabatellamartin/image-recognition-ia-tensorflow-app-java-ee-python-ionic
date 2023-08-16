import { NgModule } from '@angular/core';
import { IonicModule } from 'ionic-angular';

// Plugins
import { Geolocation } from '@ionic-native/geolocation';
import { Globalization } from '@ionic-native/globalization';

// Modules
import { SharedModule } from '../shared/shared.module';
import { AuthModule } from '../auth/auth.module';

// Pages
import { SettingPage } from './pages/setting/setting';

// Providers
import { GlobalizationService } from './providers/globalization.service';
import { SettingService } from './providers/setting.service';

@NgModule({
  declarations: [
    SettingPage
  ],
  imports: [
    IonicModule,
    SharedModule,
    AuthModule
  ],
  exports: [
    SettingPage
  ],
  entryComponents: [
    SettingPage
  ],
  providers: [
    Geolocation,
    Globalization,
    GlobalizationService,
    SettingService
  ]
})
export class SettingModule { }

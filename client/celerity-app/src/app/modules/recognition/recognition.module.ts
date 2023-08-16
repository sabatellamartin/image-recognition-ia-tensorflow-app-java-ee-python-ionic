import { NgModule } from '@angular/core';
import { IonicModule } from 'ionic-angular';

// Plugins
import { CameraPreview } from '@ionic-native/camera-preview';
import { ScreenOrientation } from '@ionic-native/screen-orientation';

// Modules
import { SharedModule } from '../shared/shared.module';
import { AuthModule } from '../auth/auth.module';

// Pages
import { HistoryPage } from './pages/history/history';
import { RecognitionPage } from './pages/recognition/recognition';

// Providers
import { TestService } from './providers/test.service';
import { RecognitionService } from './providers/recognition.service';

@NgModule({
  declarations: [
    HistoryPage,
    RecognitionPage
  ],
  imports: [
    IonicModule,
    SharedModule,
    AuthModule
  ],
  exports: [
    HistoryPage,
    RecognitionPage
  ],
  entryComponents: [
    HistoryPage,
    RecognitionPage
  ],
  providers: [
    CameraPreview,
    ScreenOrientation,
    TestService,
    RecognitionService
  ]
})
export class RecognitionModule { }

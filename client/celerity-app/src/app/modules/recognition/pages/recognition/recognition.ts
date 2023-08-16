import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { LoadingController } from 'ionic-angular';

// Plugins
import { CameraPreview, CameraPreviewPictureOptions, CameraPreviewOptions } from '@ionic-native/camera-preview';
import { ScreenOrientation } from '@ionic-native/screen-orientation';

// Providers
import { RecognitionService } from '../../providers/recognition.service';
import { SettingService } from '../../../setting/providers/setting.service';

// Pages
import { HistoryPage } from '../history/history';
import { HomePage } from '../../../auth/pages/home/home';

// Models
import { Classify } from '../../models/classify';
import { Lang } from '../../../setting/models/lang';

@Component({
  selector: 'page-recognition',
  templateUrl: 'recognition.html'
})
export class RecognitionPage {

  picture: string;

  classify: Classify;

  selectedSegment: string = 'translated';

  traductions: Lang[];

  selectedTraduction: Lang;

  isCameraStarted: boolean = false;

  // camera options (Size and location). In the following example, the preview uses the rear camera and display the preview in the back of the webview
  cameraPreviewOpts: CameraPreviewOptions = {
    x: 0,
    y: 0,
    width:  window.innerWidth, // default: window.screen.width,
    height: window.innerHeight-75, // default: window.screen.height,
    camera: 'rear',
    tapPhoto: false,
    tapToFocus: true,
    previewDrag: false,
    toBack: false,
    alpha: 1
  };

  // picture options
  pictureOpts: CameraPreviewPictureOptions = {
    width: window.innerWidth, // default: 1280
    height: window.innerHeight, // default: 1280
    quality: 100 // default: 85
  };

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    public loadingCtrl: LoadingController,
    private cameraPreview: CameraPreview,
    private recognitionService: RecognitionService,
    private settingService: SettingService,
    private screenOrientation: ScreenOrientation) {
      this.getTraductions();
  }

  goToHistory() {
    this.navCtrl.setRoot(HistoryPage);
  }

  /*presentLoading() {
    const loader = this.loadingCtrl.create({
      content: "Please wait..."
      //duration: 2000
    });
    loader.present();
  }*/

  ionViewDidLoad() {
    const file = this.navParams.get('file');
    const traduction = this.navParams.get('traduction');
    const picture = this.navParams.get('picture');
    if (file && traduction && picture) {
      this.picture = picture;
      this.classifyFromQuery(file, traduction);
    } else {
      this.startCamera();
    }
  }

  ionViewDidLeave() {
    this.stopCamera();
  }

  async startCamera() {
    // start camera
    await this.cameraPreview.startCamera(this.cameraPreviewOpts);
    this.isCameraStarted=true;
    // set to landscape
    this.screenOrientation.lock(this.screenOrientation.ORIENTATIONS.PORTRAIT);
  }

  async stopCamera() {
    // Stop the camera preview
    await this.cameraPreview.stopCamera();
    this.isCameraStarted=false;
  }

  async takePicture() {
    // take a picture
    const result = await this.cameraPreview.takePicture(this.pictureOpts);
    // Stop the camera preview
    this.stopCamera();
    this.picture = `data:image/jpeg;base64,${result}`;
    let loader = this.loadingCtrl.create({content: "Please wait..."});
    loader.present().then(() => {
      this.recognitionService.uploadImage(this.picture, this.selectedTraduction.code).then(response => {
          loader.dismiss();
          this.classify = response;
      });
    });
    // allow user rotate
    this.screenOrientation.unlock();
  }

  switchCamera() {
    // Switch camera
    this.cameraPreview.switchCamera();
  }

  backCamera() {
    if (this.picture) {
      this.stopCamera();
    } else {
      this.navCtrl.setRoot(HomePage);
    }
  }

  colorEffect() {
    // set color effect to negative
    this.cameraPreview.setColorEffect('negative');
  }

  private getTraductions() {
    this.settingService.getLanguages().then(traductions => {
      this.traductions = traductions;
      this.getCurrentTraduction();
    });
  }

  private getCurrentTraduction() {
    this.settingService.getCurrent().then(setting => {
      this.selectedTraduction = this.getTraductionByCode(setting.traduction as string);
    });
  }

  private getTraductionByCode(code: string):Lang {
    return this.traductions.find(t => t.code == code);
  }

  onChangeTraduction(language: Lang) {
    if (language.code) {
      this.selectedTraduction = this.getTraductionByCode(language.code as string);
      let loader = this.loadingCtrl.create({content: "Please wait..."});
      loader.present().then(() => {
        this.recognitionService.classify(this.classify.name, this.selectedTraduction.code).then(response => {
          loader.dismiss();
          this.classify = response;
        });
      });
    }
  }

  private classifyFromQuery(file:string, language:string) {
    let loader = this.loadingCtrl.create({content: "Please wait..."});
    loader.present().then(() => {
      this.recognitionService.classify(file, language).then(response => {
        loader.dismiss();
        this.classify = response;
      });
    });
  }

}

import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';

// PLugins
import { LoadingController } from 'ionic-angular';

// Providers
import { RecognitionService } from '../../providers/recognition.service';

// Pages
import { RecognitionPage } from '../recognition/recognition';

// Models
import { Query } from '../../models/query';

@Component({
  selector: 'page-history',
  templateUrl: 'history.html'
})
export class HistoryPage {
  /*selectedItem: any;
  icons: string[];
  items: Array<{title: string, note: string, icon: string}>;
  */
  querys: Query[];

  constructor(
    public navCtrl: NavController,
    public navParams: NavParams,
    public loadingCtrl: LoadingController,
    private recognitionService: RecognitionService) {

    this.getQuerys();
    /*
    // If we navigated to this page, we will have an item available as a nav param
    this.selectedItem = navParams.get('item');
    // Let's populate this page with some filler content for funzies
    this.icons = ['flask', 'wifi', 'beer', 'football', 'basketball', 'paper-plane',
    'american-football', 'boat', 'bluetooth', 'build'];
    this.items = [];
    for (let i = 1; i < 11; i++) {
      this.items.push({
        title: 'Item ' + i,
        note: 'This is item #' + i,
        icon: this.icons[Math.floor(Math.random() * this.icons.length)]
      });
    }*/
  }

/*
  itemTapped(event, query) {
    // That's right, we're pushing to ourselves!
    this.navCtrl.push(RecognitionPage, {
      outputFile: query.outputFile,
      lang: query.to
    });
  }*/

  view(query: Query) {
    this.navCtrl.push(RecognitionPage, {
      file: query.outputFile,
      traduction: query.langcode,
      picture: this.getImage(query.encodedFile)
    });
  }

  private getQuerys() {
    let loader = this.loadingCtrl.create({content: "Please wait..."});
    loader.present().then(() => {
      this.recognitionService.getMyActiveQueryHistory().then(response => {
        loader.dismiss();
        this.querys = response;
      });
    });
  }

  getImage(encodedFile) {
    return `data:image/jpeg;base64,${encodedFile}`;
  }

  /*
  private presentLoading() {
    const loader = this.loadingCtrl.create({
      content: "Please wait...",
      duration: 2000
    });
    loader.present();
  }
  */
}

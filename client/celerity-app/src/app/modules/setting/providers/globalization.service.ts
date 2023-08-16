import { Injectable } from '@angular/core';

// Plugins
import { Globalization } from '@ionic-native/globalization';
import { Geolocation } from '@ionic-native/geolocation';

@Injectable()
export class GlobalizationService {

  constructor(
    private globalization: Globalization,
    private geolocation: Geolocation) {

  }

  public getPreferredLanguage(): Promise<string> {
    return this.globalization.getPreferredLanguage()
      .then(language => language.value as string)
      .catch(e => {
        console.log(e);
        return "en-US";
      });
  }

  public getLocaleName(): Promise<string> {
    return this.globalization.getLocaleName()
      .then(locale => locale.value as string)
      .catch(e => {
        console.log(e);
        return "en-US";
      });
  }

  public getCurrentPosition(): Promise<Coordinates> {
    return this.geolocation.getCurrentPosition().then((resp) => {
      // resp.coords.latitude
      // resp.coords.longitude
      //alert(resp.coords.latitude+" "+resp.coords.longitude);
      return resp.coords;
    }).catch((error) => {
      console.log('Error getting location', error);
      return new Coordinates();
    });
  }

  public getWatchPosition() {
    let watch = this.geolocation.watchPosition();
      watch.subscribe((data) => {
       // data can be a set of coordinates, or an error (if an error occurred).
       // data.coords.latitude
       // data.coords.longitude
    });
  }

}

import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';

// Providers
import { SharedService } from '../../shared/providers/shared.service';

// Models
import { Client } from '../models/client';

// Plugins
//import { GooglePlus } from '@ionic-native/google-plus';
//import { Facebook, FacebookLoginResponse } from '@ionic-native/facebook';
import { NativeStorage } from '@ionic-native/native-storage';

// Actions
import { Events } from 'ionic-angular';

@Injectable()
export class AuthenticationService {

  private url: string;

  constructor(private http : Http,
              private shared: SharedService,
              //private gp: GooglePlus,
              //private fb: Facebook,
              private nativeStorage: NativeStorage,
              public events: Events) {
    this.url = `${this.shared.endpoint}/authentication`;
  }

  private getHeaders(type: string, token: string): Headers {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    headers.append('Authorization', type + " " + token);
    return headers;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  login(username: string, password: string): Promise<any> {
    const headers = new Headers({'Content-Type': 'application/json'});
    return this.http
      .post(this.url, JSON.stringify({ username: username, password: password }), {headers : headers})
      .toPromise()
      .then(async response => {
        const token = response.text() as string;
        if (token) {
          return await this.storeSession("Bearer",token, 3600*24*7);
        } else {
          return false;
        }
     }).catch(e => {
         if (e.status === 401) { return false; }
     });
  }

  /* Google
  loginGoogle(): Promise<any> {
    return this.gp.login({}).then(async result => {
      if(result!=null) {
        //return await this.storeSessionGP(result);
        return await this.storeSession("Google", result.accessToken, result.expires);
      } else {
        return false;
      }
    }).catch(e => {
      console.error('Error login Google', e);
    });
  }*/

  /* Facebook
  loginFacebook(): Promise<any> {
    //the permissions your facebook app needs from the user
    let permissions = ["public_profile", "user_friends", "email"];
    return this.fb.login(permissions).then(async (res: FacebookLoginResponse) => {
      if(res.status == 'connected') {
        //return await this.storeSessionFB(res.authResponse);
        return await this.storeSession("Facebook", res.authResponse.accessToken, res.authResponse.expiresIn);
      } else {
        return false;
      }
    }).catch(e => {
      console.error('Error login Facebook', e);
    });
  } */

  storeSession(type: string, token: string, expires_in: number) : Promise<any> {
    const url = `${this.url}/current`;
    return this.http
     .get(url, new RequestOptions({headers: this.getHeaders(type, token)}))
     .toPromise()
     .then(client => {
       let user = client.json() as Client;
       return this.nativeStorage.setItem('user',
       {
         token_type: type,
         access_token: token,
         expires_in: expires_in,
         email: user.email,
         id: user.id,
         name: user.firstname!=null && user.lastname!=null ? user.firstname+" "+user.lastname : "",
         first_name: user.firstname,
         last_name: user.lastname,
         picture: user.picture
       }).then(() => {
         this.publishSession(true);
         return true;
       }, e => console.error('Error storing item', e));
     }).catch(this.handleError);
  }

  getSession(): Promise<any> {
    return this.nativeStorage.getItem('user').then(data => {
        const session = {
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
        return session;
    }, error => {
      console.log(error);
      return false;
    });
  }

  /* Facebook
  logoutFB() {
    this.fb.logout().then(() => {
      this.logout();
    });
  }*/

  /* Google
  logoutGP() {
    this.gp.logout().then(() => {
      this.logout();
    });
  }*/

  logout(): void {
    //user logged out so we will remove him from the NativeStorage
    this.nativeStorage.remove('user');
    this.publishSession(false);
  }

  logoutByType(type: string): void {
    /*if(type=="Facebook") {
      this.logoutFB();
    } else if (type=="Google") {
      this.logoutGP();
    } else {*/
      this.logout();
    //}
  }

  async current(): Promise<Client> {
    const session = await this.getSession();
    const headers = this.getHeaders(session.token_type, session.access_token);
    const url = `${this.url}/current`;
    return this.http
     .get(url, new RequestOptions({headers: headers}))
     .toPromise()
     .then(client => {
       return client.json() as Client;
     }).catch(this.handleError);
  }

  private publishSession(status: boolean) {
    this.events.publish('session:stored', status, Date.now());
  }

  public async checkSession() {
    const session = await this.getSession();
    if (session) {
      //const type = session.token_type;
      /*if(type=="Facebook") {
        // Renew access token
        this.fb.getLoginStatus().then(response => {

          // Pobar luego
          alert(JSON.stringify(response));

          if(response.status!="connected") {
            this.fb.getAccessToken().then(res => {

              // Pobar luego
              alert(JSON.stringify(res));

            });
          }
        });
      } else if (type=="Google") {
        // Renew access token
        this.gp.trySilentLogin({
          //'scopes': '... ', // optional - space-separated list of scopes, If not included or empty, defaults to `profile` and `email`.
          //'webClientId': 'client id of the web app/server side', // optional - clientId of your Web application from Credentials settings of your project - On Android, this MUST be included to get an idToken. On iOS, it is not required.
          //'offline': true, // Optional, but requires the webClientId - if set to true the plugin will also return a serverAuthCode, which can be used to grant offline access to a non-Google server
        }).then(result => {
          if(result!=null) {
            this.storeSession("Google", result.accessToken, result.expires);
          }
        });
      } else {
        //this.logout();
      }*/
      this.publishSession(true);
    } else {
      this.logout();
    }
  }

  /* Google
  private storeSessionGP(userInfo: any): Promise<any> {
    return this.nativeStorage.setItem('user',
    {
      token_type: "Google",
      access_token: userInfo.accessToken,
      expires_in: userInfo.expires_in,
      email: userInfo.email,
      id: userInfo.userId,
      name: userInfo.displayName,
      first_name: userInfo.givenName,
      last_name: userInfo.familyName,
      picture: userInfo.imageUrl
    }).then(() => {return true;}, e => console.error('Error storing Google item', e));
  } */

  /* Facebook
  private storeSessionFB(userInfo: any): Promise<any>  {
    let permissions = ["public_profile", "user_friends", "email"];
    //Getting name and gender properties
    let params = "/me?fields=id,name,email,first_name,last_name,picture,gender";
    return this.fb.api(params, permissions).then(res => {
      //user.picture = "https://graph.facebook.com/" + userId + "/picture?type=large";
      //now we have the users info, let's save it in the NativeStorage
      return this.nativeStorage.setItem('user',
      {
        token_type: "Facebook",
        access_token: userInfo.accessToken,
        expires_in: userInfo.expiresIn,
        email: res.email,
        id: userInfo.userID,
        name: res.name,
        first_name: res.first_name,
        last_name: res.last_name,
        picture: res.picture.data.url
      }).then(() => {return true;}, e => console.error('Error storing Facebook item', e));

    })
    .catch(error => {
      console.error( error );
    });
  } */

}

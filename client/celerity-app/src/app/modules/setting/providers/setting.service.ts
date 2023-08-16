import { Injectable } from '@angular/core';
import { Headers, Http, RequestOptions } from '@angular/http';

// Providers
import { SharedService } from '../../shared/providers/shared.service';
import { AuthenticationService } from '../../auth/providers/authentication.service';

// Models
import { Setting } from '../models/setting';
import { Lang } from '../models/lang';

@Injectable()
export class SettingService {

  private url: string; // URL to web api
  private session: any;

  constructor(
    private http : Http,
    private shared: SharedService,
    private authService: AuthenticationService) {
    this.url = `${this.shared.endpoint}/settings`;
    this.getSession()
  }

  private async getSession() {
    this.session = await this.authService.getSession();
  }

  private getHeaders(): Headers {
    let headers = new Headers();
    if (this.session) {
      headers.append('Authorization', this.session.token_type + " " + this.session.access_token);
      headers.append('Content-Type', 'application/json');
    }
    return headers;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  updateLanguage(language: string): Promise<Setting> {
    const url = `${this.url}/language`;
    return this.http
      .put(url, language as string, {headers: this.getHeaders()})
      .toPromise()
      .then(response => response.json() as Setting)
      .catch(e => new Setting());
  }

  updateTraduction(traduction: string): Promise<Setting> {
    const url = `${this.url}/traduction`;
    return this.http
      .put(url, traduction as string, {headers: this.getHeaders()})
      .toPromise()
      .then(response => response.json() as Setting)
      .catch(e => new Setting());
  }

  getCurrent(): Promise<Setting> {
    return this.authService.getSession().then(session => {
      this.session = session;
      let options = new RequestOptions({headers: this.getHeaders()});
      const url = `${this.url}`;
      return this.http.get(url, options)
                 .toPromise()
                 .then(response => response.json() as Setting)
                 .catch(this.handleError);
    });
  }

  getLanguages(): Promise<Lang[]> {
    return this.authService.getSession().then(session => {
      this.session = session;
      const url = `${this.url}/languages`;
      return this.http.get(url, {headers: this.getHeaders()})
                 .toPromise()
                 .then(response => response.json() as Lang[])
                 .catch(this.handleError);
    });
  }

}

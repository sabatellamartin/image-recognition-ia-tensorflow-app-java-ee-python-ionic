import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

// Providers
import { SharedService } from '../../shared/providers/shared.service';
import { AuthenticationService } from '../../auth/providers/authentication.service';

import { Classify } from '../models/classify';

import { Query } from '../models/query';

@Injectable()
export class RecognitionService {

  private url: string; // URL to web api
  private session: any ;

  constructor(
    private http : Http,
    private shared: SharedService,
    private authService: AuthenticationService) {
    this.url = `${this.shared.endpoint}/recognition`;
    this.getSession();
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

  uploadImage(img: string, traduction: string): Promise<Classify> {
    const url = `${this.url}/upload/img/${traduction}`;
    return this.http
               .post(url, JSON.stringify(img), {headers: this.getHeaders()})
               .toPromise()
               .then(request => request.json() as Classify)
               .catch(this.handleError);
  }

  classify(filename: string, traduction: string): Promise<Classify> {
    const url = `${this.url}/classify/${traduction}`;
    return this.http
               .post(url, filename as string, {headers: this.getHeaders()})
               .toPromise()
               .then(request => request.json() as Classify)
               .catch(this.handleError);
  }

  getMyActiveQueryHistory(): Promise<Query[]> {
    return this.authService.getSession().then(session => {
      this.session = session;
      const url = `${this.url}/active`;
      return this.http.get(url, {headers: this.getHeaders()})
                 .toPromise()
                 .then(response => response.json() as Query[])
                 .catch(this.handleError);
    });
  }

  getEncodedFileByName(filename: string): Promise<string> {
    const url = `${this.url}/encoded`;
    return this.http
               .post(url, filename as string, {headers: this.getHeaders()})
               .toPromise()
               .then(request => request.json() as string)
               .catch(this.handleError);
  }

}

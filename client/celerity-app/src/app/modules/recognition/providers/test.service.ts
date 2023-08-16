import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

import { SharedService } from '../../shared/providers/shared.service';

@Injectable()
export class TestService {

  private url: string; // URL to web api

  constructor(private http : Http, private shared: SharedService) {
    this.url = `${this.shared.endpoint}/test`;
  }

  private getHeaders(): Headers {
    const headers = new Headers();
    headers.append('Content-Type', 'application/json');
    return headers;
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  hello(): Promise<string> {
    return this.http.get(this.url, {headers: this.getHeaders()})
               .toPromise()
               .then(response => response.text() as string)
               .catch(this.handleError);
  }

}

import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';

// Providers
import { SharedService } from '../../shared/providers/shared.service';
import { AuthenticationService } from './authentication.service';

// Models
import { Client } from '../models/client';

@Injectable()
export class UserService {

  private url: string; // URL to web api
  private session: any ;

  constructor(
    private http : Http,
    private shared: SharedService,
    private authService: AuthenticationService) {
    this.url = `${this.shared.endpoint}/users`;
    this.authService.getSession().then(session => { this.session=session } );
  }

  private getHeaders(): Headers {
    let headers = new Headers();
    if (this.session) { headers.append('Authorization', this.session.token_type + " " + this.session.access_token); }
    headers.append('Content-Type', 'application/json');
    return headers;
  }

  /*
  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }*/

  register(client: Client): Promise<boolean> {
    client.picture =  this.shared.defaultUserImg;
    const headers = new Headers({'Content-Type': 'application/json'});
    const url = `${this.url}/register`;
    return this.http
      .post(url, JSON.stringify(client), {headers: headers})
      .toPromise()
      .then(request => request.json() as boolean)
      .catch(e => { return false; });
  }

  update(client: Client): Promise<Client> {
    const url = `${this.url}/update/client/${client.id}`;
    return this.http
      .put(url, JSON.stringify(client), {headers: this.getHeaders()})
      .toPromise()
      .then(async request => {
        if (this.session) {
          await this.authService.storeSession(this.session.token_type, this.session.access_token, this.session.expires_in);
        } else {
          this.authService.logout();
        }
        return request.json() as Client;
      })
      .catch(e => { return null; });
  }

  setPassword(client: Client): Promise<boolean> {
    const url = `${this.url}/set/password/${client.id}`;
    return this.http
      .put(url, JSON.stringify(client), {headers: this.getHeaders()})
      .toPromise()
      .then(request => request.json() as boolean)
      .catch(e => { return false });
  }

/*
  getByUsername(username: string): Promise<Usuario> {
      let options = new RequestOptions({headers: this.getHeaders()});
      const url = `${this.url}/username/${username}`;
      return this.http
                 .get(url, options)
                 .toPromise()
                 .then(response => response.json() as Usuario)
                 .catch(this.handleError);
  }

  getByEmail(email: string): Promise<Usuario> {
      let options = new RequestOptions({headers: this.getHeaders()});
      const url = `${this.url}/email/${email}`;
      return this.http
                 .get(url, options)
                 .toPromise()
                 .then(response => response.json() as Usuario)
                 .catch(this.handleError);
  }

  getById(id: number): Promise<Usuario> {
    let options = new RequestOptions({headers: this.getHeaders()});
    const url = `${this.url}/${id}`;
    return this.http.get(url, options)
               .toPromise()
               .then(response => response.json() as Usuario)
               .catch(this.handleError);
  }

  existByEmail(email: string): Promise<Boolean> {
      let options = new RequestOptions({headers: this.getHeaders()});
      const url = `${this.url}/exist/email/${email}`;
      return this.http
                 .get(url, options)
                 .toPromise()
                 .then(response => response.json() as Boolean)
                 .catch(this.handleError);
  }

  getClienteByEmail(email: string): Promise<Operador> {
      let options = new RequestOptions({headers: this.getHeaders()});
      const url = `${this.url}/email/operador/${email}`;
      return this.http
                 .get(url, options)
                 .toPromise()
                 .then(response => response.json() as Operador)
                 .catch(this.handleError);
  }

  update(usuario: any, tipo:string): Promise<Boolean> {
    const url = tipo=="ADMINISTRADOR" ? `${this.url}/update/administrador/${usuario.id}` : `${this.url}/update/operador/${usuario.id}`;
    return this.http
               .put(url, JSON.stringify(usuario), {headers: this.getHeaders()})
               .toPromise()
               .then(request => request.json() as Boolean)
               .catch(this.handleError);
  }
*/

}

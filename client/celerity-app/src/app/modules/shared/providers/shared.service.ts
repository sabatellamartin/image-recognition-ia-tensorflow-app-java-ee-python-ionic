import { Injectable } from '@angular/core';

@Injectable()
export class SharedService {

  public endpoint: string;

  public defaultUserImg: string;

  constructor() {
    // From localhost to docker
    this.endpoint = 'http://localhost:8080/celerity-web/rest';
    this.defaultUserImg = "./assets/avatars/avatar.png";
  }

}

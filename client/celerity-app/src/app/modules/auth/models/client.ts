import { User } from './user';

export class Client extends User {
  firstname: string;
  lastname: string;
  picture: string;
  constructor() {
    super();
  }
}

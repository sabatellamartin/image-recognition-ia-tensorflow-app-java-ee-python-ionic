
import { Setting } from '../../setting/models/setting';

export class User {
  id: number;
  email: string;
  password: string;
  creation: Date;
  lock: Date;
  setting: Setting;
  constructor() {
    this.setting = new Setting();
  }
}

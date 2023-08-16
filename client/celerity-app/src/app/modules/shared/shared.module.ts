import { NgModule } from '@angular/core';

// Modules
import { TranslateModule } from "@ngx-translate/core";

// Providers
import { SharedService } from './providers/shared.service';

@NgModule({
  imports: [
    TranslateModule.forChild()
  ],
  declarations: [
  ],
  entryComponents: [
  ],
  exports: [
    TranslateModule
  ],
  providers: [
    SharedService
  ]
})
export class SharedModule { }

import { WebPlugin } from '@capacitor/core';

import type { ufacesdkPlugin , ScoreResult , ConfigOptions } from './definitions';

export class ufacesdkWeb extends WebPlugin implements ufacesdkPlugin {

  async verify(_options: ConfigOptions ): Promise<ScoreResult> {
    throw this.unimplemented('Not implemented on web.') ;
  }

  async test(_options: {baseurl:string}) : Promise<ScoreResult>  { 
    throw this.unimplemented('Not implemented on web.') ;
  }

}

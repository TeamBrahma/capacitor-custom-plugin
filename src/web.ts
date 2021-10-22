import { WebPlugin } from '@capacitor/core';

import type { CustomPlugin } from './definitions';

export class CustomWeb extends WebPlugin implements CustomPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}

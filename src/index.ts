import { registerPlugin } from '@capacitor/core';

import type { CustomPlugin } from './definitions';

const Custom = registerPlugin<CustomPlugin>('Custom', {
  web: () => import('./web').then(m => new m.CustomWeb()),
});

export * from './definitions';
export { Custom };

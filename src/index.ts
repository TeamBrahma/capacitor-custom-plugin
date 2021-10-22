import { registerPlugin } from '@capacitor/core';

import type { CustomPlugin } from './definitions';

const Contacts = registerPlugin<CustomPlugin>('Custom', {
  web: () => import('./web').then(m => new m.ContactsPluginWeb()),
});

export * from './definitions';
export { Contacts };
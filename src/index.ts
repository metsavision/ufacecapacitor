import { registerPlugin } from '@capacitor/core';

import type { ufacesdkPlugin } from './definitions';

const ufacesdk = registerPlugin<ufacesdkPlugin>('ufacesdk', {
  web: () => import('./web').then(m => new m.ufacesdkWeb()),
});

export * from './definitions';
export { ufacesdk };

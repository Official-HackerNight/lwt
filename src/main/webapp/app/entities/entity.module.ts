import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { LwtEpisodeModule } from './episode/episode.module';
import { LwtResourceModule } from './resource/resource.module';
import { LwtTagModule } from './tag/tag.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        LwtEpisodeModule,
        LwtResourceModule,
        LwtTagModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LwtEntityModule {}

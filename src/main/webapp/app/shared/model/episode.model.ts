import { Moment } from 'moment';
import { ITag } from 'app/shared/model//tag.model';
import { IResource } from 'app/shared/model//resource.model';

export interface IEpisode {
    id?: number;
    title?: string;
    description?: string;
    airDate?: Moment;
    episodeNumber?: number;
    episodeSeason?: number;
    thumbnailUrl?: string;
    youtubeUrl?: string;
    callToAction?: string;
    callToActionDueDate?: Moment;
    tags?: ITag[];
    resources?: IResource[];
}

export class Episode implements IEpisode {
    constructor(
        public id?: number,
        public title?: string,
        public description?: string,
        public airDate?: Moment,
        public episodeNumber?: number,
        public episodeSeason?: number,
        public thumbnailUrl?: string,
        public youtubeUrl?: string,
        public callToAction?: string,
        public callToActionDueDate?: Moment,
        public tags?: ITag[],
        public resources?: IResource[]
    ) {}
}

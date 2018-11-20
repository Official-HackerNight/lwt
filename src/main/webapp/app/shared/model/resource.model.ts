import { IEpisode } from 'app/shared/model//episode.model';

export interface IResource {
    id?: number;
    name?: string;
    url?: string;
    description?: string;
    episode?: IEpisode;
}

export class Resource implements IResource {
    constructor(public id?: number, public name?: string, public url?: string, public description?: string, public episode?: IEpisode) {}
}

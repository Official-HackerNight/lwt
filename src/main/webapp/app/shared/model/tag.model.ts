import { IEpisode } from 'app/shared/model//episode.model';

export interface ITag {
    id?: number;
    name?: string;
    episodes?: IEpisode[];
}

export class Tag implements ITag {
    constructor(public id?: number, public name?: string, public episodes?: IEpisode[]) {}
}

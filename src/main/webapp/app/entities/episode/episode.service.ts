import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEpisode } from 'app/shared/model/episode.model';

type EntityResponseType = HttpResponse<IEpisode>;
type EntityArrayResponseType = HttpResponse<IEpisode[]>;

@Injectable({ providedIn: 'root' })
export class EpisodeService {
    public resourceUrl = SERVER_API_URL + 'api/episodes';

    constructor(private http: HttpClient) {}

    create(episode: IEpisode): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(episode);
        return this.http
            .post<IEpisode>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(episode: IEpisode): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(episode);
        return this.http
            .put<IEpisode>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IEpisode>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IEpisode[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(episode: IEpisode): IEpisode {
        const copy: IEpisode = Object.assign({}, episode, {
            airDate: episode.airDate != null && episode.airDate.isValid() ? episode.airDate.format(DATE_FORMAT) : null,
            callToActionDueDate:
                episode.callToActionDueDate != null && episode.callToActionDueDate.isValid()
                    ? episode.callToActionDueDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.airDate = res.body.airDate != null ? moment(res.body.airDate) : null;
            res.body.callToActionDueDate = res.body.callToActionDueDate != null ? moment(res.body.callToActionDueDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((episode: IEpisode) => {
                episode.airDate = episode.airDate != null ? moment(episode.airDate) : null;
                episode.callToActionDueDate = episode.callToActionDueDate != null ? moment(episode.callToActionDueDate) : null;
            });
        }
        return res;
    }
}

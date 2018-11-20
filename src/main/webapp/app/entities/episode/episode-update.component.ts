import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IEpisode } from 'app/shared/model/episode.model';
import { EpisodeService } from './episode.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';

@Component({
    selector: 'jhi-episode-update',
    templateUrl: './episode-update.component.html'
})
export class EpisodeUpdateComponent implements OnInit {
    episode: IEpisode;
    isSaving: boolean;

    tags: ITag[];
    airDateDp: any;
    callToActionDueDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private episodeService: EpisodeService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ episode }) => {
            this.episode = episode;
        });
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.episode.id !== undefined) {
            this.subscribeToSaveResponse(this.episodeService.update(this.episode));
        } else {
            this.subscribeToSaveResponse(this.episodeService.create(this.episode));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IEpisode>>) {
        result.subscribe((res: HttpResponse<IEpisode>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

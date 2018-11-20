/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { EpisodeService } from 'app/entities/episode/episode.service';
import { IEpisode, Episode } from 'app/shared/model/episode.model';

describe('Service Tests', () => {
    describe('Episode Service', () => {
        let injector: TestBed;
        let service: EpisodeService;
        let httpMock: HttpTestingController;
        let elemDefault: IEpisode;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(EpisodeService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Episode(0, 'AAAAAAA', 'AAAAAAA', currentDate, 0, 0, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        airDate: currentDate.format(DATE_FORMAT),
                        callToActionDueDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Episode', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        airDate: currentDate.format(DATE_FORMAT),
                        callToActionDueDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        airDate: currentDate,
                        callToActionDueDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Episode(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Episode', async () => {
                const returnedFromService = Object.assign(
                    {
                        title: 'BBBBBB',
                        description: 'BBBBBB',
                        airDate: currentDate.format(DATE_FORMAT),
                        episodeNumber: 1,
                        episodeSeason: 1,
                        thumbnailUrl: 'BBBBBB',
                        youtubeUrl: 'BBBBBB',
                        callToAction: 'BBBBBB',
                        callToActionDueDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        airDate: currentDate,
                        callToActionDueDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Episode', async () => {
                const returnedFromService = Object.assign(
                    {
                        title: 'BBBBBB',
                        description: 'BBBBBB',
                        airDate: currentDate.format(DATE_FORMAT),
                        episodeNumber: 1,
                        episodeSeason: 1,
                        thumbnailUrl: 'BBBBBB',
                        youtubeUrl: 'BBBBBB',
                        callToAction: 'BBBBBB',
                        callToActionDueDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        airDate: currentDate,
                        callToActionDueDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Episode', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});

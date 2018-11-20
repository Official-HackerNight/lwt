/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EpisodeComponentsPage, EpisodeDeleteDialog, EpisodeUpdatePage } from './episode.page-object';

const expect = chai.expect;

describe('Episode e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let episodeUpdatePage: EpisodeUpdatePage;
    let episodeComponentsPage: EpisodeComponentsPage;
    let episodeDeleteDialog: EpisodeDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Episodes', async () => {
        await navBarPage.goToEntity('episode');
        episodeComponentsPage = new EpisodeComponentsPage();
        expect(await episodeComponentsPage.getTitle()).to.eq('lwtApp.episode.home.title');
    });

    it('should load create Episode page', async () => {
        await episodeComponentsPage.clickOnCreateButton();
        episodeUpdatePage = new EpisodeUpdatePage();
        expect(await episodeUpdatePage.getPageTitle()).to.eq('lwtApp.episode.home.createOrEditLabel');
        await episodeUpdatePage.cancel();
    });

    it('should create and save Episodes', async () => {
        const nbButtonsBeforeCreate = await episodeComponentsPage.countDeleteButtons();

        await episodeComponentsPage.clickOnCreateButton();
        await promise.all([
            episodeUpdatePage.setTitleInput('title'),
            episodeUpdatePage.setDescriptionInput('description'),
            episodeUpdatePage.setAirDateInput('2000-12-31'),
            episodeUpdatePage.setEpisodeNumberInput('5'),
            episodeUpdatePage.setEpisodeSeasonInput('5'),
            episodeUpdatePage.setThumbnailUrlInput('thumbnailUrl'),
            episodeUpdatePage.setYoutubeUrlInput('youtubeUrl'),
            episodeUpdatePage.setCallToActionInput('callToAction'),
            episodeUpdatePage.setCallToActionDueDateInput('2000-12-31')
            // episodeUpdatePage.tagSelectLastOption(),
        ]);
        expect(await episodeUpdatePage.getTitleInput()).to.eq('title');
        expect(await episodeUpdatePage.getDescriptionInput()).to.eq('description');
        expect(await episodeUpdatePage.getAirDateInput()).to.eq('2000-12-31');
        expect(await episodeUpdatePage.getEpisodeNumberInput()).to.eq('5');
        expect(await episodeUpdatePage.getEpisodeSeasonInput()).to.eq('5');
        expect(await episodeUpdatePage.getThumbnailUrlInput()).to.eq('thumbnailUrl');
        expect(await episodeUpdatePage.getYoutubeUrlInput()).to.eq('youtubeUrl');
        expect(await episodeUpdatePage.getCallToActionInput()).to.eq('callToAction');
        expect(await episodeUpdatePage.getCallToActionDueDateInput()).to.eq('2000-12-31');
        await episodeUpdatePage.save();
        expect(await episodeUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await episodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Episode', async () => {
        const nbButtonsBeforeDelete = await episodeComponentsPage.countDeleteButtons();
        await episodeComponentsPage.clickOnLastDeleteButton();

        episodeDeleteDialog = new EpisodeDeleteDialog();
        expect(await episodeDeleteDialog.getDialogTitle()).to.eq('lwtApp.episode.delete.question');
        await episodeDeleteDialog.clickOnConfirmButton();

        expect(await episodeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});

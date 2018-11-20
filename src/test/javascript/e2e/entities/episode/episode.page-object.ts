import { element, by, ElementFinder } from 'protractor';

export class EpisodeComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-episode div table .btn-danger'));
    title = element.all(by.css('jhi-episode div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class EpisodeUpdatePage {
    pageTitle = element(by.id('jhi-episode-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    titleInput = element(by.id('field_title'));
    descriptionInput = element(by.id('field_description'));
    airDateInput = element(by.id('field_airDate'));
    episodeNumberInput = element(by.id('field_episodeNumber'));
    episodeSeasonInput = element(by.id('field_episodeSeason'));
    thumbnailUrlInput = element(by.id('field_thumbnailUrl'));
    youtubeUrlInput = element(by.id('field_youtubeUrl'));
    callToActionInput = element(by.id('field_callToAction'));
    callToActionDueDateInput = element(by.id('field_callToActionDueDate'));
    tagSelect = element(by.id('field_tag'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setTitleInput(title) {
        await this.titleInput.sendKeys(title);
    }

    async getTitleInput() {
        return this.titleInput.getAttribute('value');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async setAirDateInput(airDate) {
        await this.airDateInput.sendKeys(airDate);
    }

    async getAirDateInput() {
        return this.airDateInput.getAttribute('value');
    }

    async setEpisodeNumberInput(episodeNumber) {
        await this.episodeNumberInput.sendKeys(episodeNumber);
    }

    async getEpisodeNumberInput() {
        return this.episodeNumberInput.getAttribute('value');
    }

    async setEpisodeSeasonInput(episodeSeason) {
        await this.episodeSeasonInput.sendKeys(episodeSeason);
    }

    async getEpisodeSeasonInput() {
        return this.episodeSeasonInput.getAttribute('value');
    }

    async setThumbnailUrlInput(thumbnailUrl) {
        await this.thumbnailUrlInput.sendKeys(thumbnailUrl);
    }

    async getThumbnailUrlInput() {
        return this.thumbnailUrlInput.getAttribute('value');
    }

    async setYoutubeUrlInput(youtubeUrl) {
        await this.youtubeUrlInput.sendKeys(youtubeUrl);
    }

    async getYoutubeUrlInput() {
        return this.youtubeUrlInput.getAttribute('value');
    }

    async setCallToActionInput(callToAction) {
        await this.callToActionInput.sendKeys(callToAction);
    }

    async getCallToActionInput() {
        return this.callToActionInput.getAttribute('value');
    }

    async setCallToActionDueDateInput(callToActionDueDate) {
        await this.callToActionDueDateInput.sendKeys(callToActionDueDate);
    }

    async getCallToActionDueDateInput() {
        return this.callToActionDueDateInput.getAttribute('value');
    }

    async tagSelectLastOption() {
        await this.tagSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async tagSelectOption(option) {
        await this.tagSelect.sendKeys(option);
    }

    getTagSelect(): ElementFinder {
        return this.tagSelect;
    }

    async getTagSelectedOption() {
        return this.tagSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class EpisodeDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-episode-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-episode'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}

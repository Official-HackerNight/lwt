import { element, by, ElementFinder } from 'protractor';

export class ResourceComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-resource div table .btn-danger'));
    title = element.all(by.css('jhi-resource div h2#page-heading span')).first();

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

export class ResourceUpdatePage {
    pageTitle = element(by.id('jhi-resource-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nameInput = element(by.id('field_name'));
    urlInput = element(by.id('field_url'));
    descriptionInput = element(by.id('field_description'));
    episodeSelect = element(by.id('field_episode'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNameInput(name) {
        await this.nameInput.sendKeys(name);
    }

    async getNameInput() {
        return this.nameInput.getAttribute('value');
    }

    async setUrlInput(url) {
        await this.urlInput.sendKeys(url);
    }

    async getUrlInput() {
        return this.urlInput.getAttribute('value');
    }

    async setDescriptionInput(description) {
        await this.descriptionInput.sendKeys(description);
    }

    async getDescriptionInput() {
        return this.descriptionInput.getAttribute('value');
    }

    async episodeSelectLastOption() {
        await this.episodeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async episodeSelectOption(option) {
        await this.episodeSelect.sendKeys(option);
    }

    getEpisodeSelect(): ElementFinder {
        return this.episodeSelect;
    }

    async getEpisodeSelectedOption() {
        return this.episodeSelect.element(by.css('option:checked')).getText();
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

export class ResourceDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-resource-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-resource'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}

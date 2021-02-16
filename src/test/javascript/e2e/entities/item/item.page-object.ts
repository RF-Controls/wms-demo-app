import { element, by, ElementFinder } from 'protractor';

export class ItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-item div table .btn-danger'));
  title = element.all(by.css('jhi-item div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class ItemUpdatePage {
  pageTitle = element(by.id('jhi-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  epcInput = element(by.id('field_epc'));
  descriptionInput = element(by.id('field_description'));
  description2Input = element(by.id('field_description2'));
  weightInput = element(by.id('field_weight'));
  thumbnailInput = element(by.id('file_thumbnail'));
  detailImageInput = element(by.id('file_detailImage'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setEpcInput(epc: string): Promise<void> {
    await this.epcInput.sendKeys(epc);
  }

  async getEpcInput(): Promise<string> {
    return await this.epcInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setDescription2Input(description2: string): Promise<void> {
    await this.description2Input.sendKeys(description2);
  }

  async getDescription2Input(): Promise<string> {
    return await this.description2Input.getAttribute('value');
  }

  async setWeightInput(weight: string): Promise<void> {
    await this.weightInput.sendKeys(weight);
  }

  async getWeightInput(): Promise<string> {
    return await this.weightInput.getAttribute('value');
  }

  async setThumbnailInput(thumbnail: string): Promise<void> {
    await this.thumbnailInput.sendKeys(thumbnail);
  }

  async getThumbnailInput(): Promise<string> {
    return await this.thumbnailInput.getAttribute('value');
  }

  async setDetailImageInput(detailImage: string): Promise<void> {
    await this.detailImageInput.sendKeys(detailImage);
  }

  async getDetailImageInput(): Promise<string> {
    return await this.detailImageInput.getAttribute('value');
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class ItemDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-item-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-item'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

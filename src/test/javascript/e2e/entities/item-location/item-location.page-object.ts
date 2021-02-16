import { element, by, ElementFinder } from 'protractor';

export class ItemLocationComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-item-location div table .btn-danger'));
  title = element.all(by.css('jhi-item-location div h2#page-heading span')).first();
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

export class ItemLocationUpdatePage {
  pageTitle = element(by.id('jhi-item-location-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  lastLocationUpdateInput = element(by.id('field_lastLocationUpdate'));
  zoneEnterInstantInput = element(by.id('field_zoneEnterInstant'));
  descriptionInput = element(by.id('field_description'));
  xInput = element(by.id('field_x'));
  yInput = element(by.id('field_y'));
  zInput = element(by.id('field_z'));

  itemSelect = element(by.id('field_item'));
  zoneSelect = element(by.id('field_zone'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setLastLocationUpdateInput(lastLocationUpdate: string): Promise<void> {
    await this.lastLocationUpdateInput.sendKeys(lastLocationUpdate);
  }

  async getLastLocationUpdateInput(): Promise<string> {
    return await this.lastLocationUpdateInput.getAttribute('value');
  }

  async setZoneEnterInstantInput(zoneEnterInstant: string): Promise<void> {
    await this.zoneEnterInstantInput.sendKeys(zoneEnterInstant);
  }

  async getZoneEnterInstantInput(): Promise<string> {
    return await this.zoneEnterInstantInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setXInput(x: string): Promise<void> {
    await this.xInput.sendKeys(x);
  }

  async getXInput(): Promise<string> {
    return await this.xInput.getAttribute('value');
  }

  async setYInput(y: string): Promise<void> {
    await this.yInput.sendKeys(y);
  }

  async getYInput(): Promise<string> {
    return await this.yInput.getAttribute('value');
  }

  async setZInput(z: string): Promise<void> {
    await this.zInput.sendKeys(z);
  }

  async getZInput(): Promise<string> {
    return await this.zInput.getAttribute('value');
  }

  async itemSelectLastOption(): Promise<void> {
    await this.itemSelect.all(by.tagName('option')).last().click();
  }

  async itemSelectOption(option: string): Promise<void> {
    await this.itemSelect.sendKeys(option);
  }

  getItemSelect(): ElementFinder {
    return this.itemSelect;
  }

  async getItemSelectedOption(): Promise<string> {
    return await this.itemSelect.element(by.css('option:checked')).getText();
  }

  async zoneSelectLastOption(): Promise<void> {
    await this.zoneSelect.all(by.tagName('option')).last().click();
  }

  async zoneSelectOption(option: string): Promise<void> {
    await this.zoneSelect.sendKeys(option);
  }

  getZoneSelect(): ElementFinder {
    return this.zoneSelect;
  }

  async getZoneSelectedOption(): Promise<string> {
    return await this.zoneSelect.element(by.css('option:checked')).getText();
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

export class ItemLocationDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-itemLocation-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-itemLocation'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

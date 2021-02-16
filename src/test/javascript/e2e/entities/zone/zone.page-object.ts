import { element, by, ElementFinder } from 'protractor';

export class ZoneComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-zone div table .btn-danger'));
  title = element.all(by.css('jhi-zone div h2#page-heading span')).first();
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

export class ZoneUpdatePage {
  pageTitle = element(by.id('jhi-zone-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  nameInput = element(by.id('field_name'));
  descriptionInput = element(by.id('field_description'));
  maxItemsInput = element(by.id('field_maxItems'));
  x1Input = element(by.id('field_x1'));
  y1Input = element(by.id('field_y1'));
  z1Input = element(by.id('field_z1'));
  x2Input = element(by.id('field_x2'));
  y2Input = element(by.id('field_y2'));
  z2Input = element(by.id('field_z2'));
  associationZoneInput = element(by.id('field_associationZone'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setNameInput(name: string): Promise<void> {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput(): Promise<string> {
    return await this.nameInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async setMaxItemsInput(maxItems: string): Promise<void> {
    await this.maxItemsInput.sendKeys(maxItems);
  }

  async getMaxItemsInput(): Promise<string> {
    return await this.maxItemsInput.getAttribute('value');
  }

  async setX1Input(x1: string): Promise<void> {
    await this.x1Input.sendKeys(x1);
  }

  async getX1Input(): Promise<string> {
    return await this.x1Input.getAttribute('value');
  }

  async setY1Input(y1: string): Promise<void> {
    await this.y1Input.sendKeys(y1);
  }

  async getY1Input(): Promise<string> {
    return await this.y1Input.getAttribute('value');
  }

  async setZ1Input(z1: string): Promise<void> {
    await this.z1Input.sendKeys(z1);
  }

  async getZ1Input(): Promise<string> {
    return await this.z1Input.getAttribute('value');
  }

  async setX2Input(x2: string): Promise<void> {
    await this.x2Input.sendKeys(x2);
  }

  async getX2Input(): Promise<string> {
    return await this.x2Input.getAttribute('value');
  }

  async setY2Input(y2: string): Promise<void> {
    await this.y2Input.sendKeys(y2);
  }

  async getY2Input(): Promise<string> {
    return await this.y2Input.getAttribute('value');
  }

  async setZ2Input(z2: string): Promise<void> {
    await this.z2Input.sendKeys(z2);
  }

  async getZ2Input(): Promise<string> {
    return await this.z2Input.getAttribute('value');
  }

  getAssociationZoneInput(): ElementFinder {
    return this.associationZoneInput;
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

export class ZoneDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-zone-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-zone'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}

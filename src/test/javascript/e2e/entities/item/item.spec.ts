import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ItemComponentsPage, ItemDeleteDialog, ItemUpdatePage } from './item.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Item e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let itemComponentsPage: ItemComponentsPage;
  let itemUpdatePage: ItemUpdatePage;
  let itemDeleteDialog: ItemDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Items', async () => {
    await navBarPage.goToEntity('item');
    itemComponentsPage = new ItemComponentsPage();
    await browser.wait(ec.visibilityOf(itemComponentsPage.title), 5000);
    expect(await itemComponentsPage.getTitle()).to.eq('wmsdemoApp.item.home.title');
    await browser.wait(ec.or(ec.visibilityOf(itemComponentsPage.entities), ec.visibilityOf(itemComponentsPage.noResult)), 1000);
  });

  it('should load create Item page', async () => {
    await itemComponentsPage.clickOnCreateButton();
    itemUpdatePage = new ItemUpdatePage();
    expect(await itemUpdatePage.getPageTitle()).to.eq('wmsdemoApp.item.home.createOrEditLabel');
    await itemUpdatePage.cancel();
  });

  it('should create and save Items', async () => {
    const nbButtonsBeforeCreate = await itemComponentsPage.countDeleteButtons();

    await itemComponentsPage.clickOnCreateButton();

    await promise.all([
      itemUpdatePage.setNameInput('name'),
      itemUpdatePage.setEpcInput('epc'),
      itemUpdatePage.setDescriptionInput('description'),
      itemUpdatePage.setDescription2Input('description2'),
      itemUpdatePage.setWeightInput('5'),
      itemUpdatePage.setThumbnailInput(absolutePath),
      itemUpdatePage.setDetailImageInput(absolutePath),
    ]);

    expect(await itemUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await itemUpdatePage.getEpcInput()).to.eq('epc', 'Expected Epc value to be equals to epc');
    expect(await itemUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await itemUpdatePage.getDescription2Input()).to.eq('description2', 'Expected Description2 value to be equals to description2');
    expect(await itemUpdatePage.getWeightInput()).to.eq('5', 'Expected weight value to be equals to 5');
    expect(await itemUpdatePage.getThumbnailInput()).to.endsWith(
      fileNameToUpload,
      'Expected Thumbnail value to be end with ' + fileNameToUpload
    );
    expect(await itemUpdatePage.getDetailImageInput()).to.endsWith(
      fileNameToUpload,
      'Expected DetailImage value to be end with ' + fileNameToUpload
    );

    await itemUpdatePage.save();
    expect(await itemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await itemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Item', async () => {
    const nbButtonsBeforeDelete = await itemComponentsPage.countDeleteButtons();
    await itemComponentsPage.clickOnLastDeleteButton();

    itemDeleteDialog = new ItemDeleteDialog();
    expect(await itemDeleteDialog.getDialogTitle()).to.eq('wmsdemoApp.item.delete.question');
    await itemDeleteDialog.clickOnConfirmButton();

    expect(await itemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ItemLocationComponentsPage, ItemLocationDeleteDialog, ItemLocationUpdatePage } from './item-location.page-object';

const expect = chai.expect;

describe('ItemLocation e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let itemLocationComponentsPage: ItemLocationComponentsPage;
  let itemLocationUpdatePage: ItemLocationUpdatePage;
  let itemLocationDeleteDialog: ItemLocationDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load ItemLocations', async () => {
    await navBarPage.goToEntity('item-location');
    itemLocationComponentsPage = new ItemLocationComponentsPage();
    await browser.wait(ec.visibilityOf(itemLocationComponentsPage.title), 5000);
    expect(await itemLocationComponentsPage.getTitle()).to.eq('wmsdemoApp.itemLocation.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(itemLocationComponentsPage.entities), ec.visibilityOf(itemLocationComponentsPage.noResult)),
      1000
    );
  });

  it('should load create ItemLocation page', async () => {
    await itemLocationComponentsPage.clickOnCreateButton();
    itemLocationUpdatePage = new ItemLocationUpdatePage();
    expect(await itemLocationUpdatePage.getPageTitle()).to.eq('wmsdemoApp.itemLocation.home.createOrEditLabel');
    await itemLocationUpdatePage.cancel();
  });

  it('should create and save ItemLocations', async () => {
    const nbButtonsBeforeCreate = await itemLocationComponentsPage.countDeleteButtons();

    await itemLocationComponentsPage.clickOnCreateButton();

    await promise.all([
      itemLocationUpdatePage.setLastLocationUpdateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      itemLocationUpdatePage.setZoneEnterInstantInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      itemLocationUpdatePage.setDescriptionInput('description'),
      itemLocationUpdatePage.setXInput('5'),
      itemLocationUpdatePage.setYInput('5'),
      itemLocationUpdatePage.setZInput('5'),
      itemLocationUpdatePage.itemSelectLastOption(),
      itemLocationUpdatePage.zoneSelectLastOption(),
    ]);

    expect(await itemLocationUpdatePage.getLastLocationUpdateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected lastLocationUpdate value to be equals to 2000-12-31'
    );
    expect(await itemLocationUpdatePage.getZoneEnterInstantInput()).to.contain(
      '2001-01-01T02:30',
      'Expected zoneEnterInstant value to be equals to 2000-12-31'
    );
    expect(await itemLocationUpdatePage.getDescriptionInput()).to.eq(
      'description',
      'Expected Description value to be equals to description'
    );
    expect(await itemLocationUpdatePage.getXInput()).to.eq('5', 'Expected x value to be equals to 5');
    expect(await itemLocationUpdatePage.getYInput()).to.eq('5', 'Expected y value to be equals to 5');
    expect(await itemLocationUpdatePage.getZInput()).to.eq('5', 'Expected z value to be equals to 5');

    await itemLocationUpdatePage.save();
    expect(await itemLocationUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await itemLocationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last ItemLocation', async () => {
    const nbButtonsBeforeDelete = await itemLocationComponentsPage.countDeleteButtons();
    await itemLocationComponentsPage.clickOnLastDeleteButton();

    itemLocationDeleteDialog = new ItemLocationDeleteDialog();
    expect(await itemLocationDeleteDialog.getDialogTitle()).to.eq('wmsdemoApp.itemLocation.delete.question');
    await itemLocationDeleteDialog.clickOnConfirmButton();

    expect(await itemLocationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

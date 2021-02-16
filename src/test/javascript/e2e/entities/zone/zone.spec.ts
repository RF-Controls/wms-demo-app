import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ZoneComponentsPage, ZoneDeleteDialog, ZoneUpdatePage } from './zone.page-object';

const expect = chai.expect;

describe('Zone e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let zoneComponentsPage: ZoneComponentsPage;
  let zoneUpdatePage: ZoneUpdatePage;
  let zoneDeleteDialog: ZoneDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Zones', async () => {
    await navBarPage.goToEntity('zone');
    zoneComponentsPage = new ZoneComponentsPage();
    await browser.wait(ec.visibilityOf(zoneComponentsPage.title), 5000);
    expect(await zoneComponentsPage.getTitle()).to.eq('wmsdemoApp.zone.home.title');
    await browser.wait(ec.or(ec.visibilityOf(zoneComponentsPage.entities), ec.visibilityOf(zoneComponentsPage.noResult)), 1000);
  });

  it('should load create Zone page', async () => {
    await zoneComponentsPage.clickOnCreateButton();
    zoneUpdatePage = new ZoneUpdatePage();
    expect(await zoneUpdatePage.getPageTitle()).to.eq('wmsdemoApp.zone.home.createOrEditLabel');
    await zoneUpdatePage.cancel();
  });

  it('should create and save Zones', async () => {
    const nbButtonsBeforeCreate = await zoneComponentsPage.countDeleteButtons();

    await zoneComponentsPage.clickOnCreateButton();

    await promise.all([
      zoneUpdatePage.setNameInput('name'),
      zoneUpdatePage.setDescriptionInput('description'),
      zoneUpdatePage.setMaxItemsInput('5'),
      zoneUpdatePage.setX1Input('5'),
      zoneUpdatePage.setY1Input('5'),
      zoneUpdatePage.setZ1Input('5'),
      zoneUpdatePage.setX2Input('5'),
      zoneUpdatePage.setY2Input('5'),
      zoneUpdatePage.setZ2Input('5'),
    ]);

    expect(await zoneUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await zoneUpdatePage.getDescriptionInput()).to.eq('description', 'Expected Description value to be equals to description');
    expect(await zoneUpdatePage.getMaxItemsInput()).to.eq('5', 'Expected maxItems value to be equals to 5');
    expect(await zoneUpdatePage.getX1Input()).to.eq('5', 'Expected x1 value to be equals to 5');
    expect(await zoneUpdatePage.getY1Input()).to.eq('5', 'Expected y1 value to be equals to 5');
    expect(await zoneUpdatePage.getZ1Input()).to.eq('5', 'Expected z1 value to be equals to 5');
    expect(await zoneUpdatePage.getX2Input()).to.eq('5', 'Expected x2 value to be equals to 5');
    expect(await zoneUpdatePage.getY2Input()).to.eq('5', 'Expected y2 value to be equals to 5');
    expect(await zoneUpdatePage.getZ2Input()).to.eq('5', 'Expected z2 value to be equals to 5');
    const selectedAssociationZone = zoneUpdatePage.getAssociationZoneInput();
    if (await selectedAssociationZone.isSelected()) {
      await zoneUpdatePage.getAssociationZoneInput().click();
      expect(await zoneUpdatePage.getAssociationZoneInput().isSelected(), 'Expected associationZone not to be selected').to.be.false;
    } else {
      await zoneUpdatePage.getAssociationZoneInput().click();
      expect(await zoneUpdatePage.getAssociationZoneInput().isSelected(), 'Expected associationZone to be selected').to.be.true;
    }

    await zoneUpdatePage.save();
    expect(await zoneUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await zoneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Zone', async () => {
    const nbButtonsBeforeDelete = await zoneComponentsPage.countDeleteButtons();
    await zoneComponentsPage.clickOnLastDeleteButton();

    zoneDeleteDialog = new ZoneDeleteDialog();
    expect(await zoneDeleteDialog.getDialogTitle()).to.eq('wmsdemoApp.zone.delete.question');
    await zoneDeleteDialog.clickOnConfirmButton();

    expect(await zoneComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});

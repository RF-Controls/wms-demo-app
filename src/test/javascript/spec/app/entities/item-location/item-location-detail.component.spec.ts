import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WmsdemoTestModule } from '../../../test.module';
import { ItemLocationDetailComponent } from 'app/entities/item-location/item-location-detail.component';
import { ItemLocation } from 'app/shared/model/item-location.model';

describe('Component Tests', () => {
  describe('ItemLocation Management Detail Component', () => {
    let comp: ItemLocationDetailComponent;
    let fixture: ComponentFixture<ItemLocationDetailComponent>;
    const route = ({ data: of({ itemLocation: new ItemLocation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WmsdemoTestModule],
        declarations: [ItemLocationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ItemLocationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemLocationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemLocation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemLocation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

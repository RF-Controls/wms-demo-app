import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { WmsdemoTestModule } from '../../../test.module';
import { ItemLocationUpdateComponent } from 'app/entities/item-location/item-location-update.component';
import { ItemLocationService } from 'app/entities/item-location/item-location.service';
import { ItemLocation } from 'app/shared/model/item-location.model';

describe('Component Tests', () => {
  describe('ItemLocation Management Update Component', () => {
    let comp: ItemLocationUpdateComponent;
    let fixture: ComponentFixture<ItemLocationUpdateComponent>;
    let service: ItemLocationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [WmsdemoTestModule],
        declarations: [ItemLocationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ItemLocationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemLocationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ItemLocationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ItemLocation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new ItemLocation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});

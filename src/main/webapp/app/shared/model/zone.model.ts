import { IItemLocation } from 'app/shared/model/item-location.model';

export interface IZone {
  id?: number;
  name?: string;
  description?: string;
  maxItems?: number;
  x1?: number;
  y1?: number;
  z1?: number;
  x2?: number;
  y2?: number;
  z2?: number;
  associationZone?: boolean;
  epcs?: IItemLocation[];
}

export class Zone implements IZone {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public maxItems?: number,
    public x1?: number,
    public y1?: number,
    public z1?: number,
    public x2?: number,
    public y2?: number,
    public z2?: number,
    public associationZone?: boolean,
    public epcs?: IItemLocation[]
  ) {
    this.associationZone = this.associationZone || false;
  }
}

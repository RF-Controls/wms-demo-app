import { Moment } from 'moment';

export interface IItemLocation {
  id?: number;
  lastLocationUpdate?: Moment;
  zoneEnterInstant?: Moment;
  description?: string;
  x?: number;
  y?: number;
  z?: number;
  itemId?: number;
  itemName?: string;
  zoneId?: number;
  zoneName?: string;
  epc?: string;
  detailImageContentType?: string;
  detailImage?: any;
}

export class ItemLocation implements IItemLocation {
  constructor(
    public id?: number,
    public lastLocationUpdate?: Moment,
    public zoneEnterInstant?: Moment,
    public description?: string,
    public x?: number,
    public y?: number,
    public z?: number,
    public itemId?: number,
    public zoneId?: number
  ) {}
}

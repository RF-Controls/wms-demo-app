export interface IItem {
  id?: number;
  name?: string;
  epc?: string;
  description?: string;
  description2?: string;
  weight?: number;
  thumbnailContentType?: string;
  thumbnail?: any;
  detailImageContentType?: string;
  detailImage?: any;
}

export class Item implements IItem {
  constructor(
    public id?: number,
    public name?: string,
    public epc?: string,
    public description?: string,
    public description2?: string,
    public weight?: number,
    public thumbnailContentType?: string,
    public thumbnail?: any,
    public detailImageContentType?: string,
    public detailImage?: any
  ) {}
}

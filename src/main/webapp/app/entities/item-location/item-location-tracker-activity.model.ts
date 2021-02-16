import { Moment } from 'moment';

export class ItemLocationTrackerActivityModel {
  constructor(
    public epc: string,
    public x: number,
    public y: number,
    public z: number,
    public zoneName: string,
    public updateTime: Moment
  ) {}
}

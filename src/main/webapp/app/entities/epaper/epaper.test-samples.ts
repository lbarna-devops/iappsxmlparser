import { IEpaper, NewEpaper } from './epaper.model';

export const sampleWithRequiredData: IEpaper = {
  id: 51340,
};

export const sampleWithPartialData: IEpaper = {
  id: 39505,
  newspaperName: 'neural eyeballs',
  dpi: 96037,
  uploadtime: 'Product',
  filename: 'Product turquoise',
};

export const sampleWithFullData: IEpaper = {
  id: 93721,
  newspaperName: 'Chief',
  width: 8837,
  height: 44506,
  dpi: 24170,
  uploadtime: 'Nebraska Automotive didactic',
  filename: 'EXE navigating',
};

export const sampleWithNewData: NewEpaper = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

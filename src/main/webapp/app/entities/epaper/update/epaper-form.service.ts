import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEpaper, NewEpaper } from '../epaper.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEpaper for edit and NewEpaperFormGroupInput for create.
 */
type EpaperFormGroupInput = IEpaper | PartialWithRequiredKeyOf<NewEpaper>;

type EpaperFormDefaults = Pick<NewEpaper, 'id'>;

type EpaperFormGroupContent = {
  id: FormControl<IEpaper['id'] | NewEpaper['id']>;
  newspaperName: FormControl<IEpaper['newspaperName']>;
  width: FormControl<IEpaper['width']>;
  height: FormControl<IEpaper['height']>;
  dpi: FormControl<IEpaper['dpi']>;
  uploadtime: FormControl<IEpaper['uploadtime']>;
  filename: FormControl<IEpaper['filename']>;
};

export type EpaperFormGroup = FormGroup<EpaperFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EpaperFormService {
  createEpaperFormGroup(epaper: EpaperFormGroupInput = { id: null }): EpaperFormGroup {
    const epaperRawValue = {
      ...this.getFormDefaults(),
      ...epaper,
    };
    return new FormGroup<EpaperFormGroupContent>({
      id: new FormControl(
        { value: epaperRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      newspaperName: new FormControl(epaperRawValue.newspaperName),
      width: new FormControl(epaperRawValue.width),
      height: new FormControl(epaperRawValue.height),
      dpi: new FormControl(epaperRawValue.dpi),
      uploadtime: new FormControl(epaperRawValue.uploadtime),
      filename: new FormControl(epaperRawValue.filename),
    });
  }

  getEpaper(form: EpaperFormGroup): IEpaper | NewEpaper {
    return form.getRawValue() as IEpaper | NewEpaper;
  }

  resetForm(form: EpaperFormGroup, epaper: EpaperFormGroupInput): void {
    const epaperRawValue = { ...this.getFormDefaults(), ...epaper };
    form.reset(
      {
        ...epaperRawValue,
        id: { value: epaperRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EpaperFormDefaults {
    return {
      id: null,
    };
  }
}

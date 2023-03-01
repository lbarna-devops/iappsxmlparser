export interface IEpaper {
  id: number;
  newspaperName?: string | null;
  width?: number | null;
  height?: number | null;
  dpi?: number | null;
  uploadtime?: string | null;
  filename?: string | null;
}

export type NewEpaper = Omit<IEpaper, 'id'> & { id: null };

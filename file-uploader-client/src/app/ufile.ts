import { UploadState } from '../uploadx';

export class Ufile {
  name: string;
  uploadId: string;
  hash: string;
  progress: number;
  status: string;
  pause: any;
  constructor(ufile: UploadState,hash: string) {
    this.uploadId = ufile.uploadId;
    this.name = ufile.name;
    this.progress = ufile.progress;
    this.status = ufile.status;
    this.hash = hash;
    this.pause = false;
  }
}

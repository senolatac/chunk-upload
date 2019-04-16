import { Component,  ElementRef, OnDestroy, OnInit, ViewChild, ChangeDetectorRef } from '@angular/core';
import { AuthService } from '../auth.service';
import {FormControl, FormGroup} from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import {MatPaginator, MatTableDataSource, MatSort} from '@angular/material';
import { UploadxOptions, UploadState, UploadxService, UploadItem } from '../../uploadx';
import { Observable, Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { Ufile } from '../ufile';
import {ParallelHasher} from 'ts-md5/dist/parallel_hasher';

const URL = 'http://192.168.0.11:8080/api/uploadFile';
const displayURL = 'http://192.168.0.11:8080/api/displayFile/';

@Component({
  selector: 'app-filer',
  templateUrl: './filer.component.html',
  styleUrls: ['./filer.component.css']
})
export class FilerComponent implements OnDestroy, OnInit {

  @ViewChild('file', { read: ElementRef }) fileInput: ElementRef;

  uploads: Ufile[] = [];
  completedList: Array<any>;
  displayPath = displayURL;
  paused = false;
  options: UploadxOptions = {
    concurrency: 2,
    allowedTypes: 'video/*',
    endpoint: URL,
    url: URL,
    chunkSize: 1024 * 1024 * 1,
    autoUpload: false
  };
  displayedColumns: string[] = ['encryptScreen','encryptPath', 'name', 'duration'];
  hasher = new ParallelHasher('../../assets/ts-md5/md5_worker.js');
  private ngUnsubscribe: Subject<any> = new Subject();
  state: Observable<UploadState>;
  itemState: UploadState;

  constructor(private authService: AuthService,  private uploadService: UploadxService, private changeDetectorRefs: ChangeDetectorRef) { }

  ngOnInit() {
      const uploadsProgress = this.uploadService.init(this.options);
      this.onUpload(uploadsProgress);
      this.completedList = [];
    }

    getFiles(): FileList {
      return this.fileInput.nativeElement.files;
    }

    ngOnDestroy() {
      this.ngUnsubscribe.next();
      this.ngUnsubscribe.complete();
    }

    cancelAll() {
      this.uploadService.control({ action: 'cancelAll' });
    }

    uploadAll() {
      this.uploadService.control({ action: 'uploadAll' });
    }

    pauseAll() {
      this.uploadService.control({ action: 'pauseAll' });
    }

    pause(ufile: Ufile) {
      var uploadId = ufile.uploadId;
      if(ufile.pause){
        this.upload(ufile.uploadId, ufile.hash, null);
      }else{
        this.uploadService.control({ action: 'pause', uploadId});
      }
      const index = this.uploads.findIndex(f => f.uploadId === ufile.uploadId);
      this.uploads[index].pause = !ufile.pause;
    }

    upload(id: string, t: string, uri: string) {
      this.uploadService.control({
              action: 'upload',
              itemOptions: { uploadId: id, token: t, metadata: {uploadId:id, hash: t, URI: uri} }
            });
    }

    onUpload(uploadsOutStream: Observable<UploadState>) {
      this.state = uploadsOutStream;
      uploadsOutStream.pipe(takeUntil(this.ngUnsubscribe)).subscribe((item: UploadState) => {
        this.itemState = item;
        const index = this.uploads.findIndex(f => f.uploadId === item.uploadId);
        if (item.status === 'added') {
          this.hasher.hash(item.file).then((result)=> {
              this.uploads.push(new Ufile(item, result));
              this.upload(item.uploadId, result, null);
          });

        } else {
          this.uploads[index].progress = item.progress;
          this.uploads[index].status = item.status;
        }
        if(item.status === 'complete'){
          this.uploads[index].progress = 100;
          this.hasher.hash(item.file).then((result)=> {
            var fileInfo = {name: item.URI, size: item.size, currentChunk: 0, contentType: item.file.type, hash: result};
            this.authService.notifyCompleted(fileInfo).subscribe((data) => {
                this.completedList.push(data);
                this.changeDetectorRefs.detectChanges();
            }, (err) => {
              console.log(err);
            });
          });
        }
        if (item.status === 'error') {
          if (item && item.responseStatus === 401) {
            // this.uploadService.control({ action: 'refreshToken', token: tokenGetter() });
            this.uploadService.control({
              action: 'upload',
              itemOptions: { uploadId: item.uploadId }
            });
          }
        }
      });
    }

}

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

const displayURL = 'http://localhost:8080/api/displayFile/';

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
  hasher = new ParallelHasher('../../assets/ts-md5/md5_worker.js');
  private ngUnsubscribe: Subject<any> = new Subject();
  state: Observable<UploadState>;
  itemState: UploadState;

  constructor(private authService: AuthService, private changeDetectorRefs: ChangeDetectorRef) { }

  ngOnInit() {
      this.completedList = [];
    }

    ngOnDestroy() {
    }

}

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FilerComponent } from './filer/filer.component';
import { HttpClientModule } from '@angular/common/http';

import { MatButtonModule, MatCardModule, MatInputModule, MatListModule, MatToolbarModule, MatSelectModule,
MatFormFieldModule, MatTableModule, MatPaginatorModule, MatSortModule, MatProgressBarModule, MatIconModule  } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule, ReactiveFormsModule  } from "@angular/forms";
import { UploadxModule } from '../uploadx';

@NgModule({
  declarations: [
    AppComponent,
    FilerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule,
    MatInputModule,
    MatListModule,
    MatToolbarModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatPaginatorModule,
    MatTableModule,
    MatSortModule,
    MatProgressBarModule,
    MatIconModule,
    UploadxModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

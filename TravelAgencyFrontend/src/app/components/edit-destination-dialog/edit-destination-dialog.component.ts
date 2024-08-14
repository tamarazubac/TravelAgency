import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from 'src/app/common/material/material.module';
import { CommonModule } from '@angular/common';
import { Destination } from 'src/app/models/destination';
import { DestinationService } from 'src/app/services/destination/destination.service';

@Component({
  selector: 'app-edit-destination-dialog',
  templateUrl: './edit-destination-dialog.component.html',
  styleUrls: ['./edit-destination-dialog.component.css'],
  standalone: true,
  imports: [MaterialModule, CommonModule, ReactiveFormsModule]
})
export class EditDestinationDialogComponent {
  destinationForm: FormGroup;
  destination: Destination;

  selectedFile: File | null = null;
  imageList: { file: File, url: string }[] = [];
  existingImages: string[] = [];

  constructor(
    private destinationService: DestinationService,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<EditDestinationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { destination: Destination }
  ) {
    this.destination = data.destination;
    this.destinationForm = this.fb.group({
      country: [{ value: data.destination.country_name, disabled: true }],
      city: [{ value: data.destination.city_name, disabled: true }]
    });
    this.loadImages();
  }

  loadImages(): void {
    if (this.destination && this.destination.id) {
      this.destinationService.getImages(this.destination.id).subscribe({
        next: (data: string[]) => {
          this.existingImages = data;
        },
        error: (err) => {
          console.error('Error loading images:', err);
        }
      });
    }
  }

  onFileChange(event: any): void {
    if (event.target.files && event.target.files.length) {
      this.selectedFile = event.target.files[0];
    }
  }

  addImageToList(): void {
    if (this.selectedFile) {
      const reader = new FileReader();
      reader.onload = () => {
        if (this.selectedFile) {
          this.imageList.push({ file: this.selectedFile, url: reader.result as string });
        }
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  removeImage(image: string): void {
    this.existingImages = this.existingImages.filter(img => img !== image);
    const filename = image.split('/').pop() || '';

    this.destinationService.deleteImage(this.data.destination.id, filename).subscribe({
      next: () => {
        console.log('Image successfully deleted');
      },
      error: (err) => {
        console.error('Error deleting image:', err);
      }
    });
  }

  removeNewImage(image: { file: File, url: string }): void {
    this.imageList = this.imageList.filter(img => img !== image);
  }

  saveDestination(): void {

    this.uploadImages(this.data.destination.id);

  }

  uploadImages(destinationId: number|undefined): void {
    if(destinationId)
    this.imageList.forEach((image) => {
      this.destinationService.uploadImage(destinationId, image.file).subscribe({
        next: () => {
          console.log(`Image uploaded successfully for destination ${destinationId}`);
        },
        error: (err) => {
          console.error('Failed to upload image', err);
        }
      });
    });
  }

  closeDialog(): void {
    this.dialogRef.close();
  }
}

import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ReportService } from 'src/app/services/reports/report.service';
import { CommonModule } from '@angular/common';
import { MaterialModule } from 'src/app/common/material/material.module';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-date-range-dialog',
  templateUrl: './date-range-dialog.component.html',
  styleUrls: ['./date-range-dialog.component.css'],
  standalone:true,
  imports:[MaterialModule,CommonModule,FormsModule]
})
export class DateRangeDialogComponent {

  startDate: Date | null = null;
  endDate: Date | null = null;

  constructor(
    public dialogRef: MatDialogRef<DateRangeDialogComponent>,
    private reportService: ReportService,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

  generateBalanceReport(): void {
    if (!this.startDate || !this.endDate) {
      console.error('Start date and end date are required');
      return;
    }

    this.reportService.downloadBalanceReport(this.startDate, this.endDate).subscribe((response: Blob) => {
      const url = window.URL.createObjectURL(response);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'BalanceReport.pdf';
      link.click();
      window.URL.revokeObjectURL(url);
    }, error => {
      console.error('Error downloading the report', error);
    });
  }

}

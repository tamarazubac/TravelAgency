package com.example.TravelAgency.rest;

import com.example.TravelAgency.services.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Date;

@RestController
@RequestMapping("/reports")
public class RestReportController {

    private static final Logger logger = LoggerFactory.getLogger(RestReportController.class);

    @Autowired
    private ReportService reportService;

    @GetMapping("/user")
    public ResponseEntity<InputStreamResource> getUserReport() {
        try {
            byte[] reportBytes = reportService.generateUserReport();

            if (reportBytes == null || reportBytes.length == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(reportBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=userReport.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));

        } catch (Exception e) {
            logger.error("Error generating user report", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/report1")
    public ResponseEntity<InputStreamResource> getFirstReport() {
        try {
            byte[] reportBytes = reportService.generateReport1();

            if (reportBytes == null || reportBytes.length == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(reportBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=userReport.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));

        } catch (Exception e) {
            logger.error("Error generating user report", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/report2")
    public ResponseEntity<InputStreamResource> getSecondReport() {
        try {
            byte[] reportBytes = reportService.generateReport2();

            if (reportBytes == null || reportBytes.length == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(reportBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=userReport.pdf");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));

        } catch (Exception e) {
            logger.error("Error generating user report", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/generateCombinedReport")
    public ResponseEntity<byte[]> generateCombinedReport() {
        try {
            byte[] pdfBytes = reportService.generateCombinedReport();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"combined_report.pdf\"")
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/generateBalanceReport")
    public ResponseEntity<InputStreamResource> generateDateRangeReport(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        try {
            byte[] pdfBytes = reportService.generateDateRangeReport(startDate, endDate);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=DateRangeReport.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(new ByteArrayInputStream(pdfBytes)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

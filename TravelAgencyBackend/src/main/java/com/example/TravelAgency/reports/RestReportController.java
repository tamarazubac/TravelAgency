package com.example.TravelAgency.reports;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
}

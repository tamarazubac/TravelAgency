package com.example.TravelAgency.reports;

import com.example.TravelAgency.models.User;
import com.example.TravelAgency.repositories.IUserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private IUserRepository userRepository;

    public byte[] generateUserReport() throws JRException, IOException {
        // Fetch data
        List<User> users = userRepository.findAll();

        // Load Jasper report template
        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("test.jrxml").getInputStream());

        // Prepare data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);

        // Parameters (if any)
        Map<String, Object> parameters = new HashMap<>(); // Use HashMap

        // Generate report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Export report to PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}

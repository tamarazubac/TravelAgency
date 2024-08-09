package com.example.TravelAgency.services;

import com.example.TravelAgency.dtos.DestinationEarnedDTO;
import com.example.TravelAgency.dtos.DestinationVisitedDTO;
import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.models.User;
import com.example.TravelAgency.repositories.IDestinationRepository;
import com.example.TravelAgency.repositories.IReservationRepository;
import com.example.TravelAgency.repositories.IUserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IDestinationRepository destinationRepository;
    @Autowired
    private IReservationRepository reservationRepository;

    public byte[] generateUserReport() throws JRException, IOException {

        List<User> users = userRepository.findAll();  //for data source

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("test.jrxml").getInputStream());  //jasper report template

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(users);

        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource); //generate report

        return JasperExportManager.exportReportToPdf(jasperPrint); //exporting report to pdf
    }

    public byte[] generateReport1() throws JRException, IOException {

        List<Destination> destinations = destinationRepository.findAll();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "All Destinations Sorted by People Visited");

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("destination_report1.jrxml").getInputStream());

        List<DestinationVisitedDTO> reportData = destinations.stream()
                .map(dest -> {
                    Integer totalPeople = reservationRepository.countNumberOfPeopleByDestination(dest.getId());
                    int totalPeopleValue = (totalPeople != null) ? totalPeople : 0;
                    return new DestinationVisitedDTO(dest.getCityName(), dest.getCountryName(), totalPeopleValue);
                })
                .sorted(Comparator.comparingInt(DestinationVisitedDTO::getTotalPeople).reversed())
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    public byte[] generateReport2() throws JRException, IOException {

        List<Destination> destinations = destinationRepository.findAll();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "All Destinations Sorted by Money earned");


        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("destination_report2.jrxml").getInputStream());

        List<DestinationEarnedDTO> reportData = destinations.stream()
                .map(dest -> {
                    Double totalRevenue = reservationRepository.calculateTotalEarnedByDestination(dest.getId());
                    return new DestinationEarnedDTO(dest.getCityName(), dest.getCountryName(), totalRevenue != null ? totalRevenue : 0.0);
                })
                .sorted(Comparator.comparingDouble(DestinationEarnedDTO::getEarnedMoney).reversed())
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }






}

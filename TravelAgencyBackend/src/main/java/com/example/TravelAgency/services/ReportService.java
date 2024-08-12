package com.example.TravelAgency.services;

import com.example.TravelAgency.dtos.BalanceReportDTO;
import com.example.TravelAgency.dtos.DestinationEarnedDTO;
import com.example.TravelAgency.dtos.DestinationReportDTO;
import com.example.TravelAgency.dtos.DestinationVisitedDTO;
import com.example.TravelAgency.models.Destination;
import com.example.TravelAgency.models.Reservation;
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
import java.util.*;
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
        parameters.put("REPORT_TITLE", "All Destinations Sorted by Money Earned");


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


    public byte[] generateCombinedReport() throws JRException, IOException {

        List<Destination> destinations = destinationRepository.findAll();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "Time For Travel : All Destinations Report");

        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("destination_combined_report.jrxml").getInputStream());

        List<DestinationReportDTO> reportData = destinations.stream()
                .map(dest -> {
                    Integer totalPeople = reservationRepository.countNumberOfPeopleByDestination(dest.getId());
                    Integer totalReservations = reservationRepository.countReservationsByDestination(dest.getId());
                    Double totalRevenue = reservationRepository.calculateTotalEarnedByDestination(dest.getId());
                    return new DestinationReportDTO(
                            dest.getCityName(),
                            dest.getCountryName(),
                            totalPeople != null ? totalPeople : 0,
                            totalReservations != null ? totalReservations : 0,
                            totalRevenue != null ? totalRevenue : 0.0
                    );
                })
                .sorted(Comparator.comparingDouble(DestinationReportDTO::getTotalRevenue).reversed())
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }


    public byte[] generateDateRangeReport(Date startDate, Date endDate) throws IOException, JRException {

        List<Reservation> reservations = reservationRepository.findByReservationDateBetween(startDate, endDate);

        // Create parameters for the report
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("START_DATE", startDate);
        parameters.put("END_DATE", endDate);

        // Define the Jasper report template
        JasperReport jasperReport = JasperCompileManager.compileReport(
                new ClassPathResource("balance_report.jrxml").getInputStream());

        // Prepare the data for the report
        List<BalanceReportDTO> reportData = reservations.stream()
                .map(res -> new BalanceReportDTO(
                        Math.toIntExact(res.getId()),
                        res.getArrangement().getDestination().getCityName() + ", " + res.getArrangement().getDestination().getCountryName(), // Combine city and country
                        res.getNumberOfPeople(),
                        res.getFullPrice()
                ))
                .collect(Collectors.toList());

        Double totalRevenue = reportData.stream()
                .mapToDouble(BalanceReportDTO::getFullPrice)
                .sum();

        parameters.put("TOTAL_REVENUE", totalRevenue);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

}

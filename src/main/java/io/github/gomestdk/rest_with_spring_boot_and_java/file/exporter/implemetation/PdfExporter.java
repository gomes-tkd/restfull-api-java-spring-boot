package io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.implemetation;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.contract.ExportPeople;
import io.github.gomestdk.rest_with_spring_boot_and_java.services.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

@Component
public class PdfExporter implements ExportPeople {

    @Autowired
    private QRCodeService qrCodeService;

    @Override
    public Resource exportFile(List<PeopleDTO> people) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/templates/People.jrxml");

        if (inputStream == null) {
            throw new RuntimeException("Template file not found: /templates/People.jrxml");
        }

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);

        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource exportPeople(PeopleDTO people) throws Exception {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/Person.jrxml");
        if (mainTemplateStream == null) {
            throw new RuntimeException("Template file not found: /templates/Person.jrxml");
        }

        InputStream subReportStream = getClass().getResourceAsStream("/templates/Books.jrxml");
        if (subReportStream == null) {
            throw new RuntimeException("Template file not found: /templates/Books.jrxml");
        }

        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(people));
        JRBeanCollectionDataSource subDataSource = new JRBeanCollectionDataSource(people.getBooks());

        InputStream qrCodeStream = qrCodeService.generateQRCode(people.getProfileUrl(), 200, 200);

        String path = String.valueOf(getClass().getResource("/templates/Books.jasper"));

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SUB_REPORT_DATA_SOURCE", subDataSource);
        parameters.put("BOOK_SUB_REPORT", subReport);
        parameters.put("SUB_REPORT_DIR", path);
        parameters.put("QR_CODE_IMAGE", qrCodeStream);

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainDataSource);
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

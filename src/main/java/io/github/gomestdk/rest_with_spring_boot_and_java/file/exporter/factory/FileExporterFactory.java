package io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.factory;

import io.github.gomestdk.rest_with_spring_boot_and_java.exception.BadRequestException;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.MediaTypesFileExporter;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.contract.FileExporter;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.implemetation.CsvExporter;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.implemetation.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    public FileExporter getExporter(String acceptHeader) throws Exception {
        logger.info("Processing export with Accept header: {}", acceptHeader);
        if (MediaTypesFileExporter.APPLICATION_XLSX_VALUE.equalsIgnoreCase(acceptHeader)) {
            return applicationContext.getBean(XlsxExporter.class);
        } else if (MediaTypesFileExporter.APPLICATION_CSV_VALUE.equalsIgnoreCase(acceptHeader)) {
            return applicationContext.getBean(CsvExporter.class);
        } else {
            logger.error("Invalid file format requested: {}", acceptHeader);
            throw new BadRequestException("Invalid File Format!");
        }
    }

}

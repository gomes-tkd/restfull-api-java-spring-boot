package io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.factory;

import io.github.gomestdk.rest_with_spring_boot_and_java.exception.BadRequestException;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.contract.FileImporter;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.implemetation.CsvImporter;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.implemetation.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private ApplicationContext applicationContext;

    public FileImporter getImporter(String fileName) throws Exception {
        if (fileName.endsWith(".xlsx")) {
            return applicationContext.getBean(XlsxImporter.class);
        } else if (fileName.endsWith(".csv")) {
            return applicationContext.getBean(CsvImporter.class);
        } else {
            throw new BadRequestException("Invalid File Format!");
        }
    }

}

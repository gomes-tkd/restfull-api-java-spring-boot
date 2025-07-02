package io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.implemetation;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.contract.ExportPeople;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CsvExporter implements ExportPeople {

    @Override
    public Resource exportFile(List<PeopleDTO> peopleDTOList) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat
                .Builder.create()
                .setHeader("ID", "First Name", "Last Name", "Address", "Gender", "Enabled")
                .setSkipHeaderRecord(false).build();

        try (CSVPrinter csvPrinter = new CSVPrinter(outputStreamWriter, csvFormat)) {
            for (PeopleDTO person : peopleDTOList) {
                csvPrinter.printRecord(
                        person.getId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getGender(),
                        person.getEnabled()
                );
            }
        }

        return new ByteArrayResource(outputStream.toByteArray());
    }

    @Override
    public Resource exportPeople(PeopleDTO people) throws Exception {
        return null;
    }
}

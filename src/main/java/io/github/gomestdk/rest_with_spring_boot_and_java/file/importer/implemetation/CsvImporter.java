package io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.implemetation;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvImporter implements FileImporter {
    @Override
    public List<PeopleDTO> importFile(InputStream inputStream) throws Exception {
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader().setSkipHeaderRecord(true)
                .setIgnoreEmptyLines(true)
                .setTrim(true)
                .build();

        Iterable<CSVRecord> records = csvFormat.parse(new InputStreamReader(inputStream));
        return parseRecordsToPeopleDTO(records);
    }

    private List<PeopleDTO> parseRecordsToPeopleDTO(Iterable<CSVRecord> records) {
        List<PeopleDTO> people = new ArrayList<>();

        for (CSVRecord record : records) {
            PeopleDTO peopleDTO = new PeopleDTO();

            peopleDTO.setFirstName(record.get("first_name"));
            peopleDTO.setLastName(record.get("last_name"));
            peopleDTO.setAddress(record.get("address"));
            peopleDTO.setGender(record.get("gender"));
            peopleDTO.setEnabled(true);

            people.add(peopleDTO);
        }

        return people;
    }
}

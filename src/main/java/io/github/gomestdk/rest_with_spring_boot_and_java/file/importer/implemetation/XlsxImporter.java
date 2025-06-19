package io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.implemetation;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

@Component
public class XlsxImporter implements FileImporter {

    @Override
    public List<PeopleDTO> importFile(InputStream inputStream) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) rowIterator.next(); // Skip header

            return parseRowsToPeopleDtoList(rowIterator);
        }
    }

    private List<PeopleDTO> parseRowsToPeopleDtoList(Iterator<Row> rowIterator) {
        List<PeopleDTO> people = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowValid(row)) {
                people.add(parseRowToPeopleDto(row));
            }
        }

        return people;
    }

    private PeopleDTO parseRowToPeopleDto(Row row) {
        PeopleDTO dto = new PeopleDTO();

        dto.setFirstName(row.getCell(0).getStringCellValue());
        dto.setLastName(row.getCell(1).getStringCellValue());
        dto.setAddress(row.getCell(2).getStringCellValue());
        dto.setGender(row.getCell(3).getStringCellValue());
        dto.setEnabled(true);

        return dto;
    }

    private boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}

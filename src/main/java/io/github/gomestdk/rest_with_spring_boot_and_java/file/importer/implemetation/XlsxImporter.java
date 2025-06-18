package io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.implemetation;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class XlsxImporter implements FileImporter {

    @Override
    public List<PeopleDTO> importFile(InputStream inputStream) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

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
        PeopleDTO peopleDTO = new PeopleDTO();

        peopleDTO.setFirstName(row.getCell(0).getStringCellValue());
        peopleDTO.setLastName(row.getCell(1).getStringCellValue());
        peopleDTO.setAddress(row.getCell(2).getStringCellValue());
        peopleDTO.setGender(row.getCell(3).getStringCellValue());
        peopleDTO.setEnabled(true);

        return peopleDTO;
    }

    private static boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }
}

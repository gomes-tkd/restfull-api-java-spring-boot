package io.github.gomestdk.rest_with_spring_boot_and_java.file.importer.contract;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {
    List<PeopleDTO> importFile(InputStream inputStream) throws Exception;
}
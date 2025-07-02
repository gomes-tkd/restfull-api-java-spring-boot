package io.github.gomestdk.rest_with_spring_boot_and_java.file.exporter.contract;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.PeopleDTO;
import org.springframework.core.io.Resource;

import java.util.List;

public interface ExportPeople {
    Resource exportFile(List<PeopleDTO> people) throws Exception;
    Resource exportPeople(PeopleDTO people) throws Exception;
}
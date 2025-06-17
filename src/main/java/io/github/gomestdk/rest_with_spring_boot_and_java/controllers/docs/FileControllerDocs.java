package io.github.gomestdk.rest_with_spring_boot_and_java.controllers.docs;

import io.github.gomestdk.rest_with_spring_boot_and_java.data.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileControllerDocs {

    @Operation(
            summary = "Faz upload de um único arquivo",
            description = "Realiza o envio de um arquivo via formulário multipart",
            tags = {"File Endpoint"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = UploadFileResponseDTO.class)
                                    )
                            }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    UploadFileResponseDTO uploadFile(
            @Parameter(description = "Arquivo a ser enviado", required = true)
            @RequestParam("file") MultipartFile file
    );

    @Operation(
            summary = "Faz upload de múltiplos arquivos",
            description = "Realiza o envio de múltiplos arquivos via formulário multipart",
            tags = {"File Endpoint"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = UploadFileResponseDTO.class))
                                    )
                            }
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    List<UploadFileResponseDTO> uploadMultipleFile(
            @Parameter(description = "Arquivos a serem enviados", required = true)
            @RequestParam("files") MultipartFile[] files
    );

    @Operation(
            summary = "Realiza o download de um arquivo",
            description = "Faz o download de um arquivo armazenado no servidor pelo seu nome",
            tags = {"File Endpoint"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE
                                    )
                            }
                    ),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
//    ResponseEntity<ResponseEntity> downloadFile(
    ResponseEntity<Resource> downloadFile(
            @Parameter(description = "Nome do arquivo a ser baixado", required = true)
            @PathVariable String file,

            @Parameter(hidden = true)
            HttpServletRequest request
    );
}

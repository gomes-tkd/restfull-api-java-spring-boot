package io.github.gomestdk.rest_with_spring_boot_and_java.data.dto;

import java.io.Serializable;
import java.util.Objects;

public class UploadFileResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName;
    private String fileDownloadUir;
    private String fileType;
    private long size;

    public UploadFileResponseDTO() {
    }

    public UploadFileResponseDTO(String fileName, String fileDownloadUir, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUir = fileDownloadUir;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUir() {
        return fileDownloadUir;
    }

    public void setFileDownloadUir(String fileDownloadUir) {
        this.fileDownloadUir = fileDownloadUir;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UploadFileResponseDTO that = (UploadFileResponseDTO) o;
        return size == that.size && Objects.equals(fileName, that.fileName) && Objects.equals(fileDownloadUir, that.fileDownloadUir) && Objects.equals(fileType, that.fileType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName, fileDownloadUir, fileType, size);
    }
}

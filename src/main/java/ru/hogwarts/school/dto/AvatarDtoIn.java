package ru.hogwarts.school.dto;


public class AvatarDtoIn {

    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;

    private byte[] preview;

    private long studentId;

    public AvatarDtoIn(String filePath, long fileSize, String mediaType, byte[] data, byte[] preview, long studentId) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.preview = preview;
        this.studentId = studentId;
    }

    public AvatarDtoIn() {

    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getPreview() {
        return preview;
    }

    public void setPreview(byte[] preview) {
        this.preview = preview;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }


}

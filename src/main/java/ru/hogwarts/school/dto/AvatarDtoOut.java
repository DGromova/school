package ru.hogwarts.school.dto;

import ru.hogwarts.school.entity.Student;

public class AvatarDtoOut {

    private Long id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;
    private byte[] preview;
    private StudentDtoOut student;

    public AvatarDtoOut(Long id, String filePath, long fileSize, String mediaType, byte[] data, byte[] preview, StudentDtoOut student) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
        this.preview = preview;
        this.student = student;
    }

    public AvatarDtoOut() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StudentDtoOut getStudent() {
        return student;
    }

    public void setStudent(StudentDtoOut student) {
        this.student = student;
    }

}

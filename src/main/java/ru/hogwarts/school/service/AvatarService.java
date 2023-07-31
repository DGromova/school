package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDtoOut;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    @Value("Avatar")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;
//    private final Path pathToAvatarDir;
    private final StudentRepository studentRepository;
    private final AvatarMapper avatarMapper;

    public AvatarService(AvatarRepository avatarRepository, /*@Value("${path.to.avatar.dir}") String pathToAvatarDir, */StudentRepository studentRepository, AvatarMapper avatarMapper) {
        this.avatarRepository = avatarRepository;
//        this.pathToAvatarDir = Path.of(pathToAvatarDir);
        this.studentRepository = studentRepository;
        this.avatarMapper = avatarMapper;
    }

    public AvatarDtoOut uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
//        if(avatarFile.getSize() > 1024*300) {
//            throw new FileIsTooBigException("File is too biq");
//        }
        Student student = studentRepository.findById(studentId).get();
        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }

        return Optional.of(findAvatarByStudentId(studentId))
                .map(avatar -> {
                    avatar.setStudent(student);
                    avatar.setFilePath(filePath.toString());
                    avatar.setFileSize(avatarFile.getSize());
                    avatar.setMediaType(avatarFile.getContentType());
                    try {
                        avatar.setPreview(generateImagePreview(filePath));
 //                       avatar.setData(avatarFile.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return avatarMapper.toDto(avatarRepository.save(avatar));
                }).orElseThrow(()-> new StudentNotFoundException(studentId));
    }

    public Avatar findAvatarByStudentId(Long studentId) {
        return avatarRepository.findAvatarByStudent_Id(studentId).orElse(new Avatar());
    }

    public Avatar findAvatarById(Long id) {
        return avatarRepository.findById(id).orElseThrow(()-> new AvatarNotFoundException(id));
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtensions(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public Pair<byte[], String> getFromDb(long id) {
            Avatar avatar = avatarRepository.findById(id).orElseThrow(() -> new AvatarNotFoundException(id));
            return Pair.of(avatar.getData(), avatar.getMediaType());
    }


}

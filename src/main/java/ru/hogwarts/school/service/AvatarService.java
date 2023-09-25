package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.exception.AvatarNotFoundException;
import ru.hogwarts.school.exception.AvatarProcessingException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.repository.AvatarRepository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AvatarService {

//    @Value("Avatar")
//    private String avatarsDir;

    private final AvatarRepository avatarRepository;
    private final Path pathToAvatarDir;
    private final AvatarMapper avatarMapper;

    public AvatarService(AvatarRepository avatarRepository,
                         @Value("${path.to.avatar.dir}") String pathToAvatarDir, AvatarMapper avatarMapper) {
        this.avatarRepository = avatarRepository;
        this.pathToAvatarDir = Path.of(pathToAvatarDir);
        this.avatarMapper = avatarMapper;
    }

    public Avatar create(Student student, MultipartFile multipartFile) {
        try {
            String contentType = multipartFile.getContentType();
            String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            byte[] data = multipartFile.getBytes();
            String filename = UUID.randomUUID() + "." + extension;
            Path pathToAvatar = pathToAvatarDir.resolve(filename);
            Files.createDirectories(pathToAvatarDir);
            writeToFile(pathToAvatar, data);

            Avatar avatar = avatarRepository.findByStudent_Id(student.getId())
                    .orElse(new Avatar());
            avatar.setMediaType(contentType);
            avatar.setFileSize(data.length);
            avatar.setData(data);
            avatar.setStudent(student);
            avatar.setFilePath(pathToAvatar.toString());
            return avatarRepository.save(avatar);
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    private void writeToFile(Path path, byte[] data) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path.toFile())) {
            fileOutputStream.write(data);
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    public Pair<byte[], String> getFromDb(long id) {
            Avatar avatar = avatarRepository.findById(id).orElseThrow(() -> new AvatarNotFoundException(id));
            return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Pair<byte[], String> getFromFs(long id) {
        try {
            Avatar avatar = avatarRepository.findById(id).orElseThrow(() -> new AvatarNotFoundException(id));
            return Pair.of(Files.readAllBytes(Path.of(avatar.getFilePath())), avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    private byte[] read(Path path) {
        try (FileInputStream fileInputStream = new FileInputStream(path.toFile())) {
            return fileInputStream.readAllBytes();
        }
        catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }


    public List<AvatarDto> getPage(int page, int size) {
        return avatarRepository.findAll(PageRequest.of(page, size)).stream()
                .map(avatarMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }
}

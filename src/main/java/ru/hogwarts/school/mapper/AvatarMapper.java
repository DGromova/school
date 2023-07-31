package ru.hogwarts.school.mapper;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.AvatarDtoIn;
import ru.hogwarts.school.dto.AvatarDtoOut;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Optional;

@Component
public class AvatarMapper {

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    public AvatarMapper(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public AvatarDtoOut toDto(Avatar avatar) {
        AvatarDtoOut avatarDtoOut = new AvatarDtoOut();
        avatarDtoOut.setFilePath(avatar.getFilePath());
        avatarDtoOut.setFileSize(avatar.getFileSize());
//        avatarDtoOut.setData(avatar.getData());
        Optional.ofNullable(avatar.getStudent())
                .ifPresent(student -> avatarDtoOut.setStudent(studentMapper.toDto(student)));
        return avatarDtoOut;
    }

    public Avatar toEntity(AvatarDtoIn avatarDtoIn) {
        Avatar avatar = new Avatar();
//        avatar.setData(avatarDtoIn.getData());
        avatar.setFilePath(avatarDtoIn.getFilePath());
        avatar.setFileSize(avatarDtoIn.getFileSize());
        avatar.setMediaType(avatarDtoIn.getMediaType());
        avatar.setPreview(avatarDtoIn.getPreview());
        Optional.ofNullable(avatarDtoIn.getStudentId())
                .ifPresent(studentId -> studentRepository.findById(studentId)
                        .orElseThrow(() -> new StudentNotFoundException(studentId)));
        return avatar;
    }


}

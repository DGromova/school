package ru.hogwarts.school.mapper;

import org.springframework.stereotype.Component;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;

import java.util.Optional;

@Component
public class AvatarMapper {

//    private final StudentRepository studentRepository;
//
//    private final StudentMapper studentMapper;

//    public AvatarMapper(StudentRepository studentRepository, StudentMapper studentMapper) {
//        this.studentRepository = studentRepository;
//        this.studentMapper = studentMapper;
//    }

    public AvatarDto toDto(Avatar avatar) {
        AvatarDto avatarDto = new AvatarDto();
        avatarDto.setId(avatar.getId());
        avatarDto.setFileSize(avatar.getFileSize());
        avatarDto.setMediaType(avatar.getMediaType());
        avatarDto.setAvatarUrl("http://localhost:8080/avatars/" + avatar.getId() + "/from-db");
        return avatarDto;
    }

}

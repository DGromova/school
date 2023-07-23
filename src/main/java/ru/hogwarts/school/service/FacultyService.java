package ru.hogwarts.school.service;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final StudentMapper studentMapper;

    public FacultyService(FacultyRepository facultyRepository, FacultyMapper facultyMapper, StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.studentMapper = studentMapper;
    }


    public FacultyDtoOut create(FacultyDtoIn facultyDtoIn) {
        return facultyMapper.toDto(
                facultyRepository.save(
                        facultyMapper.toEntity(facultyDtoIn)));
    }

    public FacultyDtoOut find(Long id) {
        return facultyRepository.findById(id)
                .map(facultyMapper::toDto).orElseThrow(()-> new FacultyNotFoundException(id));
    }

    public FacultyDtoOut edit(Long id, FacultyDtoIn facultyDtoIn) {
        return facultyRepository.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setName(facultyDtoIn.getName());
                    oldFaculty.setColor(facultyDtoIn.getColor());
                    return facultyMapper.toDto(facultyRepository.save(oldFaculty));
                }).orElseThrow(()-> new FacultyNotFoundException(id));
    }

    public FacultyDtoOut delete(Long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(()-> new FacultyNotFoundException(id));
        facultyRepository.delete(faculty);
        return facultyMapper.toDto(faculty);
    }

    public List<FacultyDtoOut> findAll(@Nullable String color) {
        return Optional.ofNullable(color)
                .map(c -> facultyRepository.findAllByColorIgnoreCase(c))
                .orElseGet(facultyRepository::findAll).stream()
                        .map(facultyMapper::toDto)
                        .collect(Collectors.toUnmodifiableList());
    }


    public List<FacultyDtoOut> findByNameOrColor(String nameOrColor) {
        return (facultyRepository.findAllByNameContainingIgnoreCaseOrColorContainingIgnoreCase(
                nameOrColor,
                nameOrColor)
                .stream()
                .map(facultyMapper::toDto)
                .collect(Collectors.toUnmodifiableList()));
    }

    public List<StudentDtoOut> findStudentsByFaculty(String name) {
        return facultyRepository.findByNameIgnoreCase(name).getStudents()
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }
}

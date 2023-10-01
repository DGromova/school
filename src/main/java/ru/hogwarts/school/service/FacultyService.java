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
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public FacultyService(FacultyRepository facultyRepository, FacultyMapper facultyMapper, StudentRepository studentRepository, StudentMapper studentMapper) {
        this.facultyRepository = facultyRepository;
        this.facultyMapper = facultyMapper;
        this.studentRepository = studentRepository;
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

    public FacultyDtoOut update(Long id, FacultyDtoIn facultyDtoIn) {
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

    public List<StudentDtoOut> findStudentsByFacultyId(long id) {
        return studentRepository.findAllByFaculty_Id(id)
                .stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public String getLongestFacultyName() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length)).get();
    }

    public Integer getSum() {
        //int sum = Stream.iterate(1, a -> a +1) .limit(1_000_000) .reduce(0, (a, b) -> a + b );
        return Stream.iterate(1, i -> i < 1_000_001, i -> i+1)
                .parallel()
                .mapToInt(i -> i).sum();
    }




}

package ru.hogwarts.school.service;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, FacultyMapper facultyMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
    }

    public StudentDtoOut create(StudentDtoIn studentDtoIn) {
        return studentMapper.toDto(
                studentRepository.save(
                        studentMapper.toEntity(studentDtoIn)));
    }

    public StudentDtoOut find(long id) {
        return studentRepository.findById(id)
                .map(studentMapper::toDto).orElseThrow(()-> new StudentNotFoundException(id));
    }

    public StudentDtoOut edit(long id, StudentDtoIn studentDtoIn) {
        return studentRepository.findById(id)
                .map(oldFaculty -> {
                    oldFaculty.setName(studentDtoIn.getName());
                    oldFaculty.setAge(studentDtoIn.getAge());
                    return studentMapper.toDto(studentRepository.save(oldFaculty));
                }).orElseThrow(()-> new StudentNotFoundException(id));
    }

    public StudentDtoOut delete(long id) {
        Student student = studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException(id));
        studentRepository.delete(student);
        return studentMapper.toDto(student);
    }

    public List<StudentDtoOut> findAll(@Nullable Integer age) {
        return Optional.ofNullable(age).
                map(a -> studentRepository.findAllByAge(a))
                .orElseGet(studentRepository::findAll).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<StudentDtoOut> findAllByAgeBetween(int ageFrom, int ageTo) {
        return studentRepository.findAllByAgeBetween(ageFrom, ageTo)
                .stream().map(studentMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public FacultyDtoOut findFacultyByStudentName(String studentName) {
        return facultyMapper.toDto((studentRepository.findStudentByNameIgnoreCase(studentName)).getFaculty());
    }

}

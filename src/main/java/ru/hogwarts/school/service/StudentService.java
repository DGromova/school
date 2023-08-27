package ru.hogwarts.school.service;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final FacultyMapper facultyMapper;
    private final FacultyRepository facultyRepository;
    private final AvatarService avatarService;
    private final AvatarMapper avatarMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper, FacultyMapper facultyMapper, FacultyRepository facultyRepository, AvatarService avatarService, AvatarMapper avatarMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.facultyMapper = facultyMapper;
        this.facultyRepository = facultyRepository;
        this.avatarService = avatarService;
        this.avatarMapper = avatarMapper;
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

    public StudentDtoOut update(long id, StudentDtoIn studentDtoIn) {
        return studentRepository.findById(id)
                .map(oldStudent -> {
                    oldStudent.setName(studentDtoIn.getName());
                    oldStudent.setAge(studentDtoIn.getAge());
                    Optional.ofNullable(studentDtoIn.getFacultyId())
                            .ifPresent(facultyId ->
                                    oldStudent.setFaculty(
                                            facultyRepository.findById(facultyId)
                                    .orElseThrow(()-> new FacultyNotFoundException(facultyId))
                                    )
                            );
                    return studentMapper.toDto(studentRepository.save(oldStudent));
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
        return studentRepository.findAllByAgeBetween(ageFrom, ageTo).stream()
                .map(studentMapper::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    public FacultyDtoOut findFacultyByStudentId(long id) {
//        return studentRepository.findById(id)
//                .map(Student::getFaculty)
//                .map(facultyMapper::toDto).orElseThrow(()-> new StudentNotFoundException(id));
        Faculty faculty = studentRepository.findById(id)
                .map(Student::getFaculty).orElseThrow(()-> new StudentNotFoundException(id));
        return facultyMapper.toDto(faculty);
    }

    public StudentDtoOut uploadAvatar(long id, MultipartFile multipartFile) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        Avatar avatar = avatarService.create(student, multipartFile);
        StudentDtoOut studentDtoOut = studentMapper.toDto(student);
        studentDtoOut.setAvatar(avatarMapper.toDto(avatar));
        return studentDtoOut;
    }

    public long getStudentsCount() {
        return studentRepository.getStudentsCount();
    }

    public float getStudentsAverageAge() {
        return studentRepository.getStudentsAverageAge();
    }

    @Transactional(readOnly = true)
    public List<StudentDtoOut> getLastStudents(int count) {
        return studentRepository.getLastStudents(Pageable.ofSize(count)).stream()
                .map(studentMapper::toDto).collect(Collectors.toUnmodifiableList());
    }

}

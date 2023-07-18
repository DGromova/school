package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        }

    public Student createStudent(Student student) {
        studentRepository.save(student);
        return student;
    }

    public Student findStudentById(Long studentId) {
        return studentRepository.findById(studentId).get();
    }

    public List<Student> getStudentsByAge(int studentsAge) {
        return studentRepository.findAll().stream()
                .filter(student -> studentsAge == student.getAge()).collect(Collectors.toUnmodifiableList());
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

//    public Student findStudentByNameIgnoreCase(String studentName) {
//        return studentRepository.findStudentByNameIgnoreCase(studentName);
//    }

    public Faculty findFacultyByStudentName(String studentName) {
        return studentRepository.findStudentByNameIgnoreCase(studentName).getFaculty();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
    }

}

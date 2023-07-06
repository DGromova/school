package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.stream.Collectors;

public interface StudentService {

    Student createStudent(Student student);

    Student getStudentById(Long studentId);

    List<Student> getStudentsByAge(int studentsAge);

    List<Student> getAll();

    Student updateStudent(Long studentId, Student student);

    Student deleteStudent(Long studentId);

}

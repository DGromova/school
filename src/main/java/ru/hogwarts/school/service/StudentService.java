package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.List;

public interface StudentService {

    Student createStudent(Student student);

    Student findStudentById(Long studentId);

    List<Student> getStudentsByAge(int studentsAge);

    List<Student> findAll();

    Student editStudent(Student student);

    void deleteStudent(Long studentId);

}

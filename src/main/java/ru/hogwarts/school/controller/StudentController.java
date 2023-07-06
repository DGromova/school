package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;

    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @PostMapping
    public ResponseEntity createStudent(@RequestBody Student student) {
        Student createdStudent = studentServiceImpl.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("{studentId}")
    public ResponseEntity getStudent(@PathVariable Long studentId) {
        Student student = studentServiceImpl.getStudentById(studentId);
        if(student == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("{studentsAge}")
    public ResponseEntity getStudentsByAge(@PathVariable int studentsAge) {
        List<Student> studentsByAge = studentServiceImpl.getStudentsByAge(studentsAge);
        if(studentsByAge.isEmpty()) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(studentsByAge);
    }

    @PutMapping()
    public ResponseEntity updateStudent(@RequestBody Student student) {
        Student updatedStudent = studentServiceImpl.updateStudent(student.getId(), student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("{studentId}")
    public ResponseEntity deleteStudent(@PathVariable Long studentId) {
        Student deletedStudent = studentServiceImpl.deleteStudent(studentId);
        if(deletedStudent == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(deletedStudent);
    }

}

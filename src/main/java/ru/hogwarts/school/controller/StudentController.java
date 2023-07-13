package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;

    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentServiceImpl.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("{studentId}")
    public ResponseEntity findStudent(@PathVariable Long studentId) {
        Student student = studentServiceImpl.findStudentById(studentId);
        if(student == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("age/{studentsAge}")
    public ResponseEntity<Collection> getStudentsByAge(@PathVariable int studentsAge) {
        List<Student> studentsByAge = studentServiceImpl.getStudentsByAge(studentsAge);
        if(studentsByAge.isEmpty()) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(studentsByAge);
    }

    @PutMapping()
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student editedStudent = studentServiceImpl.editStudent(student);
        return ResponseEntity.ok(editedStudent);
    }

    @DeleteMapping("{studentId}")
    public ResponseEntity deleteStudent(@PathVariable Long studentId) {
        studentServiceImpl.deleteStudent(studentId);
        return ResponseEntity.ok().build();
    }

}

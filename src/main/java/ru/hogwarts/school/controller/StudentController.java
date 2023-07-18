package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentServiceImpl;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;
    private final StudentRepository studentRepository;

    public StudentController(StudentServiceImpl studentServiceImpl, StudentRepository studentRepository) {
        this.studentServiceImpl = studentServiceImpl;
        this.studentRepository = studentRepository;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student createdStudent = studentServiceImpl.createStudent(student);
        return ResponseEntity.ok(createdStudent);
    }

    @GetMapping("{studentId}")
    public ResponseEntity<Student> findStudent(@PathVariable Long studentId) {
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
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentsByAge);
    }

    @GetMapping("age/{min}/{max}")
    public ResponseEntity<Collection> findStudentsByAgeBetween(@PathVariable int min, @PathVariable int max) {
        return ResponseEntity.ok(studentRepository.findByAgeBetween(min, max));
    }

    @GetMapping
    public ResponseEntity<Collection> findAll() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("/{name}")
    public ResponseEntity findFacultyByStudentName(@PathVariable String name) {
        return ResponseEntity.ok(studentServiceImpl.findFacultyByStudentName(name));
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

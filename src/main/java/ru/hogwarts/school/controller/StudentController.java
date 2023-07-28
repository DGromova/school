package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentDtoOut create(@RequestBody StudentDtoIn studentDtoIn) {
        return studentService.create(studentDtoIn);
    }

    @GetMapping("/{id}")
    public StudentDtoOut find(@PathVariable long id) {
        return studentService.find(id);
    }

    @PutMapping("/{id}")
    public StudentDtoOut edit(@PathVariable long id, @RequestBody StudentDtoIn studentDtoIn) {
        return studentService.edit(id, studentDtoIn);
    }

    @DeleteMapping("/{id}")
    public StudentDtoOut delete(@PathVariable long id) {
        return studentService.delete(id);
    }

    @GetMapping
    public List<StudentDtoOut> findAll(@RequestParam(required = false) Integer age) {
        return studentService.findAll(age);
    }

        @GetMapping("/range")
    public  List<StudentDtoOut> findByAgeBetween(@RequestParam int ageFrom, @RequestParam int ageTo) {
        return studentService.findAllByAgeBetween(ageFrom, ageTo);
    }

    @GetMapping("{id}/faculty")
    public FacultyDtoOut findFacultyByStudentName(@PathVariable("id") long id) {
        return studentService.findFacultyByStudentId(id);
    }

}

package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private static final Logger LOG = LoggerFactory.getLogger(StudentController.class);

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
    public StudentDtoOut update(@PathVariable long id, @RequestBody StudentDtoIn studentDtoIn) {
            return studentService.update(id, studentDtoIn);
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

    @GetMapping("/{id}/faculty")
    public FacultyDtoOut findFacultyByStudentId(@PathVariable("id") long id) {
        return studentService.findFacultyByStudentId(id);
    }

    @PatchMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public StudentDtoOut uploadAvatar(@PathVariable("id") long id, @RequestPart("avatar") MultipartFile multipartFile) {
        return studentService.uploadAvatar(id, multipartFile);
    }

    @GetMapping("/count")
    public long getStudentsCount() {
        return studentService.getStudentsCount();
    }

    @GetMapping("averageAge")
    public float getStudentsAverageAge()
    {
        return studentService.getStudentsAverageAge();
    }

    @GetMapping("/lastStudents")
    public List<StudentDtoOut> getLastStudents(@RequestParam(value = "count", defaultValue = "5", required = false) int count) {
        return studentService.getLastStudents(Math.abs(count));
    }

    @GetMapping("/namesStartsWithG")
    public List<String> getStudentsNamesStartsWithG() {
        return studentService.getStudentsNamesStartsWithG();
    }

    @GetMapping("/medianAge")
    public double getStudentsMedianAge() {
        return studentService.getStudentsMedianAge();
    }

    @GetMapping("/namesList")
    public List<String> namesList() {
        return studentService.namesList();
    }

    @GetMapping("/names")
    public  void getNames() {
        studentService.getNames();
    }

    @GetMapping("/namesInOrder")
    public  void getNamesInOrder() {
        studentService.getNamesInOrder();
    }

}

package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculties")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public FacultyDtoOut create(@RequestBody FacultyDtoIn facultyDtoIn) {
        return facultyService.create(facultyDtoIn);
    }

    @GetMapping("/{id}")
    public FacultyDtoOut find(@PathVariable long id) {
        return facultyService.find(id);
    }

    @PutMapping("/{id}")
    public FacultyDtoOut update(@PathVariable long id, @RequestBody FacultyDtoIn facultyDtoIn) {
        return facultyService.update(id, facultyDtoIn);
    }

    @DeleteMapping("/{id}")
    public FacultyDtoOut delete(@PathVariable long id) {
        return facultyService.delete(id);
    }

    @GetMapping
    public List<FacultyDtoOut> findAll(@RequestParam(required = false) String color) {
        return facultyService.findAll(color);
    }

    @GetMapping("/filter")
    public List<FacultyDtoOut> findByNameOrColor(@RequestParam String nameOrColor) {
        return facultyService.findByNameOrColor(nameOrColor);
    }

    @GetMapping("/students")
    public List<StudentDtoOut> findStudentsByFacultyId(@RequestParam long id) {
        return facultyService.findStudentsByFacultyId(id);
    }
}

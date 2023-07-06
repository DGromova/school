package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyServiceImpl facultyServiceImpl;

    public FacultyController(FacultyServiceImpl facultyServiceImpl) {
        this.facultyServiceImpl = facultyServiceImpl;
    }

    @PostMapping
    public ResponseEntity createFaculty(@RequestBody Faculty faculty) {
        Faculty createdfaculty = facultyServiceImpl.createFaculty(faculty);
        return ResponseEntity.ok(createdfaculty);
    }

    @GetMapping("{studentId}")
    public ResponseEntity getFaculty(@PathVariable Long facultyId) {
        Faculty faculty = facultyServiceImpl.getFacultyById(facultyId);
        if(faculty == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("{color}")
    public ResponseEntity getFacultyByColor(@PathVariable String color) {
        List faculty = facultyServiceImpl.getFacultyByColor(color);
        if(faculty.isEmpty()) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping()
    public ResponseEntity updateFaculty(@RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyServiceImpl.updateFaculty(faculty.getId(), faculty);
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("{studentId}")
    public ResponseEntity deleteFaculty(@PathVariable Long facultyId) {
        Faculty deletedFaculty = facultyServiceImpl.deleteFaculty(facultyId);
        if(deletedFaculty == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(deletedFaculty);
    }

}

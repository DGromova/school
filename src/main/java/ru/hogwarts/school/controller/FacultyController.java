package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity createFaculty(@RequestBody Faculty faculty) {
        Faculty createdfaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(createdfaculty);
    }

    @GetMapping("{studentId}")
    public ResponseEntity getFaculty(@PathVariable Long facultyId) {
        Faculty faculty = facultyService.getFacultyById(facultyId);
        if(faculty == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("{color}")
    public ResponseEntity getFacultyByColor(@PathVariable String color) {
        List faculty = facultyService.getFacultyByColor(color);
        if(faculty.isEmpty()) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping()
    public ResponseEntity updateFaculty(@RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyService.updateFaculty(faculty.getId(), faculty);
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("{studentId}")
    public ResponseEntity deleteFaculty(@PathVariable Long facultyId) {
        Faculty deletedFaculty = facultyService.deleteFaculty(facultyId);
        if(deletedFaculty == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(deletedFaculty);
    }

}

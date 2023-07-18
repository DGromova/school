package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyServiceImpl facultyServiceImpl;
    private final FacultyRepository facultyRepository;

    public FacultyController(FacultyServiceImpl facultyServiceImpl, FacultyRepository facultyRepository) {
        this.facultyServiceImpl = facultyServiceImpl;
        this.facultyRepository = facultyRepository;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty createdfaculty = facultyServiceImpl.createFaculty(faculty);
        return ResponseEntity.ok(createdfaculty);
    }

    @GetMapping("{facultyId}")
    public ResponseEntity<Faculty> findFaculty(@PathVariable Long facultyId) {
        Faculty faculty = facultyServiceImpl.findFacultyById(facultyId);
        if(faculty == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping
    public ResponseEntity<Collection> findAll() {
        Collection allAddedFaculties = List.copyOf(facultyServiceImpl.findAll());
        return ResponseEntity.ok(allAddedFaculties);
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<Collection> findFacultiesByColor(@PathVariable String color) {
        List faculty = facultyServiceImpl.findFacultiesByColor(color);
        if(faculty.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @GetMapping("/{name}/{color}")
    public ResponseEntity<Collection> findFacultyByNameAndColor(@PathVariable String name, @PathVariable String color) {
        List faculty = List.copyOf(facultyRepository.findByNameAndColorIgnoreCase(name, color));
        if(faculty.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PutMapping()
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty editedFaculty = facultyServiceImpl.editFaculty(faculty);
        if (editedFaculty == null) {
            return ResponseEntity.notFound() .build();
        }
        return ResponseEntity.ok(editedFaculty);
    }

    @DeleteMapping("{facultyId}")
    public ResponseEntity deleteFaculty(@PathVariable Long facultyId) {
        facultyServiceImpl.deleteFaculty(facultyId);
        return ResponseEntity.ok().build();
    }

}

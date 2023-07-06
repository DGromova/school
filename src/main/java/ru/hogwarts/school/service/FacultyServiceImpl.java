package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService{

    private Map<Long, Faculty> faculties = new HashMap<>();

    private Long generatedFacultyId = 1L;

    public Faculty createFaculty(Faculty faculty) {
        faculty.setId(generatedFacultyId);
        faculties. put(generatedFacultyId, faculty);
        generatedFacultyId++;
        return faculty;
    }

    public Faculty getFacultyById(Long facultyId) {
        return faculties.get(facultyId);
    }

    public List getFacultyByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor() == color)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Faculty> getAll() {
        return faculties.values().stream().collect(Collectors.toUnmodifiableList());
    }

    public Faculty updateFaculty(Long facultyId, Faculty faculty) {
        faculties.put(facultyId, faculty);
        return faculty;
    }

    public Faculty deleteFaculty(Long facultyId) {
        return faculties.remove(facultyId);
    }

}

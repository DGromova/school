package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements FacultyService{
    private final FacultyRepository facultyRepository;

    public FacultyServiceImpl(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
        return faculty;
    }

    public Faculty findFacultyById(Long facultyId) {
        return facultyRepository.findById(facultyId).get();
    }

    public List findFacultiesByColor(String color) {
        return facultyRepository.findAll().stream()
                .filter(faculty -> (faculty.getColor()).equals(color))
                .collect(Collectors.toUnmodifiableList());
    }

    public List<Faculty> findAll() {
        return facultyRepository.findAll();
    }

    public Faculty editFaculty(Faculty faculty) {
        facultyRepository.save(faculty);
        return faculty;
    }

    public void deleteFaculty(Long facultyId) {
        facultyRepository.deleteById(facultyId);
    }

}

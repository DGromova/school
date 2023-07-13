package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyService {

    Faculty createFaculty(Faculty faculty);

    Faculty findFacultyById(Long facultyId);

    List findFacultiesByColor(String color);

    List<Faculty> findAll();

    Faculty editFaculty(Faculty faculty);

    void deleteFaculty(Long facultyId);

}

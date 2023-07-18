package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Collection<Faculty> findByNameAndColorIgnoreCase(String name, String color);

    int findFacultyById(int facultyId);

}

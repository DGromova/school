package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.Faculty;

import java.util.Collection;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    List<Faculty> findAllByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color);

    Faculty findById(long id);

    List<Faculty> findAll();

    List<Faculty> findAllByColorIgnoreCase(String color);

    Faculty findByNameIgnoreCase(String name);

}

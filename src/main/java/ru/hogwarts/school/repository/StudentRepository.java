package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAgeBetween(int min, int max);

    List<Student> findAll();

    List<Student> findAllByAge(int age);

    List<Student> findAllByFaculty_Id(long id);

    Optional<Student> findById(Long id);


}

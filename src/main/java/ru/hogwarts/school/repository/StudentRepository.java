package ru.hogwarts.school.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByAgeBetween(int min, int max);

    List<Student> findAll();

    List<Student> findAllByAge(int age);

    List<Student> findAllByFaculty_Id(long id);

    Optional<Student> findById(long id);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    long getStudentsCount();

    @Query(value = "SELECT AVG(AGE) FROM student", nativeQuery = true)
    float getStudentsAverageAge();

//    @Query("SELECT new ru.hogwarts.school.dto.StudentDtoOut(s.id, s.name, s.age, f.id, f.name, f.color, a.id) FROM Student s LEFT JOIN Faculty f on s.faculty LEFT JOIN Avatar a ON a.student = s ORDER BY s.id DESC")
//    List<StudentDtoOut> getLastStudents(Pageable pageable);

//    @Query(value = "SELECT * FROM student ORDER BY id DESC LiMIT count", nativeQuery = true)
//    List<Student> getLastStudents(@Param("count") long count);

    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> getLastStudents(Pageable pageable);

}

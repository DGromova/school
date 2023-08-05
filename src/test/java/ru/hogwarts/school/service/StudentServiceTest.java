/*package ru.hogwarts.school.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Student;

import static org.assertj.core.api.Assertions.*;

class StudentServiceImplTest {

    private final StudentServiceImpl studentServiceImpl = new StudentServiceImpl();

    @BeforeEach
    public void beforeEach() {
        Student s1 = new Student(1L, "Браун Лаванда", 15);
        Student s2 = new Student(2L, "Грейнджер Гермиона", 15);
        Student s3 = new Student(3L, "Томас Дин", 16);
        Student s4 = new Student(4L, "Финниган Симус", 16);

        studentServiceImpl.createStudent(s1);
        studentServiceImpl.createStudent(s2);
        studentServiceImpl.createStudent(s3);
        studentServiceImpl.createStudent(s4);
    }

    @AfterEach
    public void afterEach() {
        studentServiceImpl.getAll().forEach(s -> studentServiceImpl.deleteStudent(s.getId()));
    }

    @Test
    public void createStudentTest() {
        Student expected = new Student(5L, "Поттер Гарри", 15);
        int beforeCount = studentServiceImpl.getAll().size();

        assertThat(studentServiceImpl.createStudent(new Student(5L, "Поттер Гарри", 15)))
                .isEqualTo(expected)
                .isIn(studentServiceImpl.getAll());
        assertThat(studentServiceImpl.getAll().size()).isEqualTo(beforeCount + 1);
    }

    @Test
    public void getStudentByIdTest() {
        Student expected = new Student(1L, "Браун Лаванда", 15);
        int beforeCount = studentServiceImpl.getAll().size();

        assertThat(studentServiceImpl.getStudentById(1L)).isEqualTo(expected);
        assertThat(studentServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void getStudentsByAgeTest() {
        Student expected1 = new Student(1L, "Браун Лаванда", 15);
        Student expected2 = new Student(2L, "Грейнджер Гермиона", 15);
        int beforeCount = studentServiceImpl.getAll().size();

        assertThat(studentServiceImpl.getStudentsByAge(15)).containsExactlyInAnyOrder(expected1, expected2);
        assertThat(studentServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void updateStudent() {
        Student expected = new Student(1L, "Браун Лаванда", 15);
        int beforeCount = studentServiceImpl.getAll().size();

        assertThat(studentServiceImpl.updateStudent(1L, new Student(1L, "Браун Лаванда", 15))).isEqualTo(expected);
        assertThat(studentServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void deleteStudent() {
        Student expected = new Student(1L, "Браун Лаванда", 15);
        int beforeCount = studentServiceImpl.getAll().size();

        assertThat(studentServiceImpl.deleteStudent(1L)).isEqualTo(expected)
                .isNotIn(studentServiceImpl.getAll());
        assertThat(studentServiceImpl.getAll().size()).isEqualTo(beforeCount - 1);
    }
}

 */
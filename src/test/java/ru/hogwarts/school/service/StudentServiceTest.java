package ru.hogwarts.school.service;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class StudentServiceTest {

    private final StudentService studentService = new StudentService();

    @BeforeEach
    public void beforeEach() {
        Student s1 = new Student(1L, "Браун Лаванда", 15);
        Student s2 = new Student(2L, "Грейнджер Гермиона", 15);
        Student s3 = new Student(3L, "Томас Дин", 16);
        Student s4 = new Student(4L, "Финниган Симус", 16);

        studentService.createStudent(s1);
        studentService.createStudent(s2);
        studentService.createStudent(s3);
        studentService.createStudent(s4);
    }

    @AfterEach
    public void afterEach() {
        studentService.getAll().forEach(s -> studentService.deleteStudent(s.getId()));
    }

    @Test
    public void createStudentTest() {
        Student expected = new Student(5L, "Поттер Гарри", 15);
        int beforeCount = studentService.getAll().size();

        assertThat(studentService.createStudent(new Student(5L, "Поттер Гарри", 15)))
                .isEqualTo(expected)
                .isIn(studentService.getAll());
        assertThat(studentService.getAll().size()).isEqualTo(beforeCount + 1);
    }

    @Test
    public void getStudentByIdTest() {
        Student expected = new Student(1L, "Браун Лаванда", 15);
        int beforeCount = studentService.getAll().size();

        assertThat(studentService.getStudentById(1L)).isEqualTo(expected);
        assertThat(studentService.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void getStudentsByAgeTest() {
        Student expected1 = new Student(1L, "Браун Лаванда", 15);
        Student expected2 = new Student(2L, "Грейнджер Гермиона", 15);
        int beforeCount = studentService.getAll().size();

        assertThat(studentService.getStudentsByAge(15)).containsExactlyInAnyOrder(expected1, expected2);
        assertThat(studentService.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void updateStudent() {
        Student expected = new Student(1L, "Браун Лаванда", 15);
        int beforeCount = studentService.getAll().size();

        assertThat(studentService.updateStudent(1L, new Student(1L, "Браун Лаванда", 15))).isEqualTo(expected);
        assertThat(studentService.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void deleteStudent() {
        Student expected = new Student(1L, "Браун Лаванда", 15);
        int beforeCount = studentService.getAll().size();

        assertThat(studentService.deleteStudent(1L)).isEqualTo(expected)
                .isNotIn(studentService.getAll());
        assertThat(studentService.getAll().size()).isEqualTo(beforeCount - 1);
    }
}
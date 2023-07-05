package ru.hogwarts.school.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FacultyServiceTest {

    private final FacultyService facultyService = new FacultyService();

    @BeforeEach
    public void beforeEach() {
        Faculty f1 = new Faculty(1L, "Гриффиндор", "красный");
        Faculty f2 = new Faculty(2L, "Слизерин", "зелёный");
        Faculty f3 = new Faculty(3L, "Когтевран", "синий");

        facultyService.createFaculty(f1);
        facultyService.createFaculty(f2);
        facultyService.createFaculty(f3);
    }

    @AfterEach
    public void afterEach() {
        facultyService.getAll().forEach(f -> facultyService.deleteFaculty(f.getId()));
    }

    @Test
    public void createFacultyTest() {
        Faculty expected = new Faculty(4L, "Пуффендуй", "жёлтый");
        int beforeCount = facultyService.getAll().size();

        assertThat(facultyService.createFaculty(new Faculty(4L, "Пуффендуй", "жёлтый")))
                .isEqualTo(expected)
                .isIn(facultyService.getAll());
        assertThat(facultyService.getAll().size()).isEqualTo(beforeCount + 1);
    }

    @Test
    public void getFacultyByIdTest() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyService.getAll().size();

        assertThat(facultyService.getFacultyById(1L)).isEqualTo(expected);
        assertThat(facultyService.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void getFacultyByColorTest() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyService.getAll().size();

        assertThat(facultyService.getFacultyByColor("красный")).containsExactlyInAnyOrder(expected);
        assertThat(facultyService.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void updateFaculty() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyService.getAll().size();

        assertThat(facultyService.updateFaculty(1L, new Faculty(1L, "Гриффиндор", "красный"))).isEqualTo(expected);
        assertThat(facultyService.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void deleteFaculty() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyService.getAll().size();

        assertThat(facultyService.deleteFaculty(1L)).isEqualTo(expected)
                .isNotIn(facultyService.getAll());
        assertThat(facultyService.getAll().size()).isEqualTo(beforeCount - 1);
    }
}
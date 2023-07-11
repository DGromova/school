/*package ru.hogwarts.school.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;

import static org.assertj.core.api.Assertions.assertThat;

class FacultyServiceImplTest {

    private final FacultyServiceImpl facultyServiceImpl = new FacultyServiceImpl();

    @BeforeEach
    public void beforeEach() {
        Faculty f1 = new Faculty(1L, "Гриффиндор", "красный");
        Faculty f2 = new Faculty(2L, "Слизерин", "зелёный");
        Faculty f3 = new Faculty(3L, "Когтевран", "синий");

        facultyServiceImpl.createFaculty(f1);
        facultyServiceImpl.createFaculty(f2);
        facultyServiceImpl.createFaculty(f3);
    }

    @AfterEach
    public void afterEach() {
        facultyServiceImpl.getAll().forEach(f -> facultyServiceImpl.deleteFaculty(f.getId()));
    }

    @Test
    public void createFacultyTest() {
        Faculty expected = new Faculty(4L, "Пуффендуй", "жёлтый");
        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.createFaculty(new Faculty(4L, "Пуффендуй", "жёлтый")))
                .isEqualTo(expected)
                .isIn(facultyServiceImpl.getAll());
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount + 1);
    }

    @Test
    public void getFacultyByIdTest() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.getFacultyById(1L)).isEqualTo(expected);
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void getFacultyByColorTest() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.getFacultyByColor("красный")).containsExactlyInAnyOrder(expected);
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void updateFaculty() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.updateFaculty(1L, new Faculty(1L, "Гриффиндор", "красный"))).isEqualTo(expected);
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void deleteFaculty() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.deleteFaculty(1L)).isEqualTo(expected)
                .isNotIn(facultyServiceImpl.getAll());
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount - 1);
    }
}

 */
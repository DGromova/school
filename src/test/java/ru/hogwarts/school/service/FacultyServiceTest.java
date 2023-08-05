/*package ru.hogwarts.school.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacultyServiceImplTest {

    @Mock
    FacultyRepository facultyRepositoryMock;

    @InjectMocks
    private FacultyServiceImpl out;

    @BeforeEach
    public void beforeEach() {

        Map<Long, Faculty> facultiesMap = new HashMap<>();

        Faculty f1 = new Faculty(1L, "Гриффиндор", "красный");
        Faculty f2 = new Faculty(2L, "Слизерин", "зелёный");
        Faculty f3 = new Faculty(3L, "Когтевран", "синий");

        facultiesMap.put(f1.getId(), f1);
        facultiesMap.put(f2.getId(), f2);
        facultiesMap.put(f3.getId(), f3);

    }


    @AfterEach
    public void afterEach() {
        facultyServiceImpl.getAll().forEach(f -> facultyServiceImpl.deleteFaculty(f.getId()));
    }

    @Test
    public void createFacultyTest() {
        Faculty expected = new Faculty(4L, "Пуффендуй", "жёлтый");

        when(facultyRepositoryMock.save(expected)).thenReturn(expected);

        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.createFaculty(new Faculty(4L, "Пуффендуй", "жёлтый")))
                .isEqualTo(expected)
                .isIn(facultyServiceImpl.getAll());
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount + 1);
    }

    @Test
    public void findFacultyByIdTest() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.findFacultyById(1L)).isEqualTo(expected);
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void findFacultyByColorTest() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.findFacultiesByColor("красный")).containsExactlyInAnyOrder(expected);
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void editFaculty() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyServiceImpl.getAll().size();

        assertThat(facultyServiceImpl.editFaculty(new Faculty(1L, "Гриффиндор", "красный"))).isEqualTo(expected);
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount);
    }

    @Test
    void deleteFaculty() {
        Faculty expected = new Faculty(1L, "Гриффиндор", "красный");
        int beforeCount = facultyServiceImpl.getAll().size();

        facultyServiceImpl.deleteFaculty(1L);
        assertThat(expected).isNotIn(facultyServiceImpl.getAll());
        assertThat(facultyServiceImpl.getAll().size()).isEqualTo(beforeCount - 1);
    }
}
*/

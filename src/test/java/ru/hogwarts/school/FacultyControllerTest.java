package ru.hogwarts.school;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    private final Faker faker = new Faker();

    @AfterEach
    public void clean() {
        facultyRepository.deleteAll();
    }

    @Test
    public FacultyDtoOut createTest() {
        FacultyDtoIn facultyDtoIn = generate();

        ResponseEntity<FacultyDtoOut> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/faculties",
                facultyDtoIn,
                FacultyDtoOut.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        FacultyDtoOut facultyDtoOut = responseEntity.getBody();

        assertThat(facultyDtoOut.getId()).isNotEqualTo(0L);
        assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
        assertThat(facultyDtoOut.getColor()).isEqualTo(facultyDtoIn.getColor());

        return facultyDtoOut;
    }

    @Test
    public void findTest() {
        FacultyDtoOut created = createTest();

        ResponseEntity<FacultyDtoOut> responseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + created.getId(),
                FacultyDtoOut.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        FacultyDtoOut facultyDtoOut = responseEntity.getBody();

        assertThat(facultyDtoOut.getId()).isEqualTo(created.getId());
        assertThat(facultyDtoOut.getName()).isEqualTo(created.getName());
        assertThat(facultyDtoOut.getColor()).isEqualTo(created.getColor());

        // checking not found
        long incorrectId = created.getId() + 1;

        ResponseEntity<String> notFoundResponseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + incorrectId,
                String.class
        );

        assertThat(notFoundResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateTest() {
        FacultyDtoOut created = createTest();
        FacultyDtoIn facultyDtoIn = new FacultyDtoIn();
        facultyDtoIn.setName(faker.name().fullName());
        facultyDtoIn.setColor(created.getColor());

        ResponseEntity<FacultyDtoOut> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties/" + created.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(facultyDtoIn),
                FacultyDtoOut.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        FacultyDtoOut facultyDtoOut = responseEntity.getBody();

        assertThat(facultyDtoOut.getId()).isEqualTo(created.getId());
        assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
        assertThat(facultyDtoOut.getColor()).isEqualTo(facultyDtoIn.getColor());

        // checking not found
        long incorrectId = created.getId() + 1;

        ResponseEntity<String> stringResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties/" + incorrectId,
                HttpMethod.PUT,
                new HttpEntity<>(facultyDtoIn),
                String.class
        );

        assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteTest() {
        FacultyDtoOut created = createTest();

        ResponseEntity<FacultyDtoOut> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties/" + created.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(created),
                FacultyDtoOut.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        FacultyDtoOut facultyDtoOut = responseEntity.getBody();

        assertThat(facultyDtoOut.getId()).isEqualTo(created.getId());
        assertThat(facultyDtoOut.getName()).isEqualTo(created.getName());
        assertThat(facultyDtoOut.getColor()).isEqualTo(created.getColor());
        assertThat(facultyDtoOut).isNotIn(facultyRepository);

        // checking not found
        long incorrectId = created.getId() + 1;

        ResponseEntity<String> notFoundResponseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/faculties/" + incorrectId,
                String.class
        );

        assertThat(notFoundResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findAllTest() {
        FacultyDtoOut created = createTest();
        FacultyDtoOut created2 = createTest();
        List<FacultyDtoOut> faculties = new ArrayList<>();
        faculties.add(created);
        faculties.add(created2);
        ResponseEntity<List> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties",
                HttpMethod.GET,
                new HttpEntity<>(faculties),
                List.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().size()).isEqualTo(faculties.size());
       //find by color

        String color = created.getColor();
        List<FacultyDtoOut> expected = faculties.stream()
                .filter(f-> f.getColor().equals(color)).toList();

        ResponseEntity<List> responseEntityByColor = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties?color=" + color,
                HttpMethod.GET,
                new HttpEntity<>(expected),
                List.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntityByColor.getBody().size()).isEqualTo(expected.size());

    }

    @Test
    public void findByNameOrColorTest() {
        FacultyDtoOut created = createTest();
        FacultyDtoOut created2 = createTest();
        String name = created.getName();
        String color = created2.getColor();
        List<FacultyDtoOut> faculties = new ArrayList<>();
        faculties.add(created);
        faculties.add(created2);
        List<FacultyDtoOut> expectedByName = faculties.stream()
                .filter(f -> f.getName().equals(name))
                .toList();

        ResponseEntity<List> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties/filter?nameOrColor=" + name,
                HttpMethod.GET,
                new HttpEntity<>(expectedByName),
                List.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<FacultyDtoOut> facultyByName = responseEntity.getBody();

        assertThat(facultyByName.size()).isEqualTo(expectedByName.size());
    }

    @Test
    public void findStudentsByFacultyIdTest() {
        FacultyDtoOut faculty = createTest();
        List<StudentDtoOut> students = generateStudents(10);
        students.get(0).setFaculty(faculty);
        students.get(1).setFaculty(faculty);
        long id = faculty.getId();

        List<StudentDtoOut> expected = new ArrayList<>();
        expected.add(students.get(0));
        expected.add(students.get(1));

        ResponseEntity<List> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/faculties/students?id=" + id,
                HttpMethod.GET,
                new HttpEntity<>(expected),
                List.class
        );

        List studentsByFacultyId = responseEntity.getBody();

        assertThat(studentsByFacultyId.size()).isEqualTo(expected.size());
        studentRepository.deleteAll();
    }

    public FacultyDtoIn generate() {
        FacultyDtoIn facultyDtoIn = new FacultyDtoIn();
        facultyDtoIn.setName(faker.harryPotter().house());
        facultyDtoIn.setColor(faker.color().name());
        return facultyDtoIn;
    }

    private StudentDtoIn generateStudent(long id) {
        StudentDtoIn student = new StudentDtoIn();
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(7, 18));
        return student;
    }

    private StudentDtoOut createStudent(long id) {
        StudentDtoIn studentDtoIn = generateStudent(id);

        StudentDtoOut studentDtoOut = testRestTemplate.postForObject(
                "http://localhost:" + port + "/students",
                studentDtoIn,
                StudentDtoOut.class
        );

        return studentDtoOut;
    }

    private List<StudentDtoOut> generateStudents(int size) {
        List<StudentDtoOut> students = Stream.iterate(1, id -> id + 1)
                .map(this::createStudent)
                .limit(size)
                .toList();
        return students;
    }

}

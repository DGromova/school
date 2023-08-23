package ru.hogwarts.school;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    private final Faker faker = new Faker();

    @AfterEach
    public void clean() {
        studentRepository.deleteAll();
    }

    @Test
    public StudentDtoOut createTest() {
        StudentDtoIn studentDtoIn = generate();

        ResponseEntity<StudentDtoOut> responseEntity = testRestTemplate.postForEntity(
                "http://localhost:" + port + "/students",
                studentDtoIn,
                StudentDtoOut.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDtoOut studentDtoOut = responseEntity.getBody();

        assertThat(studentDtoOut.getId()).isNotEqualTo(0L);
        assertThat(studentDtoOut.getName()).isEqualTo(studentDtoIn.getName());
        assertThat(studentDtoOut.getAge()).isEqualTo(studentDtoIn.getAge());

        return studentDtoOut;
    }

    @Test
    public void findTest() {
        StudentDtoOut created = createTest();

        ResponseEntity<StudentDtoOut> responseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + created.getId(),
                StudentDtoOut.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDtoOut studentDtoOut = responseEntity.getBody();

        assertThat(studentDtoOut.getId()).isEqualTo(created.getId());
        assertThat(studentDtoOut.getName()).isEqualTo(created.getName());
        assertThat(studentDtoOut.getAge()).isEqualTo(created.getAge());

        // checking not found
        long incorrectId = created.getId() + 1;

        ResponseEntity<String> notFoundResponseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + incorrectId,
                String.class
        );

        assertThat(notFoundResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateTest() {
        StudentDtoOut created = createTest();
        StudentDtoIn studentDtoIn = new StudentDtoIn();
        studentDtoIn.setName(faker.name().fullName());
        studentDtoIn.setAge(created.getAge());

        ResponseEntity<StudentDtoOut> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/students/" + created.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(studentDtoIn),
                StudentDtoOut.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDtoOut studentDtoOut = responseEntity.getBody();

        assertThat(studentDtoOut.getId()).isEqualTo(created.getId());
        assertThat(studentDtoOut.getName()).isEqualTo(studentDtoIn.getName());
        assertThat(studentDtoOut.getAge()).isEqualTo(studentDtoIn.getAge());

        // checking not found
        long incorrectId = created.getId() + 1;

        ResponseEntity<String> stringResponseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/students/" + incorrectId,
                HttpMethod.PUT,
                new HttpEntity<>(studentDtoIn),
                String.class
        );

        assertThat(stringResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteTest() {
        StudentDtoOut created = createTest();

        ResponseEntity<StudentDtoOut> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/students/" + created.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(created),
                StudentDtoOut.class
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        StudentDtoOut studentDtoOut = responseEntity.getBody();

        assertThat(studentDtoOut.getId()).isEqualTo(created.getId());
        assertThat(studentDtoOut.getName()).isEqualTo(created.getName());
        assertThat(studentDtoOut.getAge()).isEqualTo(created.getAge());
        assertThat(studentDtoOut).isNotIn(studentRepository);

        // checking not found
        long incorrectId = created.getId() + 1;

        ResponseEntity<String> notFoundResponseEntity = testRestTemplate.getForEntity(
                "http://localhost:" + port + "/students/" + incorrectId,
                String.class
        );

        assertThat(notFoundResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void findAllTest() {
        StudentDtoOut created = createTest();
        StudentDtoOut created2 = createTest();
        List<StudentDtoOut> students = new ArrayList<>();
        students.add(created);
        students.add(created2);
        ResponseEntity<List> responseEntity = testRestTemplate.exchange(
                "http://localhost:" + port + "/students",
                HttpMethod.GET,
                new HttpEntity<>(students),
                List.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().stream().toList().size()).isEqualTo(students.size());
        // find by age

        int age = created.getAge();
        List<StudentDtoOut> expected = students.stream()
                .filter(s -> Integer.valueOf(s.getAge()).equals(Integer.valueOf(age)))
                .toList();

        ResponseEntity<List> responseEntityByAge = testRestTemplate.exchange(
                "http://localhost:" + port + "/students?age=" + age,
                HttpMethod.GET,
                new HttpEntity<>(expected),
                List.class
        );
        assertThat(responseEntityByAge.getBody().size()).isEqualTo(expected.size());
    }

    @Test
    public void findByAgeBetweenTest() {
        StudentDtoOut created = createTest();
        StudentDtoOut created2 = createTest();
        StudentDtoOut created3 = createTest();

        List<StudentDtoOut> students = new ArrayList<>();

        students.add(created);
        students.add(created2);
        students.add(created3);
        students.get(0).setAge(7);
        students.get(1).setAge(8);
        students.get(2).setAge(9);

        int ageFrom = 6;
        int ageTo = 10;

        List expected = students.stream()
                .filter(s ->
                    s.getAge() >= ageFrom & s.getAge() < ageTo
                ).toList();

        ResponseEntity<List> responseEntity = testRestTemplate.exchange(
        "http://localhost:" + port + "/students/range?ageFrom=" + ageFrom + "&ageTo=" + ageTo,
                HttpMethod.GET,
                new HttpEntity<>(expected),
                List.class
        );
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(responseEntity.getBody().size()).isNotZero();
    }

    @Test
    public void findFacultyByStudentIdTest() {
        List<StudentDtoOut> students = new ArrayList<>();
        students.add(createTest());
        students.add(createTest());
        students.add(createTest());
        students.get(0).setFaculty(generateFaculty(1));
        FacultyDtoOut expected = students.get(0).getFaculty();
        long id = expected.getId();

        FacultyDtoOut facultyDtoOut = testRestTemplate.getForObject(
                "http://localhost:" + port + "/students/{id}/faculty",
                FacultyDtoOut.class,
                id
        );

        assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
        assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
        assertThat(facultyDtoOut.getColor()).isEqualTo(expected.getColor());

        facultyRepository.deleteAll();
    }

    public StudentDtoIn generate() {
        StudentDtoIn studentDtoIn = new StudentDtoIn();
        studentDtoIn.setName(faker.harryPotter().character());
        studentDtoIn.setAge(faker.random().nextInt(7, 18));
        return studentDtoIn;
    }

    private FacultyDtoOut generateFaculty(long id) {
        FacultyDtoOut facultyDtoOut = new FacultyDtoOut();
        facultyDtoOut.setId(id);
        facultyDtoOut.setName(faker.harryPotter().house());
        facultyDtoOut.setColor(faker.color().name());
        return new FacultyDtoOut();
    }

}

package ru.hogwarts.school;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.dto.FacultyDtoIn;
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

//@WebMvcTest(controllers = FacultyController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FacultyControllerWithMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

//    @SpyBean
//    private FacultyService facultyService;
//
//    @SpyBean
//    private FacultyMapper facultyMapper;
//
//    @SpyBean
//    private StudentMapper studentMapper;

    @Autowired
    private FacultyMapper facultyMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void createTest() throws Exception {
        FacultyDtoIn facultyDtoIn = generateDto();
        Faculty faculty = generate(1);
        faculty.setName(facultyDtoIn.getName());
        faculty.setColor(facultyDtoIn.getColor());
        when(facultyRepository.save(any())).thenReturn(faculty);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facultyDtoIn))
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(facultyDtoIn.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
                });
        verify(facultyRepository, new Times(1)).save(any());
    }

    @Test
    public void updateTest() throws Exception {
        FacultyDtoIn facultyDtoIn = generateDto();

        Faculty oldFaculty = generate(1);
        when(facultyRepository.findById(eq(Long.valueOf(1L)))).thenReturn(Optional.of(oldFaculty));

        oldFaculty.setColor(facultyDtoIn.getColor());
        oldFaculty.setName(facultyDtoIn.getName());

        when(facultyRepository.save(any())).thenReturn(oldFaculty);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/faculties/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(facultyDtoIn))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(facultyDtoIn.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(facultyDtoIn.getName());
                });
        verify(facultyRepository, Mockito.times(1)).save(any());
        Mockito.reset(facultyRepository);

        // not found checking

        when(facultyRepository.findById(eq(Long.valueOf(2L)))).thenReturn(Optional.empty());

        mockMvc.perform(
                MockMvcRequestBuilders.put("/faculties/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facultyDtoIn))
        ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(facultyRepository, never()).save(any());
    }

    @Test
    public void findTest() throws Exception {
        Faculty faculty = generate(1);

        when(facultyRepository.findById(eq(Long.valueOf(1L)))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculties/1")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(faculty.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(faculty.getName());
                });

        // not found checking

        when(facultyRepository.findById(eq(Long.valueOf(2L)))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculties/2")
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(facultyRepository, never()).save(any());
        verify(facultyRepository, never()).delete(any());
    }

    @Test
    public void deleteTest() throws Exception {
        Faculty faculty = generate(1);

        when(facultyRepository.findById(eq(Long.valueOf(1L)))).thenReturn(Optional.of(faculty));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculties/1")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(faculty.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(faculty.getName());
                });
        verify(facultyRepository, Mockito.times(1)).delete(any());
        Mockito.reset(facultyRepository);

        // not found checking

        when(facultyRepository.findById(eq(Long.valueOf(2L)))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/faculties/2")
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(facultyRepository, never()).delete(any());
    }

    @Test
    public void findAllTest() throws Exception {
        List<Faculty> faculties = Stream.iterate(1, id -> id + 1)
                .map(this::generate)
                .limit(15)
                .toList();
        List<FacultyDtoOut> expectedResult = faculties.stream()
                .map(facultyMapper::toDto)
                .toList();

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculties")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<FacultyDtoOut> facultyDtoOuts = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(facultyDtoOuts)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(facultyDtoOuts.size())
                            .forEach(index -> {
                                FacultyDtoOut facultyDtoOut = facultyDtoOuts.get(index);
                                FacultyDtoOut expected = expectedResult.get(index);
                                assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                assertThat(facultyDtoOut.getColor()).isEqualTo(expected.getColor());
                                assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                            });
                });

        String color = faculties.get(0).getColor();
        faculties = faculties.stream()
                .filter(faculty -> faculty.getColor().equals(color))
                        .collect(Collectors.toList());
        List<FacultyDtoOut> expectedResult2 = faculties.stream()
                .map(facultyMapper::toDto)
                .toList();
        when(facultyRepository.findAllByColorIgnoreCase(eq(color))).thenReturn(faculties);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/faculties?color={color}", color)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<FacultyDtoOut> facultyDtoOuts = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(facultyDtoOuts)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(facultyDtoOuts.size())
                            .forEach(index -> {
                                FacultyDtoOut facultyDtoOut = facultyDtoOuts.get(index);
                                FacultyDtoOut expected = expectedResult2.get(index);
                                assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                                assertThat(facultyDtoOut.getColor()).isEqualTo(expected.getColor());
                            });
                });
    }

    @Test
    public void findByNameOrColorTest() throws Exception {
        List<Faculty> faculties = Stream.iterate(1, id -> id + 1)
                .map(this::generate)
                .limit(15)
                .toList();

        // check by color
        String color = faculties.get(0).getColor();
        faculties = faculties.stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
        List<FacultyDtoOut> expectedResult = faculties.stream()
                .map(facultyMapper::toDto)
                .toList();

        when(facultyRepository.findAllByNameContainingIgnoreCaseOrColorContainingIgnoreCase(eq(color), eq(color)))
                .thenReturn(faculties);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculties/filter?nameOrColor={color}", color)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<FacultyDtoOut> facultyDtoOuts = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(facultyDtoOuts)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(facultyDtoOuts.size())
                            .forEach(index -> {
                                FacultyDtoOut facultyDtoOut = facultyDtoOuts.get(index);
                                FacultyDtoOut expected = expectedResult.get(index);
                                assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                assertThat(facultyDtoOut.getColor()).isEqualTo(expected.getColor());
                                assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                            });
                });

        // check by name
        String name = faculties.get(0).getName();
        faculties = faculties.stream()
                .filter(faculty -> faculty.getName().equals(name))
                .collect(Collectors.toList());
        List<FacultyDtoOut> expectedResult2 = faculties.stream()
                .map(facultyMapper::toDto)
                .toList();

        when(facultyRepository.findAllByNameContainingIgnoreCaseOrColorContainingIgnoreCase(eq(name), eq(name)))
                .thenReturn(faculties);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculties/filter?nameOrColor={name}", name)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<FacultyDtoOut> facultyDtoOuts = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(facultyDtoOuts)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(facultyDtoOuts.size())
                            .forEach(index -> {
                                FacultyDtoOut facultyDtoOut = facultyDtoOuts.get(index);
                                FacultyDtoOut expected = expectedResult2.get(index);
                                assertThat(facultyDtoOut.getId()).isEqualTo(expected.getId());
                                assertThat(facultyDtoOut.getColor()).isEqualTo(expected.getColor());
                                assertThat(facultyDtoOut.getName()).isEqualTo(expected.getName());
                            });
                });
    }

    @Test
    public void findStudentsByFacultyIdTest() throws Exception {
        List<Student> students = Stream.iterate(1, id -> id +1)
                .map(this::generateStudent)
                .limit(5)
                .toList();

        Faculty faculty = students.get(1).getFaculty();
        long id = faculty.getId();

        students = students.stream()
                .filter(student -> student.getFaculty().equals(faculty))
                .collect(Collectors.toList());

        List<StudentDtoOut> expectedResult = students.stream()
                .map(studentMapper::toDto)
                .toList();

        when(studentRepository.findAllByFaculty_Id(anyLong())).thenReturn(students);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/faculties/students?id={id}", eq(id))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    List<StudentDtoOut> studentDtoOuts = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<>() {
                            }
                    );
                    assertThat(studentDtoOuts)
                            .isNotNull()
                            .isNotEmpty();
                    Stream.iterate(0, index -> index + 1)
                            .limit(studentDtoOuts.size())
                            .forEach(index -> {
                                StudentDtoOut studentDtoOut = studentDtoOuts.get(index);
                                StudentDtoOut expected = expectedResult.get(index);
                                assertThat(studentDtoOut.getId()).isEqualTo(expected.getId());
                                assertThat(studentDtoOut.getAge()).isEqualTo(expected.getAge());
                                assertThat(studentDtoOut.getName()).isEqualTo(expected.getName());
                                assertThat(studentDtoOut.getFaculty()).isEqualTo(expected.getFaculty());

                            });
                });

    }

    private FacultyDtoIn generateDto() {
        FacultyDtoIn facultyDtoIn = new FacultyDtoIn();
        facultyDtoIn.setName(faker.harryPotter().house());
        facultyDtoIn.setColor(faker.color().name());
        return facultyDtoIn;
    }

    private Faculty generate(long id) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return faculty;
    }

    private Student generateStudent(long id) {
        Faculty faculty = generate(1);
        Student student = new Student();
        student.setId(id);
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(7, 18));
        student.setFaculty(faculty);
        return student;
    }

}

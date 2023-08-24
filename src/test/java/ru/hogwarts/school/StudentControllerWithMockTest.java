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
import ru.hogwarts.school.dto.FacultyDtoOut;
import ru.hogwarts.school.dto.StudentDtoIn;
import ru.hogwarts.school.dto.StudentDtoOut;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.mapper.FacultyMapper;
import ru.hogwarts.school.mapper.StudentMapper;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerWithMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private FacultyMapper facultyMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private final Faker faker = new Faker();

    @Test
    public void createTest() throws Exception {
        StudentDtoIn studentDtoIn = generateDto();
        Student student = generate(1);
        student.setName(studentDtoIn.getName());
        student.setAge(studentDtoIn.getAge());
        when(studentRepository.save(any())).thenReturn(student);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(studentDtoIn))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    StudentDtoOut studentDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            StudentDtoOut.class
                    );
                    assertThat(studentDtoOut).isNotNull();
                    assertThat(studentDtoOut.getId()).isEqualTo(1L);
                    assertThat(studentDtoOut.getAge()).isEqualTo(studentDtoIn.getAge());
                    assertThat(studentDtoOut.getName()).isEqualTo(studentDtoIn.getName());
                });
        verify(studentRepository, new Times(1)).save(any());
    }

    @Test
    public void updateTest() throws Exception {
        StudentDtoIn studentDtoIn = generateDto();

        Student oldStudent = generate(1);
        when(studentRepository.findById(eq(1L))).thenReturn(Optional.of(oldStudent));

        oldStudent.setAge(studentDtoIn.getAge());
        oldStudent.setName(studentDtoIn.getName());

        when(studentRepository.save(any())).thenReturn(oldStudent);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/students/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(studentDtoIn))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    StudentDtoOut studentDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            StudentDtoOut.class
                    );
                    assertThat(studentDtoOut).isNotNull();
                    assertThat(studentDtoOut.getId()).usingRecursiveComparison().isEqualTo(1L);
                    assertThat(studentDtoOut.getAge()).usingRecursiveComparison().isEqualTo(studentDtoIn.getAge());
                    assertThat(studentDtoOut.getName()).isEqualTo(studentDtoIn.getName());
                });
        verify(studentRepository, Mockito.times(1)).save(any());
        Mockito.reset(studentRepository);

        // not found checking

        when(studentRepository.findById(eq(Long.valueOf(2L)))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/students/2")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(studentDtoIn))
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(studentRepository, never()).save(any());
    }

    @Test
    public void findTest() throws Exception {
        Student student = generate(1);

        when(studentRepository.findById(eq(1L))).thenReturn(Optional.of(student));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/students/1")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    StudentDtoOut studentDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            StudentDtoOut.class
                    );
                    assertThat(studentDtoOut).isNotNull();
                    assertThat(studentDtoOut.getId()).isEqualTo(student.getId());
                    assertThat(studentDtoOut.getAge()).isEqualTo(student.getAge());
                    assertThat(studentDtoOut.getName()).isEqualTo(student.getName());
                });

        // not found checking

        when(studentRepository.findById(eq(Long.valueOf(2L)))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/students/2")
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(studentRepository, never()).save(any());
        verify(studentRepository, never()).delete(any());
    }

    @Test
    public void deleteTest() throws Exception {
        Student student = generate(1);

        when(studentRepository.findById(eq(1L))).thenReturn(Optional.of(student));

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/students/1")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    StudentDtoOut studentDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            StudentDtoOut.class
                    );
                    assertThat(studentDtoOut).isNotNull();
                    assertThat(studentDtoOut.getId()).isEqualTo(1L);
                    assertThat(studentDtoOut.getAge()).isEqualTo(student.getAge());
                    assertThat(studentDtoOut.getName()).isEqualTo(student.getName());
                });
        verify(studentRepository, Mockito.times(1)).delete(any());
        Mockito.reset(studentRepository);

        // not found checking

        when(studentRepository.findById(eq(Long.valueOf(2L)))).thenReturn(Optional.empty());

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/students/2")
                ).andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(result -> {
                    String responseString = result.getResponse().getContentAsString();
                    assertThat(responseString).isNotNull();
                });
        verify(studentRepository, never()).delete(any());
    }

    @Test
    public void findAllTest() throws Exception {
        List<Student> students = Stream.iterate(1, id -> id + 1)
                .map(this::generate)
                .limit(15)
                .toList();
        List<StudentDtoOut> expectedResult = students.stream()
                .map(studentMapper::toDto)
                .toList();

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/students")
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
                            });
                });

    }

    @Test
    public void findByAgeBetween() throws Exception {
        List<Student> students = Stream.iterate(1, id -> id + 1)
                .map(this::generate)
                .limit(3)
                .toList();
        students.get(0).setAge(19);
        students.get(1).setAge(22);
        students.get(2).setAge(24);

        int ageFrom = 19;
        int ageTo = 24;

        List<StudentDtoOut> expectedResult = students.stream()
                .map(studentMapper::toDto)
                .toList();


        when(studentRepository.findAllByAgeBetween(eq(ageFrom), eq(ageTo)))
                .thenReturn(students);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/students/range?ageFrom={ageFrom}&ageTo={ageTo}", ageFrom, ageTo)
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
                            });
                });

    }

    @Test
    public void findFacultyByStudentIdTest() throws Exception {
        Student student = generate(1);
        Faculty faculty = generateFaculty(1);
        student.setFaculty(faculty);
        long id = student.getId();

        FacultyDtoOut expectedResult = facultyMapper.toDto(faculty);

        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/students/{id}/faculty", eq(id))
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(result -> {
                    FacultyDtoOut facultyDtoOut = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            FacultyDtoOut.class
                    );
                    assertThat(facultyDtoOut).isNotNull();
                    assertThat(facultyDtoOut.getId()).usingRecursiveComparison().isEqualTo(1L);
                    assertThat(facultyDtoOut.getColor()).isEqualTo(expectedResult.getColor());
                    assertThat(facultyDtoOut.getName()).isEqualTo(expectedResult.getName());
                });
    }

    @Test
    public void uploadAvatarTest() throws Exception {
    }

    private StudentDtoIn generateDto() {
        StudentDtoIn studentDtoIn = new StudentDtoIn();
        studentDtoIn.setName(faker.harryPotter().character());
        studentDtoIn.setAge(faker.random().nextInt(7, 18));
        return studentDtoIn;
    }

    private Student generate(long id) {
        Student student = new Student();
        student.setId(id);
        student.setName(faker.harryPotter().character());
        student.setAge(faker.random().nextInt(7, 18));
        return student;
    }

    private Faculty generateFaculty(long id) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(faker.harryPotter().house());
        faculty.setColor(faker.color().name());
        return faculty;
    }

}
package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private Map<Long, Student> students = new HashMap<>();

    private Long generatedStudentId = 1L;

    public Student createStudent(Student student) {
        student.setId(generatedStudentId);
        students. put(generatedStudentId, student);
        generatedStudentId++;
        return student;
    }

    public Student getStudentById(Long studentId) {
        return students.get(studentId);
    }

    public List<Student> getStudentsByAge(int studentsAge) {
        return students.values().stream()
                .filter(student -> studentsAge == student.getAge()).collect(Collectors.toUnmodifiableList());
    }

    public List<Student> getAll() {
        return students.values().stream().collect(Collectors.toUnmodifiableList());
    }

    public Student updateStudent(Long studentId, Student student) {
        if(students.containsKey(studentId)) {
            students.put(studentId, student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(Long studentId) {
        return students.remove(studentId);
    }

}

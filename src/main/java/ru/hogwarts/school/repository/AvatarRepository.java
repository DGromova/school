package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

   Optional<Avatar> findByStudent_Id(Long id);

   //Optional<Avatar> findAvatar(Long studentId);
   Optional< Avatar> findAvatarByStudent_Id(long studentId);

}

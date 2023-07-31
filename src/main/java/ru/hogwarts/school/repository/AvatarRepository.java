package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

   Optional<Avatar> findById(long id);

   //Optional<Avatar> findAvatar(Long studentId);
   Optional< Avatar> findAvatarByStudent_Id(long studentId);

}

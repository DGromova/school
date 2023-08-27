package ru.hogwarts.school.controller;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.service.AvatarService;

import java.util.List;

@RestController
@RequestMapping("/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping ("/{id}/from-db")
    public ResponseEntity<byte[]> getFromDb(@PathVariable long id) {
        return build(avatarService.getFromDb(id));
    }

    @GetMapping ("/{id}/from-fs")
    public ResponseEntity<byte[]> getFromFs(@PathVariable long id) {
        return build(avatarService.getFromFs(id));
    }

    @GetMapping
    public List<AvatarDto> page(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                @RequestParam(value = "size", required = false, defaultValue = "5") int size) {
        return avatarService.getPage(Math.abs(page), Math.abs(size));
    }

    private ResponseEntity<byte[]> build(Pair<byte[], String> pair) {
        byte[] data = pair.getKey();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(pair.getValue()))
                .contentLength(data.length)
                .body(data);
    }
}


package org.anuen.nurse.controller;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.anuen.nurse.entity.dto.AddNurseDto;
import org.anuen.nurse.service.INurseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nurse")
@RequiredArgsConstructor
public class NurseController {

    private final INurseService nurseService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody AddNurseDto nurseDto) {
        return nurseService.add(nurseDto);
    }

    @GetMapping("/fetchSuggestionsByName")
    public ResponseEntity<?> fetchSuggestionsByName(@RequestParam("name") String name) {
        if (StrUtil.isBlank(name)) {
            return ResponseEntity.fail(ResponseStatus.PARAM_LOSE);
        }
        return nurseService.fetchSuggestionsByName(name);
    }

    @GetMapping("/scheduling")
    public ResponseEntity<?> scheduling() {
        return nurseService.scheduling();
    }
}

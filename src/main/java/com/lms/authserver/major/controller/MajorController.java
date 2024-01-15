package com.lms.authserver.major.controller;


import com.lms.authserver.global.response.LmsResponse;
import com.lms.authserver.major.entity.Major;
import com.lms.authserver.major.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/major")
@RequiredArgsConstructor

public class MajorController {

    private final MajorService majorService;

    @GetMapping
    public LmsResponse<List<Major>> getAll(){
        List<Major> all = majorService.getAll();
        return new LmsResponse<>(HttpStatus.OK, all, "서비스 성공", "", LocalDateTime.now());
    }

    @PostMapping("/id")
    public LmsResponse<Long> getId(@RequestParam String name){
        Long id = majorService.getId(name);
        return new LmsResponse<>(HttpStatus.OK, id, "서비스 성공", "", LocalDateTime.now());
    }

}

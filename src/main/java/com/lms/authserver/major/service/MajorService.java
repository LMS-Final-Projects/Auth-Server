package com.lms.authserver.major.service;

import com.lms.authserver.major.entity.Major;
import com.lms.authserver.major.repository.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;

    @Transactional
    public List<Major> getAll() {
        List<Major> all = majorRepository.findAll();
        return  all;
    }

    @Transactional
    public Long getId(String majorName){
        Long idByMajorName = majorRepository.findIdByMajorName(majorName);
        return idByMajorName;
    }
}

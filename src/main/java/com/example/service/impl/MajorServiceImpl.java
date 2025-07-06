package com.example.service.impl;

import com.example.entity.Major;
import com.example.repository.MajorRepository;
import com.example.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {
    @Autowired
    private MajorRepository majorRepository;

    @Override
    public List<Major> findAll(){
        return majorRepository.findAll();
    }

    @Override
    public List<Major> findByMajorIdIn(List<Long> ids) {
        return majorRepository.findByMajorIdIn(ids);
    }
}

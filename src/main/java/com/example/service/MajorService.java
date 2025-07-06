package com.example.service;

import com.example.entity.Major;

import java.util.List;

public interface MajorService {
    List<Major> findAll();
    List<Major> findByMajorIdIn(List<Long> ids);

}

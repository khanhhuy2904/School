package com.example.repository;

import com.example.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Long> {
    List<Major> findByMajorIdIn(List<Long> ids);
}

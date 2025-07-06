package com.example.repository;

import com.example.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankRepository extends JpaRepository<Rank, Long> {
    List<Rank> findByRankIdIn(List<Long> ids);
}

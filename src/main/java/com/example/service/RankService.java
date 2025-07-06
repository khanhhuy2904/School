package com.example.service;

import com.example.entity.Rank;

import java.util.List;

public interface RankService {
    List<Rank> findAll();
    List<Rank> findByRankIdIn(List<Long> ids);

}

package com.example.service.impl;

import com.example.entity.Major;
import com.example.entity.Rank;
import com.example.repository.RankRepository;
import com.example.service.RankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankServiceImpl implements RankService {
    @Autowired
    private RankRepository rankRepository;

    @Override
    public List<Rank> findAll(){
        return rankRepository.findAll();
    }

    @Override
    public List<Rank> findByRankIdIn(List<Long> ids) {
        return rankRepository.findByRankIdIn(ids);
    }
}

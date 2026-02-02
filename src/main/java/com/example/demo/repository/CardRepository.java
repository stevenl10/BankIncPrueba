package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Card;

public interface CardRepository extends JpaRepository<Card, String>{

}

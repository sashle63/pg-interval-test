package com.example.pgintervaltest.repository;

import com.example.pgintervaltest.model.Tester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TesterRepository extends JpaRepository<Tester, Integer> {


}

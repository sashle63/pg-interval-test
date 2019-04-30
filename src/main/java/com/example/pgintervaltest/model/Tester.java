package com.example.pgintervaltest.model;

import org.hibernate.annotations.Type;
import org.postgresql.util.PGInterval;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Table(name = "tb_testinterval")
public class Tester {

    @Id
    @Column(name = "test_id", nullable = false)
    private Integer testId;

    @Column(name = "test_interval", nullable = false)
    private PGInterval testInterval;

    public Integer getTestId() { return testId; }
    public void setTestId(Integer testId) { this.testId = testId;}

    public PGInterval getTestInterval() { return testInterval; }
    public void setTestInterval(PGInterval testInterval) { this.testInterval = testInterval; }
}
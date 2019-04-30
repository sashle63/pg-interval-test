package com.example.pgintervaltest.controller;

import com.example.pgintervaltest.exception.ResourceNotFoundException;
import com.example.pgintervaltest.model.Tester;
import com.example.pgintervaltest.repository.TesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TesterController {
    @Autowired
    private TesterRepository testerRepository;

    @GetMapping("/testrecords")
    public List<Tester> getAllCategories() {
        return testerRepository.findAll();
    }

    @GetMapping("/testrecords/{id}")
    public ResponseEntity<Tester> getTesterById(@PathVariable(value = "id") Integer testerId)
            throws ResourceNotFoundException {
        Tester tester = testerRepository.findById(testerId)
                .orElseThrow(() -> new ResourceNotFoundException("Tester not found for this id :: " + testerId));
        return ResponseEntity.ok().body(tester);
    }

}

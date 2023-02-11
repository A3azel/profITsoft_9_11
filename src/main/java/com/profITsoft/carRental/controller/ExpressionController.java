package com.profITsoft.carRental.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/math", produces="application/json")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpressionController {

    private static final List<String> EXPRESSION_LIST = List.of( "2 + 2", "2 / 0", "6 - 7", "42 * 42", "5 * 5");

    @GetMapping("/expamples")
    public ResponseEntity<List<String>> getExpressions(@RequestParam(value = "count") int count){
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            resultList.add(EXPRESSION_LIST.get(i));
        }
        return ResponseEntity.ok(resultList);
    }

    /*@GetMapping("/expamples")
    public ResponseEntity<String> getExpressions(@RequestParam(value = "count") int count){

        return ResponseEntity.ok("resultList");
    }*/
}

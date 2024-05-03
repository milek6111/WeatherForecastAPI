package com.example.weatherapibackend.controller;

import com.example.weatherapibackend.service.WeatherService;
import io.micrometer.common.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


//test url
//http://localhost:8080/api/v1/forecast?latitude=1.02&longitude=23.55
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class WeatherController {

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast")
    ResponseEntity<?> getForecast(@NonNull @RequestParam Double latitude,@NonNull @RequestParam Double longitude){
        if(latitude < -90 || latitude > 90){
            Map<String,String> info = new HashMap<>();
            info.put("error","Latitude must be between -90 and 90");
            return new ResponseEntity<>(info,HttpStatus.BAD_REQUEST);
        }

        if(longitude < -180 || longitude > 180){
            Map<String,String> info = new HashMap<>();
            info.put("error","Longitude must be between -180 and 180");
            return new ResponseEntity<>(info,HttpStatus.BAD_REQUEST);
        }

        var result = weatherService.fetchData(latitude,longitude);

        if(result == null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}

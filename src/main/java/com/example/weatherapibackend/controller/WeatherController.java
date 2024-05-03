package com.example.weatherapibackend.controller;

import com.example.weatherapibackend.service.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Map;

//test url
//http://localhost:8080/api/v1/forecast?latitude=1.02&longitude=23.55
@RestController
@RequestMapping("/api/v1")
public class WeatherController {

    private WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    @GetMapping("/forecast")
    ResponseEntity<?> getForecast(@RequestParam Double latitude,@RequestParam Double longitude){
        if(latitude < -90 || latitude > 90){
            return new ResponseEntity<>("Latitude must be between -90 and 90",HttpStatus.valueOf(400));
        }

        if(longitude < -180 || longitude > 180){
            return new ResponseEntity<>("Longitude must be between -180 and 180",HttpStatus.valueOf(400));
        }

        var result = weatherService.fetchData(latitude,longitude);

        if(result == null)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);


        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}

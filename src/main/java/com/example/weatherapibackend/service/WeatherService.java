package com.example.weatherapibackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.objenesis.ObjenesisHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    public Map<String,Object> fetchData(Double latitude, Double longitude){
        try {
            var client = HttpClient.newHttpClient();
            var request =  HttpRequest
                    .newBuilder()
                    .uri(new URI("https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&daily=weather_code,temperature_2m_max,temperature_2m_min,sunshine_duration"))
                    .GET()
                    .build();

            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200)
                return null;

            ObjectMapper mapper  = new ObjectMapper();
            Map<String,Object> map = mapper.readValue(response.body(), Map.class);

            return calculateEnergy(map);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String,Object> calculateEnergy(Map<String,Object> data){
        //List of parameters returned from API
        List<String> parameters = List.of("time", "weather_code", "temperature_2m_max", "temperature_2m_min");

        Map<String, Object> result = new HashMap<>();
        Map<String, List<Object>> daily = (Map<String,List<Object>>)data.get("daily");


        for(Map.Entry<String,List<Object>> entry : daily.entrySet()){
            if(parameters.contains(entry.getKey())){
                result.put(entry.getKey(), entry.getValue());
            }
        }

        List<Object> energyValues = new ArrayList<>();

        final double power = 2.5;
        final double efficiency = 0.2;
        for(var duration : daily.get("sunshine_duration")){
            double generated_energy = power * (Double) duration * efficiency;
            energyValues.add(generated_energy);
        }

        result.put("generated_energy",energyValues);


        return result;
    }

}

package org.vaccine.finder.vaccinefinder.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.vaccine.finder.vaccinefinder.model.Session;
import org.vaccine.finder.vaccinefinder.model.Sessions;

import static util.Constants.*;

@Repository
public class CallSetuApiImpl implements CallSetuAPI{
    RestTemplate restTemplate;
    ObjectMapper objectMapper;
    @Autowired
    CallSetuApiImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Sessions getVaccineData(String disCode, String date) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam(DISTRICT_ID,disCode)
                    .queryParam(DATE,date);
            HttpEntity<?> entity = new HttpEntity<>(headers);
            HttpEntity<Sessions> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    Sessions.class);
            //System.out.println("response.getBody().toString() = " + response.getBody());
            return response.getBody();

        }catch (Exception e) {e.printStackTrace(); return null;}


    }
}

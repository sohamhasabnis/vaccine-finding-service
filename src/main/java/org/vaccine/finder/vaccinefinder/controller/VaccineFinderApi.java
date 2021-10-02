package org.vaccine.finder.vaccinefinder.controller;

import io.swagger.annotations.Api;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(value = "vaccine")
@RequestMapping(value = "/")
public interface VaccineFinderApi {
    @GetMapping(value = "/vaccine")
    ResponseEntity<String> vaccineFinder();
}

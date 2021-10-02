package org.vaccine.finder.vaccinefinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import org.vaccine.finder.vaccinefinder.service.VaccineAPICallService;
import util.TelegramBotAPI;

@RestController
public class VaccineFinderApiImpl implements VaccineFinderApi {

    VaccineAPICallService vaccineAPICallService;
    TelegramBotAPI telegramBotAPI = new TelegramBotAPI();
    @Autowired
    public VaccineFinderApiImpl(VaccineAPICallService vaccineAPICallService) {
        this.vaccineAPICallService = vaccineAPICallService;

    }

    @Override
    @Scheduled(cron = "0 0/1 * * * *")
    public ResponseEntity<String> vaccineFinder() {
        String data = vaccineAPICallService.get18PlusVaccine().toString();
        telegramBotAPI.GetData(vaccineAPICallService.get18PlusVaccine());
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}

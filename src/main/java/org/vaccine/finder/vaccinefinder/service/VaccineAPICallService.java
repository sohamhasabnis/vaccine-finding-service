package org.vaccine.finder.vaccinefinder.service;

import org.vaccine.finder.vaccinefinder.model.CenterResponse;

import java.util.List;

public interface VaccineAPICallService {
    public  List<List<CenterResponse>> get18PlusVaccine();
    public Object get45PlusVaccine();
}

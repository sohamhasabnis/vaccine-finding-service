package org.vaccine.finder.vaccinefinder.repository;

import org.vaccine.finder.vaccinefinder.model.Sessions;

public interface CallSetuAPI {
    Sessions getVaccineData(String pin, String date);
}

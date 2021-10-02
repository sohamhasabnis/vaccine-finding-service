package org.vaccine.finder.vaccinefinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.vaccine.finder.vaccinefinder.config.ConfigPropertyLoader;
import org.vaccine.finder.vaccinefinder.model.CenterResponse;
import org.vaccine.finder.vaccinefinder.model.Session;
import org.vaccine.finder.vaccinefinder.model.Sessions;
import org.vaccine.finder.vaccinefinder.repository.CallSetuAPI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class VaccineAPICallServiceIImpl implements VaccineAPICallService {
    CallSetuAPI callSetuAPI;
    private final ConfigPropertyLoader configPropertyLoader;
    List<org.vaccine.finder.vaccinefinder.model.CenterResponse> time_check = new ArrayList<>();

    @Autowired
    VaccineAPICallServiceIImpl(CallSetuAPI callSetuAPI, ConfigPropertyLoader configPropertyLoader) {
        this.callSetuAPI = callSetuAPI;
        this.configPropertyLoader = configPropertyLoader;
    }

    @Override
    public List<List<org.vaccine.finder.vaccinefinder.model.CenterResponse>> get18PlusVaccine() {
        String[] districtCode = configPropertyLoader.getPropertiesFile("districtCode").split(",");
        for (int i = 0; i < districtCode.length; i++) {
            System.out.println("districtCode[" + i + "] = " + districtCode[i]);
        }
        List<List<org.vaccine.finder.vaccinefinder.model.CenterResponse>> data = new ArrayList<>();
        Arrays.stream(districtCode).forEach(code -> {
            System.out.println("code ");
            data.add(getVaccineDetails(code));
        });
        return data;
    }

    @Override
    public Object get45PlusVaccine() {
        return null;
    }

    public List<org.vaccine.finder.vaccinefinder.model.CenterResponse> getVaccineDetails(String districtCode) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-YYYY");
        List<String> dateList = new ArrayList<>();
        dateList.add(dtf.format(LocalDate.now()));
        dateList.add(dtf.format(LocalDate.now().plusDays(1)));
        //dtf.format(date)
        List<org.vaccine.finder.vaccinefinder.model.CenterResponse> dataList = new ArrayList<>();
        for (String date : dateList) {
            Sessions sessions = callSetuAPI.getVaccineData(districtCode, date);

            // org.vaccine.finder.vaccinefinder.model.CenterResponse centerResponse = new CenterResponse();
            for (Session session : sessions.getSessions()) {
                if (session.getAvailableCapacityDose2().intValue() > 0 && session.getVaccine().equalsIgnoreCase("covaxin")) {
                    org.vaccine.finder.vaccinefinder.model.CenterResponse centerResponse = new CenterResponse();
                    centerResponse.centerName(session.getName());
                    centerResponse.address(session.getAddress());
                    centerResponse.districtName(session.getDistrictName());
                    centerResponse.capacity(session.getAvailableCapacityDose1().intValue());
                    centerResponse.pincode(session.getPincode());
                    centerResponse.slots(session.getSlots());
                    centerResponse.setTotalDoses(session.getAvailableCapacity().intValue());
                    centerResponse.setDateForDose(session.getDate());
                    centerResponse.setTypeOfVaccine(session.getVaccine());
                    centerResponse.setAge(session.getMinAgeLimit().intValue());
                    centerResponse.setTotalSecondDoses(session.getAvailableCapacityDose2().intValue());
                    if (session.getFeeType() != null && "paid".equalsIgnoreCase(session.getFeeType().getValue())) {
                        centerResponse.setFee(session.getFee());
                    } else {
                        centerResponse.setFee("Free");
                    }
                    dataList.add(centerResponse);

                }

            }
        }
        System.out.println("dataList Data = " + dataList + "District Code " + districtCode);
        return dataList;
    }

    @Scheduled(cron = "0 0/2 * * * *")
    private void clearDataFromTheCacheList() {
        System.out.println("Time check " + time_check);
        this.time_check.clear();
    }
}

package util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.vaccine.finder.vaccinefinder.model.CenterResponse;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static util.Constants.CHAT_ID;
import static util.Constants.TOKEN;

public class TelegramBotAPI {
    public String message;

    public void GetData(List<List<CenterResponse>> listData) {
        System.out.println("listData = ");
        listData.forEach(list -> {
            list.forEach(centerResponse -> {
                String abc = centerResponse.getFee() == null ?"Free":centerResponse.getFee();
                message = "Vaccine Available at: "+ centerResponse.getCenterName()+"\n"
                        +" Address for the same: "+ centerResponse.getAddress()+"\n"
                        +" District: "+centerResponse.getDistrictName()+ " Pincode "+centerResponse.getPincode()+"\n"
                        +" Age Group: "+centerResponse.getAge()+" + \n"
                        +" Total doses: "+centerResponse.getTotalDoses()+"\n"
                        +" Available for 1st dose: "+centerResponse.getCapacity()+ "\n"
                        +" Available for 2st dose: "+centerResponse.getTotalSecondDoses()+ "\n"
                        +" Vaccine Cost: "+centerResponse.getFee()+ "\n"
                        +" Vaccine: "+centerResponse.getTypeOfVaccine()+"\n"
                        +" Date for booking: "+centerResponse.getDateForDose()+" onwards\n"
                        +" Slots for the same are\n"+centerResponse.getSlots().toString()+"\n"
                        +" Register yourself at"+"\n"
                        +" cowin: https://selfregistration.cowin.gov.in/";
                System.out.println("message = " + message);
                try {
                    sendDataToTelegram(message);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void sendDataToTelegram(String message) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();

        UriBuilder builder = UriBuilder
                .fromUri("https://api.telegram.org")
                .path("/{token}/sendMessage")
                .queryParam("chat_id", CHAT_ID)
                .queryParam("text", message);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(builder.build("bot" + TOKEN))
                .timeout(Duration.ofSeconds(5))
                .build();
        System.out.println("request = " + request.uri().toString());
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}

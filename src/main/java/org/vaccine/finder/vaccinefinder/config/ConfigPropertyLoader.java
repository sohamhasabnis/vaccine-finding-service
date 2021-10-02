package org.vaccine.finder.vaccinefinder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

@Component
public class ConfigPropertyLoader {
    @Value("${config.file.location}")
    private String configFileLocation;
    public Properties getProperties() {
        Properties prop = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream(new File(configFileLocation)); InputStream inputStream = new DataInputStream(fileInputStream)) {
            prop.load(inputStream);
        }catch (Exception e) {e.printStackTrace();}
        return prop;
    }
    public String getPropertiesFile(String keyword) {
        Properties prop = getProperties();
        return prop.getProperty(keyword);
    }
}

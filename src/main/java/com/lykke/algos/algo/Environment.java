package com.lykke.algos.algo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {

    static Properties properties;
    public static void load() {
        String propertyFilePath = System.getenv().get("ALGO_PROPERTIES");
        try  {
            InputStream is = new FileInputStream(propertyFilePath);
            properties = new Properties();
            properties.load(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getVariable(String variable) {
        return properties.getProperty(variable);
    }
}
package com.polant.webshop.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Через объект данного класса получается доступ с свойствам соединения с БД.
 */
public class JdbcProperties {

    private static final JdbcProperties instance = new JdbcProperties();

    private final Properties properties = new Properties();

    private JdbcProperties() {
        try {
            properties.load(new FileInputStream(this.getClass().getClassLoader().getResource("database.properties").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JdbcProperties getInstance(){
        return instance;
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}

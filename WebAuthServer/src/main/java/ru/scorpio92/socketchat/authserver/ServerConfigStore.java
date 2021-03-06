package ru.scorpio92.socketchat.authserver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfigStore {

    private static final String PROP_FILE_NAME = "server.properties";

    /**
     * Public HttpAPI
     */
    public static int PAPI_SERVER_PORT = 8031;
    /**
     * Service HttpAPI
     */
    public static int SAPI_SERVER_PORT = 8032;

    public static String DB_NAME = "auth_db";
    public static String DB_USER = "auth_server";
    public static String DB_PASSWORD = "auth_server";

    public static boolean LOGGER_ENABLED = true;

    static void init() throws IOException {
        Properties props = new Properties();
        InputStream in = AuthServer.class.getResourceAsStream("/" + PROP_FILE_NAME);
        props.load(in);

        PAPI_SERVER_PORT = Integer.valueOf(props.getProperty("PAPI_SERVER_PORT"));
        SAPI_SERVER_PORT = Integer.valueOf(props.getProperty("SAPI_SERVER_PORT"));

        DB_NAME = props.getProperty("DB_NAME");
        DB_USER = props.getProperty("DB_USER");
        DB_PASSWORD = props.getProperty("DB_PASSWORD");

        LOGGER_ENABLED = Boolean.valueOf(props.getProperty("LOGGER_ENABLED"));
    }
}

package com.blooddonor.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class DBConnection {
    private static final String CONFIG_FILE = "db_config.properties";
    private static String dbUrl = "jdbc:mysql://localhost:3306/blood_donation_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static String dbUser = "root";
    private static String dbPassword = ""; // default empty or 'root'

    static {
        loadConfig();
    }

    public static void loadConfig() {
        File file = new File(CONFIG_FILE);
        Properties props = new Properties();
        if (file.exists()) {
            try (FileInputStream in = new FileInputStream(file)) {
                props.load(in);
                dbUrl = props.getProperty("db.url", dbUrl);
                dbUser = props.getProperty("db.user", "root");
                dbPassword = props.getProperty("db.password", "");
            } catch (IOException e) {
                System.err.println("Could not read db_config.properties: " + e.getMessage());
            }
        } else {
            // Write default config file
            saveConfig("root", "root");
            dbPassword = "root";
        }
    }

    public static void saveConfig(String user, String password) {
        Properties props = new Properties();
        props.setProperty("db.url", dbUrl);
        props.setProperty("db.user", user);
        props.setProperty("db.password", password);
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "Blood Donation System Database Configuration");
            dbUser = user;
            dbPassword = password;
        } catch (IOException e) {
            System.err.println("Could not save db_config.properties: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found in classpath!", e);
        }

        try {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException ex) {
            // If access denied, give the user a prompt to enter MySQL root password
            if (ex.getErrorCode() == 1045) { // ER_ACCESS_DENIED_ERROR
                JPasswordField pwdField = new JPasswordField(15);
                int option = JOptionPane.showConfirmDialog(
                    null,
                    new Object[] {
                        "MySQL Access Denied! Please enter your MySQL password for user '" + dbUser + "':",
                        pwdField
                    },
                    "MySQL Authentication",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );

                if (option == JOptionPane.OK_OPTION) {
                    String enteredPwd = new String(pwdField.getPassword());
                    saveConfig(dbUser, enteredPwd);
                    return DriverManager.getConnection(dbUrl, dbUser, enteredPwd);
                }
            }
            throw ex;
        }
    }
}

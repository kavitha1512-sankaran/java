package com.blooddonor;

import com.blooddonor.db.DatabaseInitializer;
import com.blooddonor.ui.MainLoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set Look and Feel for native, crisp rendering
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Initialize Database schema and sample records
        SwingUtilities.invokeLater(() -> {
            boolean initialized = DatabaseInitializer.initialize();
            if (!initialized) {
                System.out.println("Note: Database not auto-initialized yet. You can configure MySQL credentials from the login screen.");
            }
            MainLoginFrame frame = new MainLoginFrame();
            frame.setVisible(true);
        });
    }
}

package com.blooddonor.ui;

import com.blooddonor.dao.DonorDAO;
import com.blooddonor.dao.HospitalDAO;
import com.blooddonor.db.DBConnection;
import com.blooddonor.model.Donor;
import com.blooddonor.model.Hospital;

import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class MainLoginFrame extends JFrame {

    private final DonorDAO donorDAO = new DonorDAO();
    private final HospitalDAO hospitalDAO = new HospitalDAO();

    public MainLoginFrame() {
        setTitle("Blood Donor Availability Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 720);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.COLOR_BG);
        setLayout(new BorderLayout());

        // Header
        JPanel banner = UITheme.createHeaderBanner(
            "Blood Donor Availability System",
            "Connecting Lifesavers with Hospitals in Real-Time",
            "JAVA & DBMS PROJECT"
        );
        add(banner, BorderLayout.NORTH);

        // Tabbed Pane for Donor and Hospital
        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.setFont(UITheme.FONT_HEADER);
        mainTabs.setBackground(Color.WHITE);

        mainTabs.addTab("  🩸 DONOR PORTAL  ", createDonorPortalPanel());
        mainTabs.addTab("  🏥 HOSPITAL PORTAL  ", createHospitalPortalPanel());

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(15, 20, 15, 20));
        centerWrapper.add(mainTabs, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        // Footer with DB Settings shortcut
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(8, 20, 8, 20));

        JLabel lblFooter = new JLabel("Blood Donor Availability System | Core Java & MySQL Project");
        lblFooter.setFont(UITheme.FONT_SMALL);
        lblFooter.setForeground(UITheme.COLOR_TEXT_MUTED);
        footer.add(lblFooter, BorderLayout.WEST);

        JButton btnDBSettings = new JButton("⚙ Database Config");
        btnDBSettings.setFont(UITheme.FONT_SMALL);
        btnDBSettings.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDBSettings.addActionListener(e -> showDBSettingsDialog());
        footer.add(btnDBSettings, BorderLayout.EAST);

        add(footer, BorderLayout.SOUTH);
    }

    // ==========================================
    // DONOR PORTAL PANEL
    // ==========================================
    private JPanel createDonorPortalPanel() {
        JPanel panel = new JPanel(new CardLayout());
        panel.setOpaque(false);

        CardLayout cardLayout = (CardLayout) panel.getLayout();

        // 1. Donor Login Card
        JPanel loginCard = UITheme.createCardPanel();
        loginCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Donor Login");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.COLOR_PRIMARY);

        JTextField txtEmail = UITheme.createTextField(25);
        txtEmail.setText("jothika@gmail.com"); // Demo default
        JPasswordField txtPass = UITheme.createPasswordField(25);
        txtPass.setText("donor123");

        JButton btnLogin = UITheme.createPrimaryButton("Login as Donor");
        JButton btnGoRegister = new JButton("New Donor? Register here");
        btnGoRegister.setFont(UITheme.FONT_REGULAR);
        btnGoRegister.setForeground(UITheme.COLOR_PRIMARY);
        btnGoRegister.setBorderPainted(false);
        btnGoRegister.setContentAreaFilled(false);
        btnGoRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginCard.add(lblTitle, gbc);

        gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblHint = new JLabel("Demo: jothika@gmail.com / donor123");
        lblHint.setFont(UITheme.FONT_SMALL);
        lblHint.setForeground(UITheme.COLOR_TEXT_MUTED);
        loginCard.add(lblHint, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        loginCard.add(new JLabel("Email Address:"), gbc);
        gbc.gridx = 1;
        loginCard.add(txtEmail, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        loginCard.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginCard.add(txtPass, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        loginCard.add(btnLogin, gbc);

        gbc.gridy = 5;
        loginCard.add(btnGoRegister, gbc);

        // Login Action
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter email and password!", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Donor donor = donorDAO.login(email, pass);
                if (donor != null) {
                    new DonorDashboard(donor).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid email or password! Please check.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2. Donor Registration Card
        JPanel regCard = createDonorRegistrationPanel(() -> cardLayout.show(panel, "LOGIN"));

        panel.add(loginCard, "LOGIN");
        panel.add(regCard, "REGISTER");

        btnGoRegister.addActionListener(e -> cardLayout.show(panel, "REGISTER"));

        return panel;
    }

    private JPanel createDonorRegistrationPanel(Runnable onBackToLogin) {
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Color.WHITE);
        scrollContent.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("New Donor Registration");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.COLOR_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(lblTitle);

        JLabel lblNote = new JLabel("Eligibility Note: Age must be at least 18 years and weight >= 50 kg.");
        lblNote.setFont(UITheme.FONT_SUBTITLE);
        lblNote.setForeground(UITheme.COLOR_TEXT_MUTED);
        lblNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(lblNote);
        scrollContent.add(Box.createVerticalStrut(15));

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 12, 10));
        formGrid.setOpaque(false);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtName = UITheme.createTextField(20);
        JTextField txtEmail = UITheme.createTextField(20);
        JPasswordField txtPass = UITheme.createPasswordField(20);
        JTextField txtPhone = UITheme.createTextField(20);

        String[] bloodGroups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        JComboBox<String> cmbBlood = new JComboBox<>(bloodGroups);

        JSpinner spnAge = new JSpinner(new SpinnerNumberModel(18, 1, 100, 1));
        spnAge.setFont(UITheme.FONT_REGULAR);

        String[] genders = {"Male", "Female", "Other"};
        JComboBox<String> cmbGender = new JComboBox<>(genders);

        JSpinner spnWeight = new JSpinner(new SpinnerNumberModel(55.0, 30.0, 200.0, 0.5));
        spnWeight.setFont(UITheme.FONT_REGULAR);

        JTextField txtCity = UITheme.createTextField(20);
        JTextField txtLastDate = UITheme.createTextField(20);
        txtLastDate.setToolTipText("YYYY-MM-DD or leave blank if first time donor");

        JCheckBox chkAvailable = new JCheckBox("I am currently available to donate blood", true);
        chkAvailable.setFont(UITheme.FONT_BOLD);
        chkAvailable.setOpaque(false);

        formGrid.add(new JLabel("Full Name *:")); formGrid.add(txtName);
        formGrid.add(new JLabel("Email Address *:")); formGrid.add(txtEmail);
        formGrid.add(new JLabel("Password *:")); formGrid.add(txtPass);
        formGrid.add(new JLabel("Phone Number *:")); formGrid.add(txtPhone);
        formGrid.add(new JLabel("Blood Group *:")); formGrid.add(cmbBlood);
        formGrid.add(new JLabel("Age (Min 18) *:")); formGrid.add(spnAge);
        formGrid.add(new JLabel("Gender:")); formGrid.add(cmbGender);
        formGrid.add(new JLabel("Weight (kg, Min 50) *:")); formGrid.add(spnWeight);
        formGrid.add(new JLabel("City / Town *:")); formGrid.add(txtCity);
        formGrid.add(new JLabel("Last Donation (YYYY-MM-DD):")); formGrid.add(txtLastDate);

        scrollContent.add(formGrid);
        scrollContent.add(Box.createVerticalStrut(10));
        scrollContent.add(chkAvailable);
        scrollContent.add(Box.createVerticalStrut(15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRegister = UITheme.createPrimaryButton("Complete Registration");
        JButton btnBack = new JButton("Cancel / Back to Login");
        btnBack.setFont(UITheme.FONT_REGULAR);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> onBackToLogin.run());

        btnPanel.add(btnRegister);
        btnPanel.add(btnBack);
        scrollContent.add(btnPanel);

        // Register Action with Eligibility Validations
        btnRegister.addActionListener(e -> {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            String phone = txtPhone.getText().trim();
            String blood = (String) cmbBlood.getSelectedItem();
            int age = (Integer) spnAge.getValue();
            String gender = (String) cmbGender.getSelectedItem();
            double weight = (Double) spnWeight.getValue();
            String city = txtCity.getText().trim();
            String lastDateStr = txtLastDate.getText().trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || phone.isEmpty() || city.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // CRITICAL VALIDATION 1: Age >= 18
            if (age < 18) {
                JOptionPane.showMessageDialog(this,
                    "Eligibility Restriction:\nAge must be at least 18 years old to register as a blood donor!\nCurrent age entered: " + age,
                    "Age Restriction",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CRITICAL VALIDATION 2: Weight >= 50 kg
            if (weight < 50.0) {
                JOptionPane.showMessageDialog(this,
                    "Eligibility Restriction:\nWeight must be at least 50 kg for safe blood donation!\nCurrent weight entered: " + weight + " kg",
                    "Weight Restriction",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date lastDonation = null;
            if (!lastDateStr.isEmpty()) {
                try {
                    lastDonation = Date.valueOf(lastDateStr);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Date format! Please use YYYY-MM-DD (e.g. 2026-05-15) or leave it empty.", "Date Format Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            Donor newDonor = new Donor(0, name, email, pass, phone, blood, age, gender, weight, city, lastDonation, chkAvailable.isSelected());
            try {
                boolean success = donorDAO.register(newDonor);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Donor Registration Successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    onBackToLogin.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Could not register donor.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Registration Error (Email may already exist): " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    // ==========================================
    // HOSPITAL PORTAL PANEL
    // ==========================================
    private JPanel createHospitalPortalPanel() {
        JPanel panel = new JPanel(new CardLayout());
        panel.setOpaque(false);
        CardLayout cardLayout = (CardLayout) panel.getLayout();

        // 1. Hospital Login Card
        JPanel loginCard = UITheme.createCardPanel();
        loginCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Hospital Login");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.COLOR_SECONDARY);

        JTextField txtEmail = UITheme.createTextField(25);
        txtEmail.setText("apollo@hospital.com"); // Demo default
        JPasswordField txtPass = UITheme.createPasswordField(25);
        txtPass.setText("hosp123");

        JButton btnLogin = UITheme.createSecondaryButton("Login as Hospital");
        JButton btnGoRegister = new JButton("New Hospital? Register here");
        btnGoRegister.setFont(UITheme.FONT_REGULAR);
        btnGoRegister.setForeground(UITheme.COLOR_SECONDARY);
        btnGoRegister.setBorderPainted(false);
        btnGoRegister.setContentAreaFilled(false);
        btnGoRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginCard.add(lblTitle, gbc);

        gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        JLabel lblHint = new JLabel("Demo: apollo@hospital.com / hosp123");
        lblHint.setFont(UITheme.FONT_SMALL);
        lblHint.setForeground(UITheme.COLOR_TEXT_MUTED);
        loginCard.add(lblHint, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        loginCard.add(new JLabel("Hospital Email:"), gbc);
        gbc.gridx = 1;
        loginCard.add(txtEmail, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        loginCard.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginCard.add(txtPass, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        loginCard.add(btnLogin, gbc);

        gbc.gridy = 5;
        loginCard.add(btnGoRegister, gbc);

        // Login Action
        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            if (email.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter hospital email and password!", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Hospital hospital = hospitalDAO.login(email, pass);
                if (hospital != null) {
                    new HospitalDashboard(hospital).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials for hospital!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2. Hospital Registration Card
        JPanel regCard = createHospitalRegistrationPanel(() -> cardLayout.show(panel, "LOGIN"));

        panel.add(loginCard, "LOGIN");
        panel.add(regCard, "REGISTER");

        btnGoRegister.addActionListener(e -> cardLayout.show(panel, "REGISTER"));

        return panel;
    }

    private JPanel createHospitalRegistrationPanel(Runnable onBackToLogin) {
        JPanel scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(Color.WHITE);
        scrollContent.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("Hospital / Blood Bank Registration");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.COLOR_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(lblTitle);

        JLabel lblNote = new JLabel("Register your healthcare facility to search donors and post blood requests.");
        lblNote.setFont(UITheme.FONT_SUBTITLE);
        lblNote.setForeground(UITheme.COLOR_TEXT_MUTED);
        lblNote.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollContent.add(lblNote);
        scrollContent.add(Box.createVerticalStrut(15));

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 12, 10));
        formGrid.setOpaque(false);
        formGrid.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField txtName = UITheme.createTextField(20);
        JTextField txtEmail = UITheme.createTextField(20);
        JPasswordField txtPass = UITheme.createPasswordField(20);
        JTextField txtPhone = UITheme.createTextField(20);
        JTextField txtAddress = UITheme.createTextField(20);
        JTextField txtCity = UITheme.createTextField(20);
        JTextField txtLicense = UITheme.createTextField(20);

        formGrid.add(new JLabel("Hospital Name *:")); formGrid.add(txtName);
        formGrid.add(new JLabel("Official Email *:")); formGrid.add(txtEmail);
        formGrid.add(new JLabel("Password *:")); formGrid.add(txtPass);
        formGrid.add(new JLabel("Emergency Contact Phone *:")); formGrid.add(txtPhone);
        formGrid.add(new JLabel("Hospital Address *:")); formGrid.add(txtAddress);
        formGrid.add(new JLabel("City *:")); formGrid.add(txtCity);
        formGrid.add(new JLabel("License / Reg Number *:")); formGrid.add(txtLicense);

        scrollContent.add(formGrid);
        scrollContent.add(Box.createVerticalStrut(15));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnRegister = UITheme.createSecondaryButton("Register Hospital");
        JButton btnBack = new JButton("Cancel / Back to Login");
        btnBack.setFont(UITheme.FONT_REGULAR);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> onBackToLogin.run());

        btnPanel.add(btnRegister);
        btnPanel.add(btnBack);
        scrollContent.add(btnPanel);

        btnRegister.addActionListener(e -> {
            String name = txtName.getText().trim();
            String email = txtEmail.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();
            String phone = txtPhone.getText().trim();
            String address = txtAddress.getText().trim();
            String city = txtCity.getText().trim();
            String license = txtLicense.getText().trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || phone.isEmpty() || city.isEmpty() || license.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all hospital details!", "Missing Info", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Hospital newHosp = new Hospital(0, name, email, pass, phone, address, city, license);
            try {
                boolean success = hospitalDAO.register(newHosp);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Hospital registered successfully! You can now log in.", "Registered", JOptionPane.INFORMATION_MESSAGE);
                    onBackToLogin.run();
                } else {
                    JOptionPane.showMessageDialog(this, "Registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error (Hospital email might exist): " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JScrollPane scroll = new JScrollPane(scrollContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(scroll, BorderLayout.CENTER);
        return wrapper;
    }

    private void showDBSettingsDialog() {
        JTextField txtUser = new JTextField("root", 15);
        JPasswordField txtPass = new JPasswordField(15);

        int res = JOptionPane.showConfirmDialog(
            this,
            new Object[] {
                "MySQL Username:", txtUser,
                "MySQL Password:", txtPass
            },
            "Database Credentials Configuration",
            JOptionPane.OK_CANCEL_OPTION
        );

        if (res == JOptionPane.OK_OPTION) {
            DBConnection.saveConfig(txtUser.getText().trim(), new String(txtPass.getPassword()));
            JOptionPane.showMessageDialog(this, "Database settings saved to db_config.properties!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

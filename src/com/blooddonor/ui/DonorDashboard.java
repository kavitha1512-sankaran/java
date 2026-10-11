package com.blooddonor.ui;

import com.blooddonor.dao.BloodRequestDAO;
import com.blooddonor.dao.DonorDAO;
import com.blooddonor.model.BloodRequest;
import com.blooddonor.model.Donor;

import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class DonorDashboard extends JFrame {

    private final Donor donor;
    private final DonorDAO donorDAO = new DonorDAO();
    private final BloodRequestDAO requestDAO = new BloodRequestDAO();

    private JLabel lblStatusBadge;
    private JButton btnToggleStatus;
    private DefaultTableModel requestTableModel;

    public DonorDashboard(Donor donor) {
        this.donor = donor;
        setTitle("Donor Dashboard - " + donor.getFullName() + " [" + donor.getBloodGroup() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 720);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.COLOR_BG);
        setLayout(new BorderLayout());

        // Top Banner
        add(createTopBanner(), BorderLayout.NORTH);

        // Center Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.FONT_HEADER);
        tabs.setBackground(Color.WHITE);

        tabs.addTab("  👤 My Profile & Availability  ", createProfilePanel());
        tabs.addTab("  🚨 Hospital Blood Requests  ", createHospitalRequestsPanel());
        tabs.addTab("  ℹ Compatibility & Guidelines  ", createCompatibilityPanel());

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(15, 20, 15, 20));
        centerWrapper.add(tabs, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(Color.WHITE);
        JButton btnLogout = new JButton("🚪 Logout");
        btnLogout.setFont(UITheme.FONT_BOLD);
        btnLogout.setForeground(UITheme.COLOR_CRITICAL);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            new MainLoginFrame().setVisible(true);
            dispose();
        });
        footer.add(btnLogout);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createTopBanner() {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(UITheme.COLOR_PRIMARY);
        banner.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        leftPanel.setOpaque(false);

        JLabel lblName = new JLabel("Welcome, " + donor.getFullName());
        lblName.setFont(UITheme.FONT_TITLE);
        lblName.setForeground(Color.WHITE);

        JLabel lblSubtitle = new JLabel("Blood Group: " + donor.getBloodGroup() + " | City: " + donor.getCity() + " | Age: " + donor.getAge());
        lblSubtitle.setFont(UITheme.FONT_SUBTITLE);
        lblSubtitle.setForeground(new Color(255, 235, 238));

        leftPanel.add(lblName);
        leftPanel.add(lblSubtitle);
        banner.add(leftPanel, BorderLayout.WEST);

        // Status Panel on Right
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        rightPanel.setOpaque(false);

        lblStatusBadge = new JLabel();
        lblStatusBadge.setFont(UITheme.FONT_BOLD);
        lblStatusBadge.setOpaque(true);
        lblStatusBadge.setBorder(new EmptyBorder(6, 14, 6, 14));

        btnToggleStatus = new JButton();
        btnToggleStatus.setFont(UITheme.FONT_BOLD);
        btnToggleStatus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnToggleStatus.addActionListener(e -> toggleAvailability());

        updateAvailabilityUI();

        rightPanel.add(lblStatusBadge);
        rightPanel.add(btnToggleStatus);
        banner.add(rightPanel, BorderLayout.EAST);

        return banner;
    }

    private void updateAvailabilityUI() {
        if (donor.isAvailable()) {
            lblStatusBadge.setText("● STATUS: AVAILABLE TO DONATE");
            lblStatusBadge.setBackground(new Color(232, 245, 233));
            lblStatusBadge.setForeground(UITheme.COLOR_SUCCESS);

            btnToggleStatus.setText("Switch to Not Available");
            btnToggleStatus.setBackground(new Color(238, 238, 238));
            btnToggleStatus.setForeground(UITheme.COLOR_TEXT_MAIN);
        } else {
            lblStatusBadge.setText("● STATUS: CURRENTLY UNAVAILABLE");
            lblStatusBadge.setBackground(new Color(255, 235, 238));
            lblStatusBadge.setForeground(UITheme.COLOR_CRITICAL);

            btnToggleStatus.setText("Switch to Available");
            btnToggleStatus.setBackground(UITheme.COLOR_SUCCESS);
            btnToggleStatus.setForeground(Color.WHITE);
        }
    }

    private void toggleAvailability() {
        boolean newStatus = !donor.isAvailable();
        try {
            boolean ok = donorDAO.toggleAvailability(donor.getId(), newStatus);
            if (ok) {
                donor.setAvailable(newStatus);
                updateAvailabilityUI();
                JOptionPane.showMessageDialog(this,
                    "Your status is now updated to: " + (newStatus ? "AVAILABLE TO DONATE" : "NOT AVAILABLE"),
                    "Status Updated",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not update status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // TAB 1: Profile Editing
    private JPanel createProfilePanel() {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(20, 20));

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 15, 12));
        formGrid.setOpaque(false);

        JTextField txtName = UITheme.createTextField(20);
        txtName.setText(donor.getFullName());

        JTextField txtEmail = UITheme.createTextField(20);
        txtEmail.setText(donor.getEmail());
        txtEmail.setEditable(false); // Email stays unique ID

        JTextField txtPhone = UITheme.createTextField(20);
        txtPhone.setText(donor.getPhone());

        String[] bloodGroups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        JComboBox<String> cmbBlood = new JComboBox<>(bloodGroups);
        cmbBlood.setSelectedItem(donor.getBloodGroup());

        JSpinner spnAge = new JSpinner(new SpinnerNumberModel(donor.getAge(), 1, 100, 1));
        spnAge.setFont(UITheme.FONT_REGULAR);

        String[] genders = {"Male", "Female", "Other"};
        JComboBox<String> cmbGender = new JComboBox<>(genders);
        cmbGender.setSelectedItem(donor.getGender());

        JSpinner spnWeight = new JSpinner(new SpinnerNumberModel(donor.getWeightKg(), 30.0, 200.0, 0.5));
        spnWeight.setFont(UITheme.FONT_REGULAR);

        JTextField txtCity = UITheme.createTextField(20);
        txtCity.setText(donor.getCity());

        JTextField txtLastDate = UITheme.createTextField(20);
        txtLastDate.setText(donor.getLastDonationDate() != null ? donor.getLastDonationDate().toString() : "");

        formGrid.add(new JLabel("Full Name:")); formGrid.add(txtName);
        formGrid.add(new JLabel("Email Address (Permanent):")); formGrid.add(txtEmail);
        formGrid.add(new JLabel("Phone Number:")); formGrid.add(txtPhone);
        formGrid.add(new JLabel("Blood Group:")); formGrid.add(cmbBlood);
        formGrid.add(new JLabel("Age (Years, Min 18):")); formGrid.add(spnAge);
        formGrid.add(new JLabel("Gender:")); formGrid.add(cmbGender);
        formGrid.add(new JLabel("Weight (kg, Min 50):")); formGrid.add(spnWeight);
        formGrid.add(new JLabel("City / Location:")); formGrid.add(txtCity);
        formGrid.add(new JLabel("Last Donation Date (YYYY-MM-DD):")); formGrid.add(txtLastDate);

        // Eligibility analysis
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel lblEligible = new JLabel();
        lblEligible.setFont(UITheme.FONT_BOLD);

        if (donor.getLastDonationDate() != null) {
            LocalDate lastDate = donor.getLastDonationDate().toLocalDate();
            long daysPassed = ChronoUnit.DAYS.between(lastDate, LocalDate.now());
            if (daysPassed >= 90) {
                lblEligible.setText("✔ Medical Status: " + daysPassed + " days since last donation. Eligible to donate blood now!");
                lblEligible.setForeground(UITheme.COLOR_SUCCESS);
            } else {
                long waitDays = 90 - daysPassed;
                lblEligible.setText("⏳ Medical Status: Only " + daysPassed + " days since last donation. Please wait " + waitDays + " more days (3-month interval).");
                lblEligible.setForeground(UITheme.COLOR_URGENT);
            }
        } else {
            lblEligible.setText("✔ First-time / New Donor! Ready for donation.");
            lblEligible.setForeground(UITheme.COLOR_SUCCESS);
        }

        infoPanel.add(lblEligible);

        // Save Button
        JButton btnSave = UITheme.createPrimaryButton("Save Profile Changes");
        btnSave.addActionListener(e -> {
            int newAge = (Integer) spnAge.getValue();
            double newWeight = (Double) spnWeight.getValue();

            // Age rule validation
            if (newAge < 18) {
                JOptionPane.showMessageDialog(this, "Donor age cannot be less than 18 years!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newWeight < 50.0) {
                JOptionPane.showMessageDialog(this, "Weight must be at least 50 kg for blood donation!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Date newLastDate = null;
            String dtStr = txtLastDate.getText().trim();
            if (!dtStr.isEmpty()) {
                try {
                    newLastDate = Date.valueOf(dtStr);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Date format! Please use YYYY-MM-DD", "Format Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            donor.setFullName(txtName.getText().trim());
            donor.setPhone(txtPhone.getText().trim());
            donor.setBloodGroup((String) cmbBlood.getSelectedItem());
            donor.setAge(newAge);
            donor.setGender((String) cmbGender.getSelectedItem());
            donor.setWeightKg(newWeight);
            donor.setCity(txtCity.getText().trim());
            donor.setLastDonationDate(newLastDate);

            try {
                boolean ok = donorDAO.updateProfile(donor);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        bottomPanel.add(btnSave, BorderLayout.WEST);

        card.add(formGrid, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

    // TAB 2: Hospital Blood Requests
    private JPanel createHospitalRequestsPanel() {
        JPanel panel = UITheme.createCardPanel();
        panel.setLayout(new BorderLayout(10, 10));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel lblReqTitle = new JLabel("Live Hospital Blood Demands & Emergencies");
        lblReqTitle.setFont(UITheme.FONT_HEADER);
        topBar.add(lblReqTitle, BorderLayout.WEST);

        JButton btnRefresh = new JButton("🔄 Refresh Requests");
        btnRefresh.setFont(UITheme.FONT_REGULAR);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadRequests());
        topBar.add(btnRefresh, BorderLayout.EAST);

        panel.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Hospital Name", "City", "Blood Needed", "Units", "Urgency", "Case / Reason", "Emergency Contact"};
        requestTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(requestTableModel);
        UITheme.styleTable(table);
        loadRequests();

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void loadRequests() {
        requestTableModel.setRowCount(0);
        try {
            List<BloodRequest> list = requestDAO.getAllActiveRequests();
            for (BloodRequest br : list) {
                requestTableModel.addRow(new Object[] {
                    br.getHospitalName(),
                    br.getHospitalCity(),
                    br.getBloodGroup(),
                    br.getUnitsNeeded() + " Units",
                    br.getUrgencyLevel(),
                    br.getPatientCase(),
                    br.getHospitalPhone()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to load requests: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // TAB 3: Compatibility Chart
    private JPanel createCompatibilityPanel() {
        JPanel panel = UITheme.createCardPanel();
        panel.setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Blood Compatibility & General Donation Guide");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.COLOR_PRIMARY);
        panel.add(lblTitle, BorderLayout.NORTH);

        String guideText = "<html><body style='font-family: Segoe UI; font-size: 13px; line-height: 1.6;'>"
            + "<h3 style='color: #B71C1C;'>🩸 Blood Type Compatibility Matrix</h3>"
            + "<table border='1' cellpadding='8' style='border-collapse: collapse; width: 100%; border-color: #ddd;'>"
            + "<tr style='background-color: #f2f2f2;'><th>Blood Type</th><th>Can Donate Red Cells To</th><th>Can Receive Red Cells From</th></tr>"
            + "<tr><td><b>O-</b></td><td>EVERYONE (Universal Red Cell Donor)</td><td>O- only</td></tr>"
            + "<tr><td><b>O+</b></td><td>O+, A+, B+, AB+</td><td>O+, O-</td></tr>"
            + "<tr><td><b>A-</b></td><td>A-, A+, AB-, AB+</td><td>A-, O-</td></tr>"
            + "<tr><td><b>A+</b></td><td>A+, AB+</td><td>A+, A-, O+, O-</td></tr>"
            + "<tr><td><b>B-</b></td><td>B-, B+, AB-, AB+</td><td>B-, O-</td></tr>"
            + "<tr><td><b>B+</b></td><td>B+, AB+</td><td>B+, B-, O+, O-</td></tr>"
            + "<tr><td><b>AB-</b></td><td>AB-, AB+</td><td>AB-, A-, B-, O-</td></tr>"
            + "<tr><td><b>AB+</b></td><td>AB+ only</td><td>EVERYONE (Universal Recipient)</td></tr>"
            + "</table>"
            + "<br><h3 style='color: #B71C1C;'>📋 Key Donor Guidelines</h3>"
            + "<ul>"
            + "<li><b>Age Requirement:</b> 18 – 65 years old.</li>"
            + "<li><b>Weight Requirement:</b> At least 50 kg (110 lbs).</li>"
            + "<li><b>Donation Frequency:</b> Minimum 3 months (90 days) between whole blood donations.</li>"
            + "<li><b>Hydration:</b> Drink plenty of fluids (water/fruit juice) before and after donating.</li>"
            + "<li><b>Rest:</b> Avoid heavy physical exertion for 12 hours after donating.</li>"
            + "</ul></body></html>";

        JLabel lblGuide = new JLabel(guideText);
        panel.add(new JScrollPane(lblGuide), BorderLayout.CENTER);

        return panel;
    }
}

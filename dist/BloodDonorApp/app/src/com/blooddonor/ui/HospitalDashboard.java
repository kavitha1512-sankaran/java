package com.blooddonor.ui;

import com.blooddonor.dao.BloodRequestDAO;
import com.blooddonor.dao.DonorDAO;
import com.blooddonor.model.BloodRequest;
import com.blooddonor.model.Donor;
import com.blooddonor.model.Hospital;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class HospitalDashboard extends JFrame {

    private final Hospital hospital;
    private final DonorDAO donorDAO = new DonorDAO();
    private final BloodRequestDAO requestDAO = new BloodRequestDAO();

    private DefaultTableModel donorTableModel;
    private DefaultTableModel myRequestsTableModel;
    private JLabel lblResultCount;
    private JComboBox<String> cmbSearchBlood;
    private JTextField txtSearchCity;
    private JCheckBox chkOnlyAvailable;

    public HospitalDashboard(Hospital hospital) {
        this.hospital = hospital;
        setTitle("Hospital Portal - " + hospital.getHospitalName() + " (" + hospital.getCity() + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 740);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UITheme.COLOR_BG);
        setLayout(new BorderLayout());

        // Header
        add(createTopBanner(), BorderLayout.NORTH);

        // Center Tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UITheme.FONT_HEADER);
        tabs.setBackground(Color.WHITE);

        tabs.addTab("  🔍 Search Blood Donors  ", createDonorSearchPanel());
        tabs.addTab("  ➕ Post Blood Demand / Emergency  ", createPostRequestPanel());
        tabs.addTab("  📋 Our Active Requests  ", createMyRequestsPanel());

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
        banner.setBackground(UITheme.COLOR_SECONDARY);
        banner.setBorder(new EmptyBorder(15, 25, 15, 25));

        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        leftPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Hospital Portal: " + hospital.getHospitalName());
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("City: " + hospital.getCity() + " | License: " + hospital.getLicenseNo() + " | Phone: " + hospital.getPhone());
        lblSub.setFont(UITheme.FONT_SUBTITLE);
        lblSub.setForeground(new Color(207, 216, 220));

        leftPanel.add(lblTitle);
        leftPanel.add(lblSub);
        banner.add(leftPanel, BorderLayout.WEST);

        JLabel lblBadge = new JLabel("  VERIFIED HOSPITAL  ");
        lblBadge.setFont(UITheme.FONT_BOLD);
        lblBadge.setBackground(UITheme.COLOR_SUCCESS);
        lblBadge.setForeground(Color.WHITE);
        lblBadge.setOpaque(true);
        lblBadge.setBorder(new EmptyBorder(6, 12, 6, 12));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(lblBadge);
        banner.add(rightPanel, BorderLayout.EAST);

        return banner;
    }

    // TAB 1: Search & Check Donor Availability
    private JPanel createDonorSearchPanel() {
        JPanel panel = UITheme.createCardPanel();
        panel.setLayout(new BorderLayout(15, 15));

        // Filter Controls Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UITheme.COLOR_BORDER, 1),
            "Find & Filter Donors",
            0, 0, UITheme.FONT_BOLD, UITheme.COLOR_SECONDARY
        ));

        String[] bloodOptions = {"ALL", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        cmbSearchBlood = new JComboBox<>(bloodOptions);
        cmbSearchBlood.setFont(UITheme.FONT_REGULAR);

        txtSearchCity = UITheme.createTextField(12);
        txtSearchCity.setToolTipText("Enter city or leave empty");

        chkOnlyAvailable = new JCheckBox("Available Donors Only", true);
        chkOnlyAvailable.setFont(UITheme.FONT_BOLD);
        chkOnlyAvailable.setOpaque(false);

        JButton btnSearch = UITheme.createPrimaryButton("Search Donors");
        JButton btnReset = new JButton("Reset Filters");
        btnReset.setFont(UITheme.FONT_REGULAR);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));

        filterPanel.add(new JLabel("Blood Group:"));
        filterPanel.add(cmbSearchBlood);
        filterPanel.add(new JLabel("City:"));
        filterPanel.add(txtSearchCity);
        filterPanel.add(chkOnlyAvailable);
        filterPanel.add(btnSearch);
        filterPanel.add(btnReset);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Name", "Blood Group", "Age", "Gender", "Weight (kg)", "City", "Phone", "Availability", "Last Donated"};
        donorTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(donorTableModel);
        UITheme.styleTable(table);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom status and action
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);

        lblResultCount = new JLabel("Showing donors...");
        lblResultCount.setFont(UITheme.FONT_BOLD);
        lblResultCount.setForeground(UITheme.COLOR_SECONDARY);
        bottomBar.add(lblResultCount, BorderLayout.WEST);

        JButton btnContact = UITheme.createSecondaryButton("📞 View Selected Donor Contact");
        btnContact.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please click and select a donor from the table first!", "Select Donor", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String name = (String) donorTableModel.getValueAt(row, 1);
            String blood = (String) donorTableModel.getValueAt(row, 2);
            String city = (String) donorTableModel.getValueAt(row, 6);
            String phone = (String) donorTableModel.getValueAt(row, 7);
            String avail = (String) donorTableModel.getValueAt(row, 8);

            JOptionPane.showMessageDialog(this,
                "Donor Contact Details:\n\n" +
                "👤 Name: " + name + "\n" +
                "🩸 Blood Group: " + blood + "\n" +
                "🏙 Location: " + city + "\n" +
                "📞 Phone Number: " + phone + "\n" +
                "📌 Availability: " + avail + "\n\n" +
                "Hospital staff can reach out directly for blood donation.",
                "Donor Contact Info",
                JOptionPane.INFORMATION_MESSAGE);
        });

        bottomBar.add(btnContact, BorderLayout.EAST);
        panel.add(bottomBar, BorderLayout.SOUTH);

        // Action Listeners
        btnSearch.addActionListener(e -> executeSearch());
        btnReset.addActionListener(e -> {
            cmbSearchBlood.setSelectedIndex(0);
            txtSearchCity.setText("");
            chkOnlyAvailable.setSelected(false);
            executeSearch();
        });

        // Initial search load
        executeSearch();

        return panel;
    }

    private void executeSearch() {
        donorTableModel.setRowCount(0);
        String blood = (String) cmbSearchBlood.getSelectedItem();
        String city = txtSearchCity.getText().trim();
        boolean onlyAvail = chkOnlyAvailable.isSelected();

        try {
            List<Donor> list = donorDAO.searchDonors(blood, city, onlyAvail);
            for (Donor d : list) {
                donorTableModel.addRow(new Object[] {
                    d.getId(),
                    d.getFullName(),
                    d.getBloodGroup(),
                    d.getAge(),
                    d.getGender(),
                    d.getWeightKg() + " kg",
                    d.getCity(),
                    d.getPhone(),
                    d.isAvailable() ? "AVAILABLE" : "UNAVAILABLE",
                    d.getLastDonationDate() != null ? d.getLastDonationDate().toString() : "Never / First Time"
                });
            }
            lblResultCount.setText("Found " + list.size() + " donor(s) matching criteria.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Search error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // TAB 2: Post Blood Demand
    private JPanel createPostRequestPanel() {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(20, 20));

        JLabel lblTitle = new JLabel("Publish Blood Requirement / Emergency Demand");
        lblTitle.setFont(UITheme.FONT_TITLE);
        lblTitle.setForeground(UITheme.COLOR_CRITICAL);
        card.add(lblTitle, BorderLayout.NORTH);

        JPanel formGrid = new JPanel(new GridLayout(0, 2, 15, 15));
        formGrid.setOpaque(false);

        String[] bloodGroups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        JComboBox<String> cmbBlood = new JComboBox<>(bloodGroups);

        JSpinner spnUnits = new JSpinner(new SpinnerNumberModel(2, 1, 50, 1));
        spnUnits.setFont(UITheme.FONT_REGULAR);

        String[] urgencyLevels = {"Normal", "Urgent", "Critical"};
        JComboBox<String> cmbUrgency = new JComboBox<>(urgencyLevels);
        cmbUrgency.setSelectedItem("Urgent");

        JTextField txtCase = UITheme.createTextField(25);
        txtCase.setToolTipText("e.g., Cardiac Surgery, Accident ICU, Dengue Platelets");

        formGrid.add(new JLabel("Required Blood Group *:")); formGrid.add(cmbBlood);
        formGrid.add(new JLabel("Units Needed (Pints) *:")); formGrid.add(spnUnits);
        formGrid.add(new JLabel("Urgency Level *:")); formGrid.add(cmbUrgency);
        formGrid.add(new JLabel("Patient Case / Medical Reason:")); formGrid.add(txtCase);

        JButton btnPublish = UITheme.createPrimaryButton("📢 Publish Blood Request");
        btnPublish.addActionListener(e -> {
            String bg = (String) cmbBlood.getSelectedItem();
            int units = (Integer) spnUnits.getValue();
            String urgency = (String) cmbUrgency.getSelectedItem();
            String caseDesc = txtCase.getText().trim();

            if (caseDesc.isEmpty()) {
                caseDesc = "Immediate Clinical Requirement";
            }

            BloodRequest req = new BloodRequest(0, hospital.getId(), hospital.getHospitalName(),
                hospital.getPhone(), hospital.getCity(), bg, units, urgency, caseDesc, "Pending", null);

            try {
                boolean ok = requestDAO.createRequest(req);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Blood Request successfully broadcasted to donors!", "Request Created", JOptionPane.INFORMATION_MESSAGE);
                    txtCase.setText("");
                    loadMyRequests();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to create blood request.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        card.add(formGrid, BorderLayout.CENTER);
        card.add(btnPublish, BorderLayout.SOUTH);

        return card;
    }

    // TAB 3: Our Active Hospital Requests
    private JPanel createMyRequestsPanel() {
        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(15, 15));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel lblTitle = new JLabel("Blood Requests Posted by " + hospital.getHospitalName());
        lblTitle.setFont(UITheme.FONT_HEADER);
        topBar.add(lblTitle, BorderLayout.WEST);

        JButton btnRefresh = new JButton("🔄 Refresh");
        btnRefresh.setFont(UITheme.FONT_REGULAR);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadMyRequests());
        topBar.add(btnRefresh, BorderLayout.EAST);

        card.add(topBar, BorderLayout.NORTH);

        String[] cols = {"Request ID", "Blood Group", "Units", "Urgency", "Case / Reason", "Status", "Date Posted"};
        myRequestsTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(myRequestsTableModel);
        UITheme.styleTable(table);
        loadMyRequests();

        card.add(new JScrollPane(table), BorderLayout.CENTER);

        // Actions to mark fulfilled or cancel
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JButton btnFulfill = UITheme.createButton("✔ Mark Fulfilled", UITheme.COLOR_SUCCESS, Color.WHITE);
        btnFulfill.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a request from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int reqId = (Integer) myRequestsTableModel.getValueAt(row, 0);
            updateRequestStatus(reqId, "Fulfilled");
        });

        JButton btnCancel = UITheme.createButton("✖ Cancel Request", UITheme.COLOR_CRITICAL, Color.WHITE);
        btnCancel.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a request from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int reqId = (Integer) myRequestsTableModel.getValueAt(row, 0);
            updateRequestStatus(reqId, "Cancelled");
        });

        actionPanel.add(btnFulfill);
        actionPanel.add(btnCancel);
        card.add(actionPanel, BorderLayout.SOUTH);

        return card;
    }

    private void loadMyRequests() {
        if (myRequestsTableModel == null) return;
        myRequestsTableModel.setRowCount(0);
        try {
            List<BloodRequest> list = requestDAO.getRequestsByHospital(hospital.getId());
            for (BloodRequest br : list) {
                myRequestsTableModel.addRow(new Object[] {
                    br.getId(),
                    br.getBloodGroup(),
                    br.getUnitsNeeded(),
                    br.getUrgencyLevel(),
                    br.getPatientCase(),
                    br.getStatus(),
                    br.getRequestDate() != null ? br.getRequestDate().toString().substring(0, 16) : "Recent"
                });
            }
        } catch (Exception ex) {
            System.err.println("Could not load hospital requests: " + ex.getMessage());
        }
    }

    private void updateRequestStatus(int reqId, String newStatus) {
        try {
            boolean ok = requestDAO.updateStatus(reqId, newStatus);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Request status updated to " + newStatus, "Updated", JOptionPane.INFORMATION_MESSAGE);
                loadMyRequests();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Update error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

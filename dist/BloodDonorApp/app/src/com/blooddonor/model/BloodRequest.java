package com.blooddonor.model;

import java.sql.Timestamp;

public class BloodRequest {
    private int id;
    private int hospitalId;
    private String hospitalName;
    private String hospitalPhone;
    private String hospitalCity;
    private String bloodGroup;
    private int unitsNeeded;
    private String urgencyLevel;
    private String patientCase;
    private String status;
    private Timestamp requestDate;

    public BloodRequest() {}

    public BloodRequest(int id, int hospitalId, String hospitalName, String hospitalPhone,
                        String hospitalCity, String bloodGroup, int unitsNeeded,
                        String urgencyLevel, String patientCase, String status, Timestamp requestDate) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalPhone = hospitalPhone;
        this.hospitalCity = hospitalCity;
        this.bloodGroup = bloodGroup;
        this.unitsNeeded = unitsNeeded;
        this.urgencyLevel = urgencyLevel;
        this.patientCase = patientCase;
        this.status = status;
        this.requestDate = requestDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getHospitalPhone() { return hospitalPhone; }
    public void setHospitalPhone(String hospitalPhone) { this.hospitalPhone = hospitalPhone; }

    public String getHospitalCity() { return hospitalCity; }
    public void setHospitalCity(String hospitalCity) { this.hospitalCity = hospitalCity; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getUnitsNeeded() { return unitsNeeded; }
    public void setUnitsNeeded(int unitsNeeded) { this.unitsNeeded = unitsNeeded; }

    public String getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(String urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public String getPatientCase() { return patientCase; }
    public void setPatientCase(String patientCase) { this.patientCase = patientCase; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getRequestDate() { return requestDate; }
    public void setRequestDate(Timestamp requestDate) { this.requestDate = requestDate; }
}

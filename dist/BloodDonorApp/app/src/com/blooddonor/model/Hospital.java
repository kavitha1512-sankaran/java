package com.blooddonor.model;

public class Hospital {
    private int id;
    private String hospitalName;
    private String email;
    private String password;
    private String phone;
    private String address;
    private String city;
    private String licenseNo;

    public Hospital() {}

    public Hospital(int id, String hospitalName, String email, String password,
                    String phone, String address, String city, String licenseNo) {
        this.id = id;
        this.hospitalName = hospitalName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.licenseNo = licenseNo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getHospitalName() { return hospitalName; }
    public void setHospitalName(String hospitalName) { this.hospitalName = hospitalName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }
}

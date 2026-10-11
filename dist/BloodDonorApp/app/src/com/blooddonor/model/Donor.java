package com.blooddonor.model;

import java.sql.Date;

public class Donor {
    private int id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private String bloodGroup;
    private int age;
    private String gender;
    private double weightKg;
    private String city;
    private Date lastDonationDate;
    private boolean isAvailable;

    public Donor() {
        this.isAvailable = true;
    }

    public Donor(int id, String fullName, String email, String password, String phone,
                 String bloodGroup, int age, String gender, double weightKg,
                 String city, Date lastDonationDate, boolean isAvailable) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.age = age;
        this.gender = gender;
        this.weightKg = weightKg;
        this.city = city;
        this.lastDonationDate = lastDonationDate;
        this.isAvailable = isAvailable;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Date getLastDonationDate() { return lastDonationDate; }
    public void setLastDonationDate(Date lastDonationDate) { this.lastDonationDate = lastDonationDate; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}

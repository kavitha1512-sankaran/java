# 🩸 Blood Donor Availability Management System
**ஜாவா மற்றும் MySQL டேட்டாபேஸ் மூலம் உருவாக்கப்பட்ட முழுமையான ப்ராஜெக்ட்**

---

## 📌 ப்ராஜெக்ட் கண்ணோட்டம் (Project Overview)
இரத்த தானம் செய்பவர்களையும் (Donors) மற்றும் அவசரத் தேவைப்படும் மருத்துவமனைகளையும் (Hospitals) நிகழ்நேரத்தில் (Real-Time) இணைக்கும் ஒரு முழுமையான மேலாண்மை அமைப்பு.

### 🌟 முக்கிய அம்சங்கள் (Key Highlights)
1. **இருவேறு லாகின் போர்ட்டல்கள் (Dual Portal System):**
   - **டோனார் பக்கம் (Donor Portal):** பதிவு, சுயவிவர மாற்றம், ரத்த தான தகுதி சரிபார்ப்பு, தானம் செய்ய தயார்/இல்லை (Availability Status Toggle).
   - **மருத்துவமனை பக்கம் (Hospital Portal):** ரத்தக் குழு மற்றும் ஊர் வாரியாக டோனார்களைத் தேடுதல், தொடர்பு விவரங்களைப் பெறுதல், அவசர இரத்தக் கோரிக்கைகளை வெளியிடுதல்.
2. **தகுதி விதிகள் மற்றும் கட்டுப்பாடுகள் (Medical Eligibility Validations):**
   - **Age $\ge$ 18:** 18 வயதுக்குக் கீழ் உள்ளவர்கள் பதிவு செய்ய முடியாது.
   - **Weight $\ge$ 50 kg:** இரத்த தானத்திற்கான குறைந்தபட்ச எடை 50 கிலோ.
   - **3 Months Interval:** கடைசியாக ரத்தம் கொடுத்த தேதியிலிருந்து 90 நாட்கள் இடைவெளி தானாக கணக்கிடப்படும்.
3. **நிகழ்நேர நிலை மாற்றம் (One-Click Availability Toggle):**
   - டோனார் ஒரே கிளிக்கில் **"AVAILABLE"** அல்லது **"NOT AVAILABLE"** என மாற்றலாம்; டேட்டாபேஸில் உடனே அப்டேட் ஆகும்.
4. **அவசர எச்சரிக்கைப் பலகை (Emergency Blood Requests):**
   - மருத்துவமனைகள் Critical / Urgent என்று கோரிக்கை பதிவிட்டால் டோனார்களுக்கு உடனே தெரியும்.

---

## 🏗️ தொழில்நுட்ப விவரங்கள் (Tech Stack)
- **Frontend / UI:** Java Swing (Modern Native Windows GUI)
- **Backend / Database:** MySQL 8.0
- **Database Connectivity:** JDBC (MySQL Connector/J 8.3.0)
- **Architecture:** DAO (Data Access Object) Pattern & Clean MVC Structure

---

## 📂 திட்டக் கோப்புகள் அமைப்பு (Project Structure)
```
java & DBMS/
├── lib/
│   └── mysql-connector-j-8.3.0.jar      # MySQL JDBC Driver JAR
├── src/
│   └── com/blooddonor/
│       ├── Main.java                    # அப்ளிகேஷன் தொடக்கப் புள்ளி
│       ├── db/
│       │   ├── DBConnection.java        # MySQL இணைப்பு மற்றும் தானியங்கி மீட்பு
│       │   └── DatabaseInitializer.java # அட்டவணைகள் தானாக உருவாக்கும் வகுப்பு
│       ├── model/
│       │   ├── Donor.java               # டோனார் மாதிரி
│       │   ├── Hospital.java            # மருத்துவமனை மாதிரி
│       │   └── BloodRequest.java        # இரத்தக் கோரிக்கை மாதிரி
│       ├── dao/
│       │   ├── DonorDAO.java            # டோனார் SQL செயல்பாடுகள்
│       │   ├── HospitalDAO.java         # மருத்துவமனை SQL செயல்பாடுகள்
│       │   └── BloodRequestDAO.java     # கோரிக்கைகள் SQL செயல்பாடுகள்
│       └── ui/
│           ├── UITheme.java             # நவீன டிசைன், வண்ணங்கள் மற்றும் ஸ்டைலிங்
│           ├── MainLoginFrame.java      # இரு போர்ட்டல் லாகின் & ரெஜிஸ்ட்ரேஷன்
│           ├── DonorDashboard.java      # டோனார் டேஷ்போர்டு & ப்ரொஃபைல்
│           └── HospitalDashboard.java   # மருத்துவமனை டேஷ்போர்டு & தேடல்
├── bin/                                 # கம்பைல் செய்யப்பட்ட .class கோப்புகள்
├── database_setup.sql                   # MySQL டேட்டாபேஸ் மற்றும் மாதிரி தரவுகள்
├── db_config.properties                 # MySQL User & Password அமைப்பு
├── compile.bat                          # ஒரு கிளிக்கில் கம்பைல் செய்யும் ஸ்கிரிப்ட்
├── run.bat                              # ஒரு கிளிக்கில் அப்ளிகேஷன் இயக்கும் ஸ்கிரிப்ட்
└── README.md
```

---

## 🔑 டெமோ கணக்குகள் (Demo Accounts for Testing)

### 1. டோனார் லாகின் (Donor Login)
- **Email:** `jothika@gmail.com`
- **Password:** `donor123`
- *(மேலும்: kumar@gmail.com / arun@gmail.com / divya@gmail.com)*

### 2. மருத்துவமனை லாகின் (Hospital Login)
- **Email:** `apollo@hospital.com`
- **Password:** `hosp123`
- *(மேலும்: kmch@hospital.com)*

---

## 🚀 அப்ளிகேஷனை இயக்குவது எப்படி? (How to Run)

### வழிமுறை 1 (மிக எளிது - 1-Click):
1. `run.bat` என்ற கோப்பை டபுள் கிளிக் (Double Click) செய்யவும்.
2. அப்ளிகேஷன் உடனடியாகத் திறக்கும்!

*(ஏதேனும் மாற்றம் செய்தால் `compile.bat`-ஐ டபுள் கிளிக் செய்துவிட்டு பின் `run.bat` இயக்கவும்)*

### வழிமுறை 2 (கமாண்ட் லைன் மூலம்):
```bash
# கம்பைல் செய்ய:
compile.bat

# இயக்க:
run.bat
```

---

## 🛠️ MySQL Password மாற்றுவது எப்படி?
உங்கள் கணினியில் உள்ள MySQL ரூட் கடவுச்சொல் (Password) `root` இல்லை என்றால்:
1. அப்ளிகேஷனின் லாகின் திரையில் கீழே உள்ள **"⚙ Database Config"** பட்டனை கிளிக் செய்து உங்கள் MySQL பாஸ்வேர்டை கொடுத்து சேமிக்கலாம்.
2. அல்லது `db_config.properties` கோப்பை நோட்பேடில் திறந்து:
   ```properties
   db.user=root
   db.password=உங்கள்_mysql_பாஸ்வேர்ட்
   ```
   என்று மாற்றி சேமித்துக் கொள்ளலாம்.

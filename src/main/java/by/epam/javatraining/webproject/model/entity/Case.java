package by.epam.javatraining.webproject.model.entity;

public class Case extends Entity{

    private String admissionDate;
    private String dischargeDate;
    private String finalDiagnosis;
    private int medicalCardId;
    private int active;
    private int doctorId;
    private String complaints;

    public Case() {
    }

    public Case(String admissionDate, String dischargeDate, String finalDiagnosis, int medicalCardId, int doctorId, String complaints) {
        this.admissionDate = admissionDate;
        this.dischargeDate = dischargeDate;
        this.finalDiagnosis = finalDiagnosis;
        this.medicalCardId = medicalCardId;
        this.doctorId = doctorId;
        this.complaints = complaints;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public void setFinalDiagnosis(String finalDiagnosis) {
        this.finalDiagnosis = finalDiagnosis;
    }

    public int getMedicalCardId() {
        return medicalCardId;
    }

    public void setMedicalCardId(int medicalCardId) {
        this.medicalCardId = medicalCardId;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    @Override
    public String toString() {
        return "Case{" +
                "admissionDate='" + admissionDate + '\'' +
                ", dischargeDate='" + dischargeDate + '\'' +
                ", finalDiagnosis=" + finalDiagnosis +
                ", medicalCardId=" + medicalCardId +
                ", active=" + active +
                ", doctorId=" + doctorId +
                ", complaints='" + complaints + '\'' +
                '}';
    }
}

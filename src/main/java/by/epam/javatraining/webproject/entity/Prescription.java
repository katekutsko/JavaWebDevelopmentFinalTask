package by.epam.javatraining.webproject.entity;

import java.sql.Date;

public class Prescription extends Entity {

    private int cardId;
    private int doctorId;
    private String details;
    private String date;
    private PrescriptionType type;
    private int caseId;

    public Prescription() {
    }

    public Prescription(int id, int cardId, int doctorId, String details, String date, PrescriptionType type, int caseId) {
        super(id);
        this.cardId = cardId;
        this.doctorId = doctorId;
        this.details = details;
        this.date = date;
        this.type = type;
        this.caseId = caseId;
    }

    public Prescription(int cardId, int doctorId, String details, String date, PrescriptionType type, int caseId) {
        this.cardId = cardId;
        this.doctorId = doctorId;
        this.details = details;
        this.date = date;
        this.type = type;
        this.caseId = caseId;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PrescriptionType getType() {
        return type;
    }

    public void setType(PrescriptionType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "cardId=" + cardId +
                ", doctorId=" + doctorId +
                ", details='" + details + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                ", caseId=" + caseId +
                ", id=" + id +
                '}';
    }
}

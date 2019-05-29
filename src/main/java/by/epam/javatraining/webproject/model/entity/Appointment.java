package by.epam.javatraining.webproject.model.entity;

import java.util.Objects;

public class Appointment extends Entity {

    private int doctorId;
    private int patientId;
    private boolean active;
    private String date;

    public Appointment(int doctorId, int patientId, boolean active, String date) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.active = active;
        this.date = date;
    }

    public Appointment(int id, int doctorId, int patientId, boolean active, String date) {
        super(id);
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.active = active;
        this.date = date;
    }

    public Appointment() {
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return doctorId == that.doctorId &&
                patientId == that.patientId &&
                active == that.active &&
                date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctorId, patientId, active, date);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "doctorId=" + doctorId +
                ", patientId=" + patientId +
                ", active=" + active +
                ", date='" + date + '\'' +
                ", id=" + id +
                '}';
    }
}

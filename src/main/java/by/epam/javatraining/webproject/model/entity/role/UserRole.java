package by.epam.javatraining.webproject.model.entity.role;

public enum UserRole {
    DOCTOR(0), NURSE(1), PATIENT(2), ADMINISTRATOR(3);

    private int ordinal;

    private UserRole(int ordinal){
        this.ordinal = ordinal;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }
}

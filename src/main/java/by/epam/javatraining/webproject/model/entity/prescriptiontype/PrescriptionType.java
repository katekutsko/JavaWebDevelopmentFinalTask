package by.epam.javatraining.webproject.model.entity.prescriptiontype;

public enum PrescriptionType {

    MEDICINE(1),  PROCEDURE(1), OPERATION(0);

    private int access;

    private PrescriptionType(int access){
        this.access = access;
    }

    public int getAccess() {
        return access;
    }
}

package by.epam.javatraining.webproject.model.entity;

public class MedicalCard extends Entity {

    private int userID;
    private String dateOfBirth;
    private byte sex;

    public MedicalCard() {
    }

    public MedicalCard(int id, int userID, String dateOfBirth, byte sex) {
        super(id);
        this.userID = userID;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    public MedicalCard(int userID, String dateOfBirth, byte sex) {
        this.userID = userID;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "MedicalCard{" +
                "userID=" + userID +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", sex=" + sex +
                ", id=" + id +
                '}';
    }
}

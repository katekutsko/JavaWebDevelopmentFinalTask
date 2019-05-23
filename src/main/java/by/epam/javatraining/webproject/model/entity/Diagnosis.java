package by.epam.javatraining.webproject.model.entity;

public enum Diagnosis {
    UNDEFINED("undefined"), PNEUMONIA("pneumonia"), DISENTERIT("disenterit"), BOTULISM("botulism"),
    HEPATHITIS("hepathitis"), ANGINA("angina"), FLU("flu"), MENINGITIS("meningitis"), ENCEPHALITIS("encephalitis"),
    TRYCHINOSIS("trychinosis"), MEASLES("measles"), MALARIA("malaria");

    private int ordinal;
    private String key;

    private Diagnosis(String key){
        this.key = key;
        ordinal = ordinal();
    }

    public String getKey() {
        return key;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

}

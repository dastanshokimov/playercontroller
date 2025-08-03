package constants;

public enum Genders {
    MALE("male"),
    FEMALE("female");

    private final String value;

    Genders(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

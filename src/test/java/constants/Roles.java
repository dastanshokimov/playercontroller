package constants;

public enum Roles {
    ADMIN("admin"),
    USER("user"),
    SUPERVISOR("supervisor");

    private final String value;

    Roles(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

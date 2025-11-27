package content;

public enum Category {
    HEALTH("건강"),
    BEHAVIOR("행동"),
    FEEDING("식습관");

    private final String koName;

    private Category(String koName) {
        this.koName = koName;
    }

    public String getKoName() {
        return koName;
    }

    static Category valueFromKo(String token) {
        if (token == null)
            return null;
        return switch (token) {
            case "건강" -> HEALTH;
            case "행동" -> BEHAVIOR;
            case "식습관" -> FEEDING;
            default -> null;
        };
    }
}

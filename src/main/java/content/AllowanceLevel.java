package content;

public enum AllowanceLevel {
    FORBIDDEN("금지"),
    CAUTION("주의"),
    ALLOWED("허용");

    private final String koName;

    private AllowanceLevel(String koName) {
        this.koName = koName;
    }

    public String getKoName() {
        return koName;
    }

    static AllowanceLevel valueFromKo(String token) {
        if (token == null)
            return null;
        return switch (token) {
            case "금지" -> FORBIDDEN;
            case "주의" -> CAUTION;
            case "허용" -> ALLOWED;
            default -> null;
        };
    }
}

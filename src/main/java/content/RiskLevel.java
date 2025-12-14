package content;

public enum RiskLevel {
    HIGH("높음"),
    MEDIUM("중간"),
    LOW("낮음");

    private final String koName;

    private RiskLevel(String koName) {
        this.koName = koName;
    }

    public String getKoName() {
        return koName;
    }

    static RiskLevel valueFromKo(String token) {
        if (token == null)
            return null;
        return switch (token) {
            case "높음" -> HIGH;
            case "중간" -> MEDIUM;
            case "낮음" -> LOW;
            default -> null;
        };
    }
}

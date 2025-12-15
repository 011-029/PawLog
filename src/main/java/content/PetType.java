package content;

public enum PetType {
    DOG("강아지"),
    CAT("고양이");

    private final String koName;

    private PetType(String koName) {
        this.koName = koName;
    }

    public String getKoName() {
        return koName;
    }

    static PetType valueFromKo(String token) {
        if (token == null)
            return null;
        return switch (token) {
            case "강아지" -> DOG;
            case "고양이" -> CAT;
            default -> null;
        };
    }
}

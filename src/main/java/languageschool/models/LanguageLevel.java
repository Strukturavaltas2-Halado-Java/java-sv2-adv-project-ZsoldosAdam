package languageschool.models;

public enum LanguageLevel {

    NONE(0),
    BEGINNER(1),
    INTERMEDIATE(2),
    CONVERSATIONAL(3),
    FLUENT(4),
    NATIVE(5);

    private final int level;

    LanguageLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

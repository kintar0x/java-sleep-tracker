package ru.yandex.practicum.sleeptracker;

public enum SleepQuality {
    GOOD,
    NORMAL,
    BAD;

    public static SleepQuality fromString(String value) throws IllegalArgumentException {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Качество сна не может быть пустым");
        }
        return switch (value.toUpperCase().trim()) {
            case "GOOD" -> GOOD;
            case "NORMAL" -> NORMAL;
            case "BAD" -> BAD;
            default -> throw new IllegalArgumentException("Неизвестное качество сна: " + value);
        };
    }
}
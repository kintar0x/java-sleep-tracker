package ru.yandex.practicum.sleeptracker;

public class SleepAnalysisResult {
    private final String name;
    private final String value;

    public SleepAnalysisResult(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name + ": " + value;
    }
}
package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MaxSessionDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Максимальная продолжительность сна", "Нет данных");
        }

        long maxMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.bedtime(), session.wakeupTime()).toMinutes())
                .max()
                .orElseThrow();

        return new SleepAnalysisResult("Максимальная продолжительность сна", maxMinutes + " минут");
    }
}
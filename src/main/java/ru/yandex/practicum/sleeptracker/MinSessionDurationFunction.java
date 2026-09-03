package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MinSessionDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Минимальная продолжительность сна", "Нет данных");
        }

        long minMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.bedtime(), session.wakeupTime()).toMinutes())
                .min()
                .orElseThrow();

        return new SleepAnalysisResult("Минимальная продолжительность сна", minMinutes + " минут");
    }
}
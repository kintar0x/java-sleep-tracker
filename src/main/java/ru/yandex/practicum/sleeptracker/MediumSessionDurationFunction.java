package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class MediumSessionDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Средняя продолжительность сна", "Нет данных");
        }

        long sumMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.bedtime(), session.wakeupTime()).toMinutes())
                .sum();

        long averageMinutes = sumMinutes / sessions.size();

        return new SleepAnalysisResult("Средняя продолжительность сна", averageMinutes + " минут");
    }
}
package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", "Нет данных");
        }

        List<LocalDate> daysWithData = sessions.stream()
                .map(session -> session.bedtime().toLocalDate())
                .distinct()
                .sorted()
                .toList();

        long sleeplessNights = daysWithData.stream()
                .filter(date -> {
                    boolean hasNightSleep = sessions.stream()
                            .anyMatch(session ->
                                    session.bedtime().toLocalDate().equals(date)
                                            && session.bedtime().toLocalTime().isBefore(LocalTime.of(6, 0))
                                            && session.wakeupTime().toLocalTime().isAfter(LocalTime.of(0, 0))
                            );
                    return hasNightSleep;
                })
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", String.valueOf(sleeplessNights));
    }
}
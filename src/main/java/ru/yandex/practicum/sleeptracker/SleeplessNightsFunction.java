package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", "Нет данных");
        }

        LocalDateTime firstStart = sessions.stream()
                .map(SleepingSession::bedtime)
                .min(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDateTime lastEnd = sessions.stream()
                .map(SleepingSession::wakeupTime)
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDate firstNight = firstStart.getHour() >= 12
                ? firstStart.toLocalDate().plusDays(1)
                : firstStart.toLocalDate().minusDays(1);

        LocalDate lastNight = lastEnd.toLocalDate();

        long totalNights = Period.between(firstNight, lastNight).getDays() + 1;

        long sleeplessNights = LongStream.range(0, totalNights)
                .mapToObj(firstNight::plusDays)
                .filter(night -> sessions.stream()
                        .noneMatch(session ->
                                session.bedtime().isBefore(night.atTime(6, 0))
                                        && session.wakeupTime().isAfter(night.atStartOfDay())
                        )
                )
                .count();

        return new SleepAnalysisResult("Количество бессонных ночей", String.valueOf(sleeplessNights));
    }
}
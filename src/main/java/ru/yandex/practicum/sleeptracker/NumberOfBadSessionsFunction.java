package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public class NumberOfBadSessionsFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badSessions = sessions.stream()
                .filter(session -> session.qualitySleep() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult("Количество сессий с плохим качеством сна", String.valueOf(badSessions));
    }
}

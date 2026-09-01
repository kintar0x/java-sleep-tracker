package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

public class UserClassificationFunction implements Function<List<SleepingSession>, SleepAnalysisResult> {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Ваш хронотип", "Нет данных");
        }

        long owl = sessions.stream()
                .filter(session -> {
                    LocalTime bedtime = session.bedtime().toLocalTime();
                    LocalTime wakeup = session.wakeupTime().toLocalTime();
                    return !bedtime.isBefore(LocalTime.of(23, 0))
                            && !wakeup.isBefore(LocalTime.of(9, 0));
                })
                .count();

        long lark = sessions.stream()
                .filter(session -> {
                    LocalTime bedtime = session.bedtime().toLocalTime();
                    LocalTime wakeup = session.wakeupTime().toLocalTime();
                    return bedtime.isBefore(LocalTime.of(22, 0))
                            && wakeup.isBefore(LocalTime.of(7, 0));
                })
                .count();

        long pigeon = sessions.size() - owl - lark;

        String chronotype;
        if (owl > lark && owl > pigeon) {
            chronotype = "Сова";
        } else if (lark > owl && lark > pigeon) {
            chronotype = "Жаворонок";
        } else {
            chronotype = "Голубь";
        }

        return new SleepAnalysisResult("Ваш хронотип", chronotype);
    }
}
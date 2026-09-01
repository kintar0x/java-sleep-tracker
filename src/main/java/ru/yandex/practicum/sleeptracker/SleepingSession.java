package ru.yandex.practicum.sleeptracker;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * @param bedtime    время отбоя
 * @param wakeupTime время подъёма
 * @param qualitySleep качество сна
 */
public record SleepingSession(LocalDateTime bedtime, LocalDateTime wakeupTime, SleepQuality qualitySleep) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public static List<SleepingSession> fromLines(List<String> lines) throws DateTimeParseException {
        try {
            return lines.stream()
                    .filter(line -> !line.isBlank())
                    .map(line -> line.split(";"))
                    .filter(parts -> parts.length == 3)
                    .map(parts -> new SleepingSession(
                            LocalDateTime.parse(parts[0].trim(), FORMATTER),
                            LocalDateTime.parse(parts[1].trim(), FORMATTER),
                            SleepQuality.fromString(parts[2].trim())))
                    .toList();
        } catch (DateTimeParseException e) {
            throw new DateTimeException("Ожидается формат: dd.MM.yy HH:mm");
        }

    }

    @Override
    public String toString() {
        return String.format("Отбой: %s | Подъём: %s | Качество: %s",
                bedtime.format(FORMATTER),
                wakeupTime.format(FORMATTER),
                qualitySleep);
    }
}
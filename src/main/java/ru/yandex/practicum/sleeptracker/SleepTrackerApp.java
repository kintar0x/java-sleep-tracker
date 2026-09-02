package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {
    private static final List<Function<List<SleepingSession>, SleepAnalysisResult>> ANALYTICS_FUNCTIONS = List.of(
            new TotalSessionsFunction(),
            new MinSessionDurationFunction(),
            new MaxSessionDurationFunction(),
            new MediumSessionDurationFunction(),
            new NumberOfBadSessionsFunction(),
            new SleeplessNightsFunction(),
            new UserClassificationFunction()
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Ошибка: не указан путь к файлу с логом сна.");
            return;
        }

        String filePath = args[0];

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            List<SleepingSession> sessions = SleepingSession.fromLines(lines);
            if (sessions.isEmpty()) {
                System.out.println("Записи данных о сне отсутствуют.");
                return;
            }

            System.out.println("=== АНАЛИЗ СНА ===\n");

            ANALYTICS_FUNCTIONS.stream()
                    .map(f -> f.apply(sessions))
                    .forEach(System.out::println);

        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } catch (DateTimeParseException e) {
            System.out.println("Ошибка формата даты в файле: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }
}
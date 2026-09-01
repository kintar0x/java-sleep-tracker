package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {
    private List<SleepingSession> loadData() {
        try {
            return SleepingSession.fromLines(
                    Files.readAllLines(Paths.get(
                            "C:/Users/rtgbh/IdeaProjects/java-sleep-tracker/src/main/resources/sleep_log.txt"
                    ))
            );
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла: " + e.getMessage());
        }
    }

    private SleepingSession session(int d, int h, int m, int wd, int wh, int wm, SleepQuality q) {
        return new SleepingSession(
                LocalDateTime.of(2025, Month.OCTOBER, d, h, m),
                LocalDateTime.of(2025, Month.OCTOBER, wd, wh, wm),
                q
        );
    }

    // ======================================================================
    // 1. TotalSessionsFunction
    // ======================================================================
    @Test
    void totalSessions() {
        assertEquals("13", new TotalSessionsFunction().apply(loadData()).getValue());
    }

    @Test
    void totalSessionsEmpty() {
        assertEquals("0", new TotalSessionsFunction().apply(List.of()).getValue());
    }

    // ======================================================================
    // 2. MinSessionDurationFunction
    // ======================================================================
    @Test
    void minDuration() {
        assertEquals("45 минут", new MinSessionDurationFunction().apply(loadData()).getValue());
    }

    @Test
    void minDurationEmpty() {
        assertEquals("Нет данных", new MinSessionDurationFunction().apply(List.of()).getValue());
    }

    // ======================================================================
    // 3. MaxSessionDurationFunction
    // ======================================================================
    @Test
    void maxDuration() {
        assertEquals("500 минут", new MaxSessionDurationFunction().apply(loadData()).getValue());
    }

    @Test
    void maxDurationEmpty() {
        assertEquals("Нет данных", new MaxSessionDurationFunction().apply(List.of()).getValue());
    }

    // ======================================================================
    // 4. MediumSessionDurationFunction
    // ======================================================================
    @Test
    void averageDuration() {
        assertEquals("345 минут", new MediumSessionDurationFunction().apply(loadData()).getValue());
    }

    @Test
    void averageDurationEmpty() {
        assertEquals("Нет данных", new MediumSessionDurationFunction().apply(List.of()).getValue());
    }

    // ======================================================================
    // 5. NumberOfBadSessionsFunction
    // ======================================================================
    @Test
    void badSessions() {
        assertEquals("2", new NumberOfBadSessionsFunction().apply(loadData()).getValue());
    }

    @Test
    void badSessionsEmpty() {
        assertEquals("0", new NumberOfBadSessionsFunction().apply(List.of()).getValue());
    }

    // ======================================================================
    // 6. UserClassificationFunction
    // ======================================================================
    @Test
    void chronotype() {
        assertEquals("Голубь", new UserClassificationFunction().apply(loadData()).getValue());
    }

    @Test
    void chronotypeEmpty() {
        assertEquals("Нет данных", new UserClassificationFunction().apply(List.of()).getValue());
    }

    // ======================================================================
    // 7. SleeplessNightsFunction
    // ======================================================================
    @Test
    void sleeplessNightsFromFile() {
        assertEquals("1", new SleeplessNightsFunction().apply(loadData()).getValue());
    }

    @Test
    void sleeplessNightsAllGood() {
        var data = List.of(
                session(1, 23, 15, 2, 7, 30, SleepQuality.GOOD),
                session(2, 23, 50, 3, 6, 40, SleepQuality.NORMAL)
        );
        assertEquals("0", new SleeplessNightsFunction().apply(data).getValue());
    }

    @Test
    void sleeplessNightsOnlyDaySleep() {
        var data = List.of(
                session(1, 14, 0, 1, 15, 0, SleepQuality.NORMAL),
                session(2, 13, 30, 2, 14, 30, SleepQuality.GOOD)
        );
        assertEquals("0", new SleeplessNightsFunction().apply(data).getValue());
    }

    @Test
    void sleeplessNightsEmpty() {
        assertEquals("Нет данных", new SleeplessNightsFunction().apply(List.of()).getValue());
    }
}
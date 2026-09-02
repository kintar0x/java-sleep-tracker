package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepTrackerAppTest {

    private List<SleepingSession> loadData() {
        try {
            return SleepingSession.fromLines(Files.readAllLines(Paths.get("src/main/resources/sleep_log.txt")));
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

    @Test
    void chronotypeTieShouldReturnPigeon() {
        List<SleepingSession> data = List.of(
                //Совы
                session(1, 23, 30, 2, 9, 30, SleepQuality.GOOD),
                session(2, 23, 45, 3, 10, 0, SleepQuality.GOOD),
                // Жаворонки
                session(4, 21, 0, 5, 6, 30, SleepQuality.GOOD),
                session(5, 21, 30, 6, 6, 0, SleepQuality.GOOD)
        );
        UserClassificationFunction func = new UserClassificationFunction();
        SleepAnalysisResult result = func.apply(data);
        assertEquals("Голубь", result.getValue());
    }
    // ======================================================================
    // 7. SleeplessNightsFunction
    // ======================================================================
    @Test
    void sleeplessNightsFromFile() {
        assertEquals("20", new SleeplessNightsFunction().apply(loadData()).getValue());
    }

    @Test
    void sleeplessNightsAllGood() {
        List<SleepingSession> data = List.of(
                session(1, 23, 15, 2, 7, 30, SleepQuality.GOOD),
                session(2, 23, 50, 3, 6, 40, SleepQuality.NORMAL)
        );
        assertEquals("0", new SleeplessNightsFunction().apply(data).getValue());
    }

    @Test
    void sleeplessNightsOnlyDaySleep() {
        List<SleepingSession> data = List.of(
                session(1, 14, 0, 1, 15, 0, SleepQuality.NORMAL),
                session(2, 13, 30, 2, 14, 30, SleepQuality.GOOD)
        );
        assertEquals("1", new SleeplessNightsFunction().apply(data).getValue());
    }

    @Test
    void sleeplessNightsEmpty() {
        assertEquals("Нет данных", new SleeplessNightsFunction().apply(List.of()).getValue());
    }

    @Test
    void sleeplessNightsFirstSessionAfterMidnight() {
        List<SleepingSession> data = List.of(
                session(1, 2, 0, 1, 5, 0, SleepQuality.GOOD),
                session(2, 23, 0, 3, 7, 0, SleepQuality.GOOD)
        );
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.apply(data);
        assertEquals("1", result.getValue());
    }

    @Test
    void sleeplessNightsCrossMonthAllGood() {
        SleepingSession s1 = new SleepingSession(
                LocalDateTime.of(2025, Month.OCTOBER, 30, 23, 0),
                LocalDateTime.of(2025, Month.OCTOBER, 31, 7, 0),
                SleepQuality.GOOD
        );
        SleepingSession s2 = new SleepingSession(
                LocalDateTime.of(2025, Month.OCTOBER, 31, 23, 0),
                LocalDateTime.of(2025, Month.NOVEMBER, 1, 7, 0),
                SleepQuality.GOOD
        );
        SleepingSession s3 = new SleepingSession(
                LocalDateTime.of(2025, Month.NOVEMBER, 1, 23, 0),
                LocalDateTime.of(2025, Month.NOVEMBER, 2, 7, 0),
                SleepQuality.GOOD
        );
        List<SleepingSession> data = List.of(s1, s2, s3);
        SleeplessNightsFunction func = new SleeplessNightsFunction();
        SleepAnalysisResult result = func.apply(data);
        assertEquals("0", result.getValue());
    }
}
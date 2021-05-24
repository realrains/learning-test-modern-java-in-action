package net.realrain.chap12;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static org.junit.jupiter.api.Assertions.*;

class TimeTest {

    @Test
    void localDateTest() {
        LocalDate date = LocalDate.of(2021, 5, 23);

        assertEquals(2021, date.getYear());
        assertEquals(Month.MAY, date.getMonth());
        assertEquals(23, date.getDayOfMonth());
        assertEquals(DayOfWeek.SUNDAY, date.getDayOfWeek());
        assertFalse(date.isLeapYear());
        assertEquals(31, date.lengthOfMonth());

        assertEquals(2021, date.get(ChronoField.YEAR));
        assertEquals(5, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals(23, date.get(ChronoField.DAY_OF_MONTH));
        assertEquals(7, date.get(ChronoField.DAY_OF_WEEK));
        assertFalse(date.isLeapYear());
        assertEquals(31, date.lengthOfMonth());
    }

    @Test
    void localTimeTest() {
        LocalTime time = LocalTime.of(11, 0, 10);

        assertEquals(11, time.getHour());
        assertEquals(0, time.getMinute());
        assertEquals(10, time.getSecond());
    }

    @Test
    void parseDateAndTime() {
        LocalDate date = LocalDate.parse("2021-05-23");
        LocalTime time = LocalTime.parse("11:00:10");

        LocalDateTime dt1 = LocalDateTime.of(date, time);
        LocalDateTime dt2 = date.atTime(time);
        LocalDateTime d23 = time.atDate(date);
    }

    @Test
    void instant() {
        Instant.ofEpochSecond(1); // epoch time for machine
        Instant.now();
        Instant.ofEpochSecond(10, 100000000); // nano second adjustment
    }

    @Test
    void durationAndPeriod() {
        // Temporal interface : LocalDate, LocalTime, LocalDateTime, Instant
        LocalDateTime ldt1 = LocalDateTime.parse("2020-01-01T03:12:00");
        LocalDateTime ldt2 = LocalDateTime.now();

        Duration d1 = Duration.between(ldt1, ldt2); // duration express time as second
        Duration threeMinute = Duration.ofMinutes(3);

        Period tenDays = Period.ofDays(10);
        Period threeWeeks = Period.ofWeeks(3);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);
    }

    @Test
    void dateTimeWith() {
        // Temporal instances are immutable.
        LocalDate date = LocalDate.of(2021, 5, 24);
        LocalDate withYear = date.withYear(2020);
        LocalDate withDayOfMonth = date.withDayOfMonth(1);

        assertEquals(LocalDate.of(2020, 5, 24), withYear);
        assertEquals(LocalDate.of(2021, 5, 1), withDayOfMonth);
    }

    @Test
    void relativeTime() {
        LocalDate date = LocalDate.of(1994, 4, 26);
        LocalDate fiveWeeksLater = date.plusWeeks(5);
        LocalDate oneYearsBefore = date.minusYears(1);
    }

    @Test
    void temporalAdjustment() {
        LocalDate date = LocalDate.of(1994, 4, 26);
        LocalDate lastDayOfMonth = date.with(lastDayOfMonth());
        LocalDate nextSunday = date.with(nextOrSame(DayOfWeek.SUNDAY));
    }

    @Test
    void customTemporalAdjustment() {
        LocalDate date = LocalDate.of(2021, 5, 22);
        LocalDate nextWorkingDay = date.with(new NextWorkingDay());

        assertEquals(LocalDate.of(2021, 5, 24), nextWorkingDay);
    }

    @Test
    void dateFormatter() {
        LocalDate date = LocalDate.of(2014, 3, 18);
        String isoDate = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
        String isoLocalDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String customFormat = date.format(formatter);

        System.out.println(customFormat);
    }

    @Test
    void zoneId() {
        ZoneId korZone = ZoneId.of("Asia/Seoul");

    }
}

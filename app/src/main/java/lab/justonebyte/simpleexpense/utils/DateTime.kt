package lab.justonebyte.simpleexpense.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

// Constants for common date patterns.
private const val PATTERN_YEAR = "yyyy"
private const val PATTERN_YEAR_MONTH = "yyyy-MM"
private const val PATTERN_MONTH_READABLE = "MMMM"
private const val PATTERN_YEAR_MONTH_DAY = "yyyy-MM-dd"
private const val PATTERN_DAY_READABLE = "MMMM dd yyyy"
private const val PATTERN_DATE_TIME = "yyyy-MM-dd HH:mm:ss"

// The default time zone for all operations, which should match the system's.
private val defaultZoneId: ZoneId = ZoneId.systemDefault()

/**
 * Gets the year from a timestamp in "yyyy" format.
 *
 * @param timestamp The timestamp in milliseconds.
 * @param locale The locale for formatting.
 * @return The formatted year string.
 */
fun getFormattedYear(timestamp: Long, locale: Locale? = Locale.ENGLISH): String {
    return Instant.ofEpochMilli(timestamp)
        .atZone(defaultZoneId)
        .format(DateTimeFormatter.ofPattern(PATTERN_YEAR, locale))
}

/**
 * Gets the year and month from a timestamp in "yyyy-MM" format.
 *
 * @param timestamp The timestamp in milliseconds.
 * @param locale The locale for formatting.
 * @return The formatted month string.
 */
fun getFormattedMonth(timestamp: Long, locale: Locale? = Locale.ENGLISH): String {
    return Instant.ofEpochMilli(timestamp)
        .atZone(defaultZoneId)
        .format(DateTimeFormatter.ofPattern(PATTERN_YEAR_MONTH, locale))
}

/**
 * Gets the month from a timestamp in a readable format (e.g., "January").
 *
 * @param timestamp The timestamp in milliseconds.
 * @param locale The locale for formatting.
 * @return The readable month string.
 */
fun getReadableFormattedMonth(timestamp: Long, locale: Locale? = Locale.ENGLISH): String {
    return Instant.ofEpochMilli(timestamp)
        .atZone(defaultZoneId)
        .format(DateTimeFormatter.ofPattern(PATTERN_MONTH_READABLE, locale))
}

/**
 * Gets the date from a timestamp in "yyyy-MM-dd" format.
 *
 * @param timestamp The timestamp in milliseconds.
 * @param locale The locale for formatting.
 * @return The formatted day string.
 */
fun getFormattedDay(timestamp: Long, locale: Locale? = Locale.ENGLISH): String {
    return Instant.ofEpochMilli(timestamp)
        .atZone(defaultZoneId)
        .format(DateTimeFormatter.ofPattern(PATTERN_YEAR_MONTH_DAY, locale))
}

/**
 * Gets the date from a timestamp in a readable format (e.g., "January 01 2025").
 *
 * @param timestamp The timestamp in milliseconds.
 * @param locale The locale for formatting.
 * @return The readable day string.
 */
fun getReadableFormattedDay(timestamp: Long, locale: Locale? = Locale.ENGLISH): String {
    return Instant.ofEpochMilli(timestamp)
        .atZone(defaultZoneId)
        .format(DateTimeFormatter.ofPattern(PATTERN_DAY_READABLE, locale))
}

/**
 * Gets the current year in "yyyy" format.
 *
 * @param locale The locale for formatting.
 * @return The current year string.
 */
fun getCurrentYear(locale: Locale? = Locale.ENGLISH): String {
    return LocalDate.now(defaultZoneId).year.toString()
}

/**
 * Gets the current month in "yyyy-MM" format.
 *
 * @param locale The locale for formatting.
 * @return The current month string.
 */
fun getCurrentMonth(locale: Locale? = Locale.ENGLISH): String {
    return LocalDate.now(defaultZoneId).format(DateTimeFormatter.ofPattern(PATTERN_YEAR_MONTH, locale))
}

/**
 * Gets the current day in "yyyy-MM-dd" format.
 *
 * @param locale The locale for formatting.
 * @return The current day string.
 */
fun getCurrentDay(locale: Locale? = Locale.ENGLISH): String {
    return LocalDate.now(defaultZoneId).format(DateTimeFormatter.ofPattern(PATTERN_YEAR_MONTH_DAY, locale))
}

/**
 * Gets the day from a timestamp in "yyyy-MM-dd" format. This function's name is kept for API compatibility.
 *
 * @param timestampMillis The timestamp in milliseconds.
 * @param locale The locale for formatting.
 * @return The formatted day string.
 */
fun getCurrentDayFromTimestamp(timestampMillis: Long, locale: Locale = Locale.ENGLISH): String {
    return Instant.ofEpochMilli(timestampMillis)
        .atZone(defaultZoneId)
        .format(DateTimeFormatter.ofPattern(PATTERN_YEAR_MONTH_DAY, locale))
}

/**
 * Gets the timestamp for the start of a given year (00:00:00).
 *
 * @param year The year.
 * @return The timestamp in milliseconds.
 */
fun getTimeStampForYearStart(year: Int): Long {
    val startOfYear = LocalDate.of(year, 1, 1)
    return startOfYear.atStartOfDay(defaultZoneId).toInstant().toEpochMilli()
}

/**
 * Gets the timestamp for the end of a given year (23:59:59.999).
 *
 * @param year The year.
 * @return The timestamp in milliseconds.
 */
fun getTimeStampForYearEnd(year: Int): Long {
    val endOfYear = LocalDate.of(year, 12, 31)
    return endOfYear.atTime(23, 59, 59, 999_000_000).atZone(defaultZoneId).toInstant().toEpochMilli()
}

/**
 * Gets the timestamp for the start of a given day (00:00:00) from a date string.
 *
 * @param dateString The date string in "yyyy-MM-dd" format.
 * @return The timestamp in milliseconds.
 */
fun getTimeStampForStartDate(dateString: String): Long {
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(PATTERN_YEAR_MONTH_DAY))
        .atStartOfDay(defaultZoneId)
        .toInstant()
        .toEpochMilli()
}

/**
 * Gets the timestamp for the end of a given day (23:59:59.999) from a date string.
 *
 * @param dateString The date string in "yyyy-MM-dd" format.
 * @return The timestamp in milliseconds.
 */
fun getTimeStampForEndDate(dateString: String): Long {
    return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(PATTERN_YEAR_MONTH_DAY))
        .atTime(23, 59, 59, 999_000_000)
        .atZone(defaultZoneId)
        .toInstant()
        .toEpochMilli()
}

/**
 * Gets the timestamp for the start of a given month (00:00:00) from a "yyyy-MM" string.
 *
 * @param yearMonthString The year-month string.
 * @return The timestamp in milliseconds.
 */
fun getTimestampForMonthStart(yearMonthString: String): Long {
    val (year, month) = yearMonthString.split("-").map { it.toInt() }
    val startOfMonth = LocalDate.of(year, month, 1)
    return startOfMonth.atStartOfDay(defaultZoneId).toInstant().toEpochMilli()
}

/**
 * Gets the timestamp for the end of a given month (23:59:59.999) from a "yyyy-MM" string.
 *
 * @param yearMonthString The year-month string.
 * @return The timestamp in milliseconds.
 */
fun getTimestampForMonthEnd(yearMonthString: String): Long {
    val (year, month) = yearMonthString.split("-").map { it.toInt() }
    val endOfMonth = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1)
    return endOfMonth.atTime(23, 59, 59, 999_000_000).atZone(defaultZoneId).toInstant().toEpochMilli()
}

/**
 * Gets the timestamp from a date-time string in "yyyy-MM-dd HH:mm:ss" format.
 *
 * @param dateTimeString The date-time string.
 * @param locale The locale for parsing.
 * @return The timestamp in milliseconds, or 0 if parsing fails.
 */
fun getTimestampFromDateTimeString(dateTimeString: String, locale: Locale? = Locale.ENGLISH): Long {
    return try {
        val formatter = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME, locale)
        LocalDateTime.parse(dateTimeString, formatter)
            .atZone(defaultZoneId)
            .toInstant()
            .toEpochMilli()
    } catch (e: Exception) {
        0
    }
}

/**
 * This function has been kept for API compatibility but its internal logic is simplified and corrected.
 * The original logic with Calendar was complex and had potential bugs. The new code simply sets the time to 00:00:30 if it's midnight.
 *
 * @param timestampInMillis The original timestamp.
 * @return The modified timestamp if it was at midnight, otherwise the original timestamp.
 */
fun convertTimestampIfNeeded(timestampInMillis: Long): Long {
    val localDateTime = Instant.ofEpochMilli(timestampInMillis).atZone(defaultZoneId).toLocalDateTime()

    // Check if hours, minutes, and seconds are all zero.
    if (localDateTime.hour == 0 && localDateTime.minute == 0 && localDateTime.second == 0) {
        // Return a new timestamp with the time set to 00:00:30.
        return localDateTime.withSecond(30).atZone(defaultZoneId).toInstant().toEpochMilli()
    } else {
        return timestampInMillis
    }
}
package lab.justonebyte.simpleexpense

import lab.justonebyte.simpleexpense.utils.getTimeStampForEndDate
import lab.justonebyte.simpleexpense.utils.getTimeStampForStartDate
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DateUtilsTest {
    @Test
    fun convert_start_date_to_timestamp_isCorrect(){
        val dateLong = getTimeStampForStartDate("2024-03-14")
        assertEquals(dateLong,234230)
    }

    @Test
    fun convert_end_date_to_timestamp_isCorrect(){
        val dateLong = getTimeStampForEndDate("2024-03-14")
        assertEquals(dateLong,234230)
    }
}
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromLocalDate(value: LocalDate?): String? {
            return value?.format(DateTimeFormatter.ISO_LOCAL_DATE)
        }

        @TypeConverter
        @JvmStatic
        fun toLocalDate(value: String?): LocalDate? {
            return value?.let {
                LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE)
            }
        }
    }
}

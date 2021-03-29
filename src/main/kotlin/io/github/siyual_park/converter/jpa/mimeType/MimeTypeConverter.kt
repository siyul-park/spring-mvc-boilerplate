package io.github.siyual_park.converter.jpa.mimeType

import org.springframework.util.MimeType
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class MimeTypeConverter : AttributeConverter<MimeType, String> {
    override fun convertToDatabaseColumn(attribute: MimeType) = attribute.toString()

    override fun convertToEntityAttribute(dbData: String) = MimeType.valueOf(dbData)
}

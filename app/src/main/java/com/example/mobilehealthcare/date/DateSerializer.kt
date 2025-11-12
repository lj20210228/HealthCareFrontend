package com.example.mobilehealthcare.date

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

object StrictLocalDateSerializer: KSerializer<LocalDate> {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    override val descriptor: SerialDescriptor
            = PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    /**
     * Funckija koja LocalDate pretvara u string
     * @param value [LocalDate] objekat koji treba pretvoriti u string
     * @param encoder Enkoder koji upisuje podatke u JSON
     */
    override fun serialize(
        encoder: Encoder,
        value: LocalDate
    ) {
        encoder.encodeString(value.format(formatter))
    }
    /**
     * Deserijalizuje string iz JSON-a u objekat tipa [LocalDate].
     * Dozvoljen je isključivo format "yyyy-MM-dd"
     *
     * @param decoder Dekoder koji čita podatke iz JSON-a.
     * @return Objekat tipa [LocalDate] koji odgovara prosleđenom stringu.
     *
     * @throws IllegalArgumentException Ako je string u pogrešnom formatu.
     */
    override fun deserialize(decoder: Decoder): LocalDate {
        val dateStr=decoder.decodeString()
        try {
            return LocalDate.parse(dateStr,formatter)
        }catch (e: DateTimeParseException){
            throw IllegalArgumentException("Datum mora biti u formatu yyyy-MM-dd")
        }
    }

}
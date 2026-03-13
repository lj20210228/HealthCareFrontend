package com.example.mobilehealthcare.date


import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Serializer koji obezbeđuje da se datum i vreme serijalizuju i deserijalizuju
 * u standardnom ISO-8601 formatu "yyyy-MM-dd'T'HH:mm:ss".
 * Ovaj format je kompatibilan sa većinom modernih backend sistema (Spring, Ktor).
 * * @author Lazar Janković
 * @property descriptor Opisuje tip podatka za JSON (String).
 * @property formatter Koristi predefinisani ISO_LOCAL_DATE_TIME formater.
 *
 * @see java.time.LocalDateTime
 * @see java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
 */
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    /**
     * Serijalizuje [LocalDateTime] u ISO string format.
     * @param encoder Enkoder za upis u JSON.
     * @param value Objekat koji se šalje.
     */
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    /**
     * Deserijalizuje ISO string u [LocalDateTime] objekat.
     * @param decoder Dekoder koji čita JSON.
     * @return [LocalDateTime] instanca.
     * @throws IllegalArgumentException Ako format nije ISO-8601 (npr. nedostaje 'T').
     */
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val dateTimeStr = decoder.decodeString()
        return try {
            LocalDateTime.parse(dateTimeStr, formatter)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException(
                "Datum mora biti u ISO formatu yyyy-MM-ddTHH:mm:ss (npr. 2026-03-08T00:35:00)"
            )
        }
    }
}
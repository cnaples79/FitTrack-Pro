package com.fittrackpro.shared.data.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.KSerializer

class ListColumnAdapter<T>(
    private val serializer: KSerializer<List<T>>,
    private val json: Json = Json { ignoreUnknownKeys = true }
) : ColumnAdapter<List<T>, String> {
    override fun decode(databaseValue: String): List<T> =
        if (databaseValue.isEmpty()) emptyList() else json.decodeFromString(serializer, databaseValue)

    override fun encode(value: List<T>): String =
        if (value.isEmpty()) "" else json.encodeToString(serializer, value)
}

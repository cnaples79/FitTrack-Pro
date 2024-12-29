package com.fittrackpro.shared.data.adapters

import app.cash.sqldelight.ColumnAdapter

class EnumColumnAdapter<T : Enum<T>>(
    private val enumValues: Array<T>,
    private val encode: (T) -> String = { it.name },
    private val decode: (String) -> T = { enumValue -> enumValues.first { it.name == enumValue } }
) : ColumnAdapter<T, String> {
    override fun decode(databaseValue: String): T = decode(databaseValue)
    override fun encode(value: T): String = encode(value)
}

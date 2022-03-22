package com.gaur.weatherapp.utils

import java.sql.Date
import java.text.SimpleDateFormat

fun formatUptoTwoDecimal(value: Double): String {
    return "%.2f".format(value)
}

fun convertMilliTime(milli: Long): String {
    val date = Date(milli)
    val formatter = SimpleDateFormat("h:mm a")
    return formatter.format(date)
}

fun convertIntoDate(milli: Long): String {
    val formatter = SimpleDateFormat("MMM dd")
    return formatter.format(milli)
}

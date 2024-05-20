package com.ioappstudio.emicalculator.data

data class CalculateLoanInterest(
    var isShowResult: Boolean = false,
    val principal: Float,
    val interest: Float,
    val tenure: Float,
    val emi: Float = 0f
)

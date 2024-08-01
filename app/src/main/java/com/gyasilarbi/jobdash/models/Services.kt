package com.gyasilarbi.jobdash.models

data class Services(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val description: String = "",
    val amount: String = "",
    val location: String = "",
    val duration: String = "",
    val userName: String = "",
    val imageUri: String = "",
    val status: Int = 0 // 0 for inactive, 1 for active
)

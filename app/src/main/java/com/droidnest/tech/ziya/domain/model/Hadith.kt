package com.droidnest.tech.ziya.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Hadith(
    val id: Int,
    val arabic: String,
    val bangla: String,
    val reference: String,
    val category: String
)

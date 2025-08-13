package com.droidnest.tech.ziya.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Dua(
    val dua: String,
    val banglaPronunciation: String,
    val banglaMeaning: String,
    val reference: String,
    val whenToRecite: String
)
@Serializable
data class DuaList(
    val daily: List<Dua>,
    val namaz: List<Dua>
)

package com.example.math_duel.domain.models

/**
 * Модель игрока
 * @param id уникальный id
 * @param name имя игрока
 * @param score текущий счёт
 */
data class Player(
    val id: Int,
    val name: String,
    val score: Int = 0
)


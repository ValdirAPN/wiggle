package br.com.wiggles.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Animal(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: Gender,
    val imageUrl: String? = null,
    val distance: Long,
    val postedAt: LocalDateTime
) {
    companion object {
        fun fakeList() = listOf(
            Animal(
                id = 1,
                name = "Marley",
                age = 4,
                gender = Gender.MALE,
                distance = 1000L,
                postedAt = LocalDateTime.now()
            ),
            Animal(
                id = 2,
                name = "Toby",
                age = 2,
                gender = Gender.MALE,
                distance = 2000L,
                postedAt = LocalDateTime.now()
            ),
            Animal(
                id = 3,
                name = "Marry",
                age = 8,
                gender = Gender.FEMALE,
                distance = 100L,
                postedAt = LocalDateTime.now()
            )
        )
    }
}


enum class Gender {
    MALE, FEMALE
}

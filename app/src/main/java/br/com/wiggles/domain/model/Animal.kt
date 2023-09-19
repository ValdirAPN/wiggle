package br.com.wiggles.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Animal(
    val id: String,
    val name: String,
    val age: Int,
    val gender: Gender,
    val distance: Long,
    val postedAt: LocalDateTime
) {
    companion object {
        fun fakeList() = listOf(
            Animal(
                id = UUID.randomUUID().toString(),
                name = "Marley",
                age = 4,
                gender = Gender.MALE,
                distance = 1000L,
                postedAt = LocalDateTime.now()
            ),
            Animal(
                id = UUID.randomUUID().toString(),
                name = "Toby",
                age = 2,
                gender = Gender.MALE,
                distance = 2000L,
                postedAt = LocalDateTime.now()
            ),
            Animal(
                id = UUID.randomUUID().toString(),
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

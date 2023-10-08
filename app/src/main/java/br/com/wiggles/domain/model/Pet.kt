package br.com.wiggles.domain.model

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import java.time.LocalDateTime

data class Pet(
    val id: Int,
    val name: String,
    val description: String?,
    val age: String,
    val color: String?,
    val size: String,
    val gender: Gender,
    val petImage: PetImage? = null,
    val distance: Long,
    val postedAt: LocalDateTime
) {
    companion object {

        private const val LOREM_IPSUM = "Duis nec erat dolor. Nulla vitae consectetur ligula. " +
                "Quisque nec mi est. Ut quam ante, rutrum at pellentesque gravida, pretium in dui. " +
                "Cras eget sapien velit."
        fun fakeList() = listOf(
            Pet(
                id = 1,
                name = "Marley",
                description = LOREM_IPSUM,
                age = "Senior",
                color = "Tortoiseshell",
                size = "Medium",
                gender = Gender.MALE,
                distance = 1000L,
                postedAt = LocalDateTime.now()
            ),
            Pet(
                id = 2,
                name = "Toby",
                description = LOREM_IPSUM,
                age = "Senior",
                color = "Tortoiseshell",
                size = "Medium",
                gender = Gender.MALE,
                distance = 2000L,
                postedAt = LocalDateTime.now()
            ),
            Pet(
                id = 3,
                name = "Marry",
                description = LOREM_IPSUM,
                age = "Senior",
                color = "Tortoiseshell",
                size = "Medium",
                gender = Gender.FEMALE,
                distance = 100L,
                postedAt = LocalDateTime.now()
            )
        )
    }
}

data class PetImage(
    val mediumImageUrl: String? = null,
    val fullImageUrl: String? = null
)


enum class Gender {
    MALE, FEMALE
}

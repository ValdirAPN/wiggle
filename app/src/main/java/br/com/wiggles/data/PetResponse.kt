package br.com.wiggles.data

import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import br.com.wiggles.domain.model.Pet
import br.com.wiggles.domain.model.Gender
import br.com.wiggles.domain.model.PetImage
import java.time.LocalDateTime

data class PetsResponse(
    val animals: List<PetResponse>
)

data class SinglePetResponse(
    val animal: PetResponse
)

data class PetResponse(
    val id: Int,
    val type: String,
    val age: String,
    val colors: Map<String, String>,
    val size: String,
    val name: String,
    val description: String,
    val gender: String,
    val photos: List<Map<String, String>>
) {
    companion object {
        fun fakeList() = listOf(
            PetResponse(
                id = 1,
                type = "",
                age = "",
                colors = mapOf(),
                size = "",
                name = "Jon",
                description = LoremIpsum().toString(),
                gender = "male",
                photos = listOf()
            )
        )
    }
}

fun PetResponse.toPet() : Pet = Pet(
    id = id,
    name = name,
    age = age,
    color = colors["primary"],
    size = size,
    description = description,
    gender = Gender.valueOf(gender.uppercase()),
    petImage = PetImage(
        mediumImageUrl = photos.firstOrNull()?.get("medium"),
        fullImageUrl = photos.firstOrNull()?.get("full")
    ),
    distance = 0L,
    postedAt = LocalDateTime.MAX
)
package br.com.wiggles.data

import br.com.wiggles.domain.model.Animal
import br.com.wiggles.domain.model.Gender
import java.time.LocalDateTime

data class AnimalsResponse(
    val animals: List<AnimalResponse>
)

data class AnimalResponse(
    val id: Int,
    val type: String,
    val name: String,
    val gender: String,
    val photos: List<Map<String, String>>
)

fun AnimalResponse.toAnimal() : Animal = Animal(
    id = id,
    name = name,
    age = 0,
    gender = Gender.valueOf(gender.uppercase()),
    imageUrl = photos.firstOrNull()?.get("medium"),
    distance = 0L,
    postedAt = LocalDateTime.now()
)
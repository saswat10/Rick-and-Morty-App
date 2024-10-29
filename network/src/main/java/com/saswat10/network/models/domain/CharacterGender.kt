package com.saswat10.network.models.domain

sealed class CharacterGender(val displayName: String) {
    object Male: CharacterGender("Male")
    object Female: CharacterGender("Female")
    object Genderless: CharacterGender("No Gender")
    object Unknown: CharacterGender("Not Specified")
}

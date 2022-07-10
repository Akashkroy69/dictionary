package com.example.dictionary.feature_dictionary.data.remote.dto

data class MeaningDto(
    val antonyms: List<String>,
    val definitions: List<Definition>,
    val partOfSpeech: String,
    val synonyms: List<String>
)
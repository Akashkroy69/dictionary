package com.example.dictionary.feature_dictionary.data.remote.dto

data class Definition(
    val antonyms: List<String>,
    val definition: String,
    val example: String,
    val synonyms: List<Any>
)
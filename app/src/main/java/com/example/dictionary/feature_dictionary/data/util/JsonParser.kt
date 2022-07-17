package com.example.dictionary.feature_dictionary.data.util

import java.lang.reflect.Type

//This interface will contain two functions. one to get an object from a json string and the other to parse an object to a json string
interface JsonParser {

    fun <T> fromJson(json: String, type: Type): T?

    fun <T> toJson(obj: T, type: Type): String?
}
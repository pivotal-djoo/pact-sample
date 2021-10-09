package com.example.todo.models

import com.squareup.moshi.Json

data class ToDo(
    @Json(name = "id")
    var id: Long?,
    @Json(name = "text")
    var text: String?,
    @Json(name = "checked")
    var checked: Boolean?
)
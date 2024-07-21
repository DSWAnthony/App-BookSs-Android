package com.program.bookss.domain.model

data class Book(
    var id : String ="",
    var author : String = "",
    var title : String = "",
    var description : String = "",
    var imageUrl : String = "",
    var state : Boolean = false,
    var storageId :String ="",
    var userId : String = "",
)

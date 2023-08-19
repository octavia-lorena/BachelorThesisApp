package com.example.bachelorthesisapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class PostComment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val postId: Int,
    val authorId: String,
    val text: String
)

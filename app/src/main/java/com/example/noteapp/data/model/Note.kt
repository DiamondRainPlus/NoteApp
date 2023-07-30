package com.example.noteapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    val id: Int,
    val title: String,
    val desc: String,
    val date: String,
    val saveType: String
):Parcelable

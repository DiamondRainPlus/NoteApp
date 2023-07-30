package com.example.noteapp.data.source

import com.example.noteapp.data.model.Note


object Database {

    private val dailyNotes = mutableListOf<Note>()
    private val bookmarkNotes = mutableListOf<Note>()

    fun getDailyNotes()  = dailyNotes
    fun getBookmarkNotes()  = bookmarkNotes

    fun addDailyNote(title: String, desc: String, date: String, saveType: String) {
        dailyNotes.add(
            Note(
                id = (dailyNotes.lastOrNull()?.id ?: 0) + 1,
                title = title,
                desc = desc,
                date = date,
                saveType = saveType
            )
        )
    }

    fun addBookmarkNote(title: String, desc: String, date: String, saveType: String) {
        bookmarkNotes.add(
            Note(
                id = (bookmarkNotes.lastOrNull()?.id ?: 0) + 1,
                title = title,
                desc = desc,
                date = date,
                saveType = saveType
            )
        )
    }

    fun removeDailyNote(note: Note) = dailyNotes.remove(note)
    fun removeBookmarkNote(note: Note) = bookmarkNotes.remove(note)

    fun bookmarksToDaily(note: Note){
        bookmarkNotes.remove(note)
        dailyNotes.add(note)
    }

    fun dailyToBookmark(note: Note){
        dailyNotes.remove(note)
        bookmarkNotes.add(note)
    }

}
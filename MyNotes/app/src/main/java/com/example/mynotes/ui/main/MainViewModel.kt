package com.example.mynotes.ui.main

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.mynotes.models.NoteDetail
import java.lang.ClassCastException

class MainViewModel(val sharedPreferences: SharedPreferences) : ViewModel() {
    lateinit var note: NoteDetail
    lateinit var onNoteAdded: (() -> Unit)
    lateinit var removeNote: () -> Unit
    var decreaseOne: Int = -1

    val notes: MutableList<NoteDetail> by lazy {
        retrieveNotes()
    }

    private fun retrieveNotes(): MutableList<NoteDetail> {
        val sharedPreferencesContents = sharedPreferences.all
        val note = ArrayList<NoteDetail>()

        for (noteList in sharedPreferencesContents) {
            try {
                val itemNote = NoteDetail(noteList.key,noteList.value as String)
                note.add(itemNote)
            } catch (e: ClassCastException){
                Log.d(TAG,"ClassCast")
            }
        }
        return note
    }

    fun saveNote(note: NoteDetail) {
        val editor = sharedPreferences.edit()
        val text: String = note.Detail
        editor.putString(note.name, text)
        editor.apply()
        Log.d(ContentValues.TAG, note.Detail)
    }

    fun updateNote(note: NoteDetail) {
        val editor = sharedPreferences.edit()
        val text: String = note.Detail
        editor.putString(note.name, text)
        editor.apply()
        Log.d(ContentValues.TAG, note.Detail)
        refreshNotes()
    }

    fun createNote(note: NoteDetail) {
        val editor = sharedPreferences.edit()
        val text: String = note.Detail
        editor.putString(note.name, text)
        editor.apply()
        notes.add(note)
        onNoteAdded.invoke()
    }

    fun refreshNotes() {
        notes.clear()
        notes.addAll(retrieveNotes())
    }

    fun removeNote(note: NoteDetail) {
        val index = notes.indexOf(note)
        decreaseOne = index
        notes.remove(note)
        removeNote.invoke()

        val editor = sharedPreferences.edit()
        editor.remove(note.name)
        editor.apply()
    }

    fun find(key: String):Boolean {
        return sharedPreferences.contains(key)
    }
}
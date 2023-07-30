package com.example.noteapp.ui.bookmarknotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.model.Note
import com.example.noteapp.databinding.ItemBookmarkNoteBinding

class BookmarkNotesAdapter (
    private val onNoteClick: (Note) -> Unit,
    private val onCheckboxClick: (Note) -> Unit
): RecyclerView.Adapter<BookmarkNotesAdapter.BookmarkNoteViewHolder>() {

    private val noteList = mutableListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, ViewType: Int) =
        BookmarkNoteViewHolder(
            ItemBookmarkNoteBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false))

    override fun onBindViewHolder(holder: BookmarkNoteViewHolder, position: Int)
            = holder.bind(noteList[position])

    inner class BookmarkNoteViewHolder(private val binding: ItemBookmarkNoteBinding):
        RecyclerView.ViewHolder(binding.root){

        fun bind(note: Note) {
            with(binding){
                tvTitle.text = note.title
                tvDate.text= note.date
                checkBox.isChecked = true

                root.setOnClickListener{
                    onNoteClick(note)
                }

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (!isChecked){
                        onCheckboxClick(note)
                    }

                }

            }
        }
    }

    fun updateList(list: List<Note>) {
        noteList.clear()
        noteList.addAll(list)
        notifyItemRangeChanged(0, list.size)
    }

    override fun getItemCount() = noteList.size
}

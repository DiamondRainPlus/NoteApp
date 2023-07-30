package com.example.noteapp.ui.dailynotes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.data.model.Note
import com.example.noteapp.databinding.ItemDailyNoteBinding

class DailyNotesAdapter(
    private val onNoteClick: (Note) -> Unit,
    private val onCheckboxClick: (Note) -> Unit
): RecyclerView.Adapter<DailyNotesAdapter.DailyNoteViewHolder>() {

    private val noteList = mutableListOf<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, ViewType: Int) =
       DailyNoteViewHolder(
           ItemDailyNoteBinding.inflate(LayoutInflater.from(parent.context), parent,
        false))

    override fun onBindViewHolder(holder: DailyNoteViewHolder, position: Int)
        = holder.bind(noteList[position])

    inner class DailyNoteViewHolder(private val binding: ItemDailyNoteBinding):
        RecyclerView.ViewHolder(binding.root){

            fun bind(note: Note) {
                with(binding){
                    tvTitle.text = note.title
                    tvDate.text= note.date

                    root.setOnClickListener{
                        onNoteClick(note)
                    }

                    checkBox.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked){
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
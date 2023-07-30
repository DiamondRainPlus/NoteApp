package com.example.noteapp.ui.bookmarknotes

import android.app.AlertDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.common.showDatePicker
import com.example.noteapp.common.showTimePicker
import com.example.noteapp.common.viewBinding
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.source.Database
import com.example.noteapp.databinding.AddAlertDialogDesignBinding
import com.example.noteapp.databinding.FragmentBookmarkNoteBinding
import com.example.noteapp.databinding.FragmentDailyNoteBinding
import com.example.noteapp.ui.dailynotes.DailyNoteFragmentDirections
import com.example.noteapp.ui.dailynotes.DailyNotesAdapter

class BookmarkNoteFragment : Fragment(R.layout.fragment_bookmark_note) {

    private val binding by viewBinding(FragmentBookmarkNoteBinding::bind)

    private val bookmarkNotesAdapter = BookmarkNotesAdapter(::onNoteClick, ::onCheckboxClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            setData(Database.getBookmarkNotes())
            fabAdd.setOnClickListener {
                showAddDialog()
            }

        }
    }

    private fun showAddDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val binding = AddAlertDialogDesignBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()

        val saveTypeList = listOf("Daily", "Bookmark" )

        var selectedSaveType = ""

        var selectedDate = ""

        val saveTypeAdapter = ArrayAdapter(
            requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            saveTypeList
        )


        with(binding) {

            val calendar = Calendar.getInstance()

            actSaveType.setAdapter(saveTypeAdapter)

            actSaveType.setOnItemClickListener { _, _, position, _ ->
                selectedSaveType = saveTypeList[position]
            }


            switchDate.setOnCheckedChangeListener{_, isChecked ->
                if (isChecked){
                    showDatePicker(calendar){ year,month,day ->
                        showTimePicker(calendar) { hour, minute ->
                            tvDate.text = "$day.$month.$year - $hour:$minute"
                            tvDate.visibility = View.VISIBLE

                        }
                    }
                }
            }

            btnAdd.setOnClickListener {
                val title = etTitle.text.toString()
                val desc = etDesc.text.toString()


                if (title.isNotEmpty() && desc.isNotEmpty() && selectedSaveType.isNotEmpty()) {
                    if (selectedSaveType == "Daily"){
                        Database.addDailyNote(title, desc, selectedDate, selectedSaveType)
                    } else {
                        Database.addBookmarkNote(title, desc, selectedDate, selectedSaveType)
                        setData(Database.getBookmarkNotes())
                    }

                    dialog.dismiss()
                }
            }

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

        }

        dialog.show()
    }

    private fun setData(list: List<Note>) {
        binding.rvBookmarkNotes.adapter = bookmarkNotesAdapter
        bookmarkNotesAdapter.updateList(list)

    }

    private fun onNoteClick(note: Note) {

        val action = BookmarkNoteFragmentDirections.actionBookmarkNoteFragmentToDetailFragment(note).setIsBookmark(true)
        findNavController().navigate(action)

    }

    private fun onCheckboxClick(note: Note) {
        Database.bookmarksToDaily(note)
        setData(Database.getBookmarkNotes())
    }
}

package com.example.noteapp.ui.dailynotes

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.noteapp.R
import com.example.noteapp.common.showDatePicker
import com.example.noteapp.common.showTimePicker
import com.example.noteapp.common.viewBinding
import com.example.noteapp.data.model.Note
import com.example.noteapp.data.source.Database
import com.example.noteapp.databinding.AddAlertDialogDesignBinding
import com.example.noteapp.databinding.FragmentDailyNoteBinding
import java.time.Year
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR


class DailyNoteFragment : Fragment(R.layout.fragment_daily_note) {

    private val binding by viewBinding(FragmentDailyNoteBinding::bind)

    private val dailyNotesAdapter = DailyNotesAdapter(::onNoteClick, ::onCheckboxClick)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

           setData(Database.getDailyNotes())
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
                        setData(Database.getDailyNotes())
                    } else {
                        Database.addBookmarkNote(title, desc, selectedDate, selectedSaveType)
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
        binding.rvDailyNotes.adapter = dailyNotesAdapter
        dailyNotesAdapter.updateList(list)

    }

    private fun onNoteClick(note: Note) {
        val action = DailyNoteFragmentDirections.actionDailyNoteFragmentToDetailFragment(note).setIsDaily(true)
        findNavController().navigate(action)

    }

    private fun onCheckboxClick(note: Note) {
        Database.dailyToBookmark(note)
        setData(Database.getDailyNotes())
    }
}
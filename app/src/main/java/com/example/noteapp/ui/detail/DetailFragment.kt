package com.example.noteapp.ui.detail

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.noteapp.R
import com.example.noteapp.common.viewBinding
import com.example.noteapp.data.source.Database
import com.example.noteapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val binding by viewBinding (FragmentDetailBinding::bind)
    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            tvNoteTitle.text = args.note.title
            tvNoteDesc.text = args.note.desc
            tvNoteDate.text = args.note.date
        }

        delete()
        back()
    }

    private fun  delete () {
        binding.btnDetailDelete.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("Delete Note")
                .setMessage("Do you want to delete message?")
                .setPositiveButton("Yes"){_ ,_ ->
                    if (args.isDaily){
                        with(binding){
                            tvNoteTitle.text = ""
                            tvNoteDesc.text = ""
                            tvNoteDate.text = ""
                            btnDetailDelete.visibility = View.GONE
                        }
                        Toast.makeText(requireContext(), "Successfully!", Toast.LENGTH_SHORT).show()
                        Database.removeDailyNote(args.note)

                    }
                    else{
                        Toast.makeText(requireContext(), "Successfully!", Toast.LENGTH_SHORT).show()
                        Database.removeBookmarkNote(args.note)
                    }

                }
                .setNegativeButton("Cancel",null)
                .show()

        }

    }

    private fun back () {

        binding.btnBack.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToDailyNoteFragment()
            findNavController().navigate(action)
        }
    }

}
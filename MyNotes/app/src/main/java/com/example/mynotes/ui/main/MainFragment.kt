package com.example.mynotes.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.databinding.MainFragmentBinding
import com.example.mynotes.models.NoteDetail

class MainFragment() : Fragment(), NoteSelectionRecyclerViewAdapter.NoteSelectionRecyclerViewClickListener {
    private lateinit var binding: MainFragmentBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    var clickListener: MainFragmentInteractionListener? = null
    var holdClickListener: MainFragmentInteractionListener? = null

    interface MainFragmentInteractionListener {
        fun clicknotetitle(note: NoteDetail)
        fun noteItemHold(note: NoteDetail)
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)

        binding.noteRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(),
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(requireActivity()))).get(MainViewModel::class.java)

        val recyclerViewAdapter = NoteSelectionRecyclerViewAdapter(viewModel.notes, this,this)
        binding.noteRecyclerview.adapter = recyclerViewAdapter
        viewModel.onNoteAdded = {
            recyclerViewAdapter.notesUpdated()
        }
        viewModel.removeNote ={
            recyclerViewAdapter.notesRemove(viewModel.decreaseOne)

        }
    }

    override fun clickNoteTitle(note: NoteDetail) {
        clickListener?.clicknotetitle(note)
    }

    override fun HoldNameTitle(note: NoteDetail) {
        holdClickListener?.noteItemHold(note)
    }

}
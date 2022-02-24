package com.example.mynotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.mynotes.databinding.MainActivityBinding
import com.example.mynotes.models.NoteDetail
import com.example.mynotes.ui.detail.NoteDetailFragment
import com.example.mynotes.ui.main.MainFragment
import com.example.mynotes.ui.main.MainViewModel
import com.example.mynotes.ui.main.MainViewModelFactory

class  MainActivity : AppCompatActivity(), MainFragment.MainFragmentInteractionListener {

    private lateinit var binding: MainActivityBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            MainViewModelFactory(PreferenceManager.getDefaultSharedPreferences(this))
        ).get(MainViewModel::class.java)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this
            mainFragment.holdClickListener = this
            val fragmentContainerId : Int = if (binding.mainFragmentContainer == null) {
                R.id.container
            }else {
                R.id.main_fragment_container
            }
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerId, mainFragment)
            }
        }

        binding.addButton.setOnClickListener {
            popUpCreateNote()
        }
    }
    private fun popUpCreateNote() {
        val dialogTitle = getString(R.string.enterNameOfNote)
        val positiveButTitle = getString(R.string.createList)

        val builder = AlertDialog.Builder(this)
        val noteTitleEditText = EditText(this)
        noteTitleEditText.inputType = InputType.TYPE_CLASS_TEXT
        builder.setTitle(dialogTitle)
        builder.setView(noteTitleEditText)

        builder.setPositiveButton(positiveButTitle) { dialog, _ ->
            dialog.dismiss()
            if (viewModel.find(noteTitleEditText.text.toString())) {
                Toast.makeText(this,"The note name has been taken",Toast.LENGTH_LONG).show()
            }else {
                val noteList = NoteDetail(noteTitleEditText.text.toString(), "")
                viewModel.createNote(noteList)
                showNoteDetail(noteList)
            }
        }

        builder.create().show()
    }

    private fun showNoteDetail(note: NoteDetail) {
        if (binding.mainFragmentContainer == null) {
            val note = Intent(this, NoteDetailActivity::class.java)
            note.putExtra(INTENT_NOTE_KEY, note)
            startActivity(note)
        }else{
            val bundle = bundleOf(INTENT_NOTE_KEY to note)

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.noteFragment, NoteDetailFragment::class.java,bundle,null)
            }
        }
    }

    companion object {
        const val INTENT_NOTE_KEY = "note"
        const val NOTE_DETAIL_REQUEST_CODE = 123
    }

    override fun noteItemTapped(note: NoteDetail) {
        showNoteDetail(note)
    }

    override fun noteItemHold(note: NoteDetail) {
        popUpRemove(note)
    }

    private fun popUpRemove(note: NoteDetail) {
        val dialogTitle = "You removing note ${note.name}!"
        val positiveButtonTitle = "Yes"
        val negativeButtonTitle = "No"

        val builder = AlertDialog.Builder(this)

        builder.setTitle(dialogTitle)

        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            viewModel.removeNote(note)

            viewModel = ViewModelProvider(this,
                MainViewModelFactory(android.preference.PreferenceManager.getDefaultSharedPreferences(this))
            ).get(MainViewModel::class.java)
            binding = MainActivityBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val mainFragment = MainFragment.newInstance()
            mainFragment.clickListener = this
            mainFragment.holdClickListener = this
            val fragmentContainerViewId: Int = if (binding.mainFragmentContainer == null) {
                R.id.container }
            else {
                R.id.main_fragment_container
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(fragmentContainerViewId, mainFragment)
            }


            binding.addButton.setOnClickListener {
                popUpCreateNote()
            }

        }
        builder.setNegativeButton(negativeButtonTitle) { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NOTE_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                viewModel.updateNote(data.getParcelableExtra(INTENT_NOTE_KEY)!!)
            }
        }
    }

    override fun onBackPressed() {
        val noteNoteFragment = supportFragmentManager.findFragmentById(R.id.noteFragment)
        if (noteNoteFragment == null) {
            super.onBackPressed()
        } else {
            title = resources.getString(R.string.app_name)
            val editNoteText: EditText = findViewById(R.id.editTextNote)
            viewModel.saveNote(NoteDetail(viewModel.note.name,editNoteText.text.toString()))
            editNoteText.setText("")
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                remove(noteNoteFragment)
            }
            binding.addButton.setOnClickListener {
                popUpCreateNote()
            }
        }
    }
}

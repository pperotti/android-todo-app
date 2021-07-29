package com.pabloperotti.android.samples.todoapp.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.pabloperotti.android.samples.todoapp.R
import com.pabloperotti.android.samples.todoapp.data.models.ToDoData
import com.pabloperotti.android.samples.todoapp.fragments.SharedViewModel

class AddFragment : Fragment() {

    private val viewModel: AddViewModel by viewModels()
    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    // Widgets
    private lateinit var newNoteItem: EditText
    private lateinit var newNoteDescription: EditText
    private lateinit var newNoteSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the view with the appropriate layout
        val view = inflater.inflate(R.layout.add_fragment, container, false)

        // Get Items
        newNoteItem = view.findViewById(R.id.new_note_title)
        newNoteDescription = view.findViewById(R.id.new_note_description)
        newNoteSpinner = view.findViewById(R.id.new_note_priorities)

        // Enable Options Menu
        setHasOptionsMenu(true)

        // Spinner Item Selected Listener
        newNoteSpinner.onItemSelectedListener = sharedViewModel.listener

        return view
    }

//    // This is the most basic way to obtain the viewModel
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        viewModel = ViewModelProvider(this).get(AddViewModel::class.java)
//
//        // TODO: Use the ViewModel
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val title = newNoteItem.text.toString()
        val description = newNoteDescription.text.toString()
        val priority = sharedViewModel.parsePriority(newNoteSpinner.selectedItem.toString())

        if (sharedViewModel.verifyDataFromUser(title, description)) {
            val newNote = ToDoData(
                0,
                title,
                priority,
                description
            )
            todoViewModel.insertData(newNote)

            // Notify the user
            Toast.makeText(
                requireContext(),
                "Note Added!",
                Toast.LENGTH_LONG
            ).show()

            // Go back to the list
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please make sure you enter title and description!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
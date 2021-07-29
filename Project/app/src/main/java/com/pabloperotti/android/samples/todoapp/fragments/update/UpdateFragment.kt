package com.pabloperotti.android.samples.todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.pabloperotti.android.samples.todoapp.R
import com.pabloperotti.android.samples.todoapp.data.models.Priority
import com.pabloperotti.android.samples.todoapp.data.models.ToDoData
import com.pabloperotti.android.samples.todoapp.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    //private lateinit var viewModel: UpdateViewModel
    private val sharedViewModel: SharedViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()

    // Safe Args
    private val args by navArgs<UpdateFragmentArgs>()

    // Widgets
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var priorities: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.update_fragment, container, false)

        // Enable Options Menu
        setHasOptionsMenu(true)

        // Get the widgets
        title = view.findViewById<EditText>(R.id.note_title)
        description = view.findViewById<EditText>(R.id.note_description)
        priorities = view.findViewById<Spinner>(R.id.note_priorities)

        // Update the view with the information received from the safe args
        title.setText(args.current.title)
        description.setText(args.current.description)
        priorities.setSelection(
            parsePriority(args.current.priority)
        )
        priorities.onItemSelectedListener = sharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemDeletion()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val titleValue = title.text.toString()
        val descriptionValue = description.text.toString()
        val priority = sharedViewModel.parsePriority(priorities.selectedItem.toString())

        if (sharedViewModel.verifyDataFromUser(titleValue, descriptionValue)) {
            val newNote = ToDoData(
                args.current.id,
                titleValue,
                priority,
                descriptionValue
            )
            toDoViewModel.updateData(newNote)

            // Notify the user
            Toast.makeText(
                requireContext(),
                "Note Updated!",
                Toast.LENGTH_LONG
            ).show()

            //Return to the main view
            returnToMainList()
        }
    }

    fun parsePriority(priority: Priority): Int {
        return when (priority) {
            Priority.HIGH -> 0
            Priority.MEDIUM -> 1
            Priority.LOW -> 2
        }
    }

    fun returnToMainList() {
        // Go back to the list
        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    }

    fun confirmItemDeletion() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            toDoViewModel.deleteItem(args.current)
            Toast.makeText(
                requireContext(),
                "Successfully removed: ${args.current.title}",
                Toast.LENGTH_LONG
            ).show()
            returnToMainList()
        }
        builder.setNegativeButton("No", {_, _ ->})
        builder.setTitle("Delete '${args.current.title}'?")
        builder.setMessage("Are you sure you want to remove '${args.current.title}'?")
        builder.create().show()
    }
}
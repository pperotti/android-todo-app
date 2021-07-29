package com.pabloperotti.android.samples.todoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.viewmodel.ToDoViewModel
import com.pabloperotti.android.samples.todoapp.R
import com.pabloperotti.android.samples.todoapp.fragments.SharedViewModel

class ListFragment : Fragment() {

    private lateinit var viewModel: ListViewModel

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    // Empty data
    private lateinit var listNoData: ImageView
    private lateinit var listNoDataLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.list_fragment, container, false)

        // Get the empty views
        listNoData = view.findViewById(R.id.list_no_data)
        listNoDataLabel = view.findViewById(R.id.list_no_data_label)

        //Setup Recycler View
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        // Observe changes in the data
        toDoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        // Observe and update UI visibility when adapter is empty
        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, { isEmpty ->
            showEmptyDatabaseViews(isEmpty)
        })

        // Listen for Click on the floating button
        view.findViewById<View>(R.id.listAddNewNote).setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        // Enable Options Menu
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            toDoViewModel.deleteAll()
            confirmAllItemsRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmAllItemsRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            toDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully removed all items!",
                Toast.LENGTH_LONG
            ).show()
        }
        builder.setNegativeButton("No", {_, _ ->})
        builder.setTitle("Delete all items'?")
        builder.setMessage("Are you sure you want to remove all items?")
        builder.create().show()
    }

    private fun showEmptyDatabaseViews(emptyDatabase: Boolean) {
        if (emptyDatabase) {
            listNoData.visibility = View.VISIBLE
            listNoDataLabel.visibility = View.VISIBLE
        } else {
            listNoData.visibility = View.GONE
            listNoDataLabel.visibility = View.GONE
        }
    }
}
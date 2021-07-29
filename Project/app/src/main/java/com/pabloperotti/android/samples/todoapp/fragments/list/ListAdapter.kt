package com.pabloperotti.android.samples.todoapp.fragments.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.pabloperotti.android.samples.todoapp.R
import com.pabloperotti.android.samples.todoapp.data.models.Priority
import com.pabloperotti.android.samples.todoapp.data.models.ToDoData

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    // Responsible for drawing a single ToDoData element
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.row_note_title).text = dataList[position].title
        holder.itemView.findViewById<TextView>(R.id.row_note_description).text =
            dataList[position].description

        val priority = dataList[position].priority
        val priorityIndicator = holder.itemView.findViewById<CardView>(R.id.row_note_indicator)
        when (priority) {
            Priority.HIGH -> priorityIndicator.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.red)
            )
            Priority.MEDIUM -> priorityIndicator.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.yellow)
            )
            Priority.LOW -> priorityIndicator.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, R.color.green)
            )
        }

        holder.itemView.findViewById<View>(R.id.row_ackground).setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(data: List<ToDoData>) {
        dataList = data
        notifyDataSetChanged()
    }
}
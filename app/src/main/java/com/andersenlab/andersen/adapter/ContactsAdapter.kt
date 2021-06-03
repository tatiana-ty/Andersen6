package com.andersenlab.andersen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.andersenlab.andersen.utils.DiffCallback
import com.andersenlab.andersen.R
import com.andersenlab.andersen.model.Person
import com.bumptech.glide.Glide

class ContactsAdapter(
        private val contactsData: MutableList<Person>,
        private val context: Context
) :
        RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {

    private val data = mutableListOf<Person>()

    init {
        data.addAll(contactsData)
    }

    class MyViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.contactName)
        var picture: ImageView = itemView.findViewById(R.id.contactImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.contact_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = data[position].name
        Glide.with(context)
                .load(data[position].image)
                .into(holder.picture)
        holder.itemView.setOnClickListener {
            onClick(holder.adapterPosition)
        }
        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            onLongClick(data[holder.adapterPosition], holder.adapterPosition)
            return@OnLongClickListener true
        }
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    var onClick: (Int) -> Unit = { _ -> }

    var onLongClick: (Person, Int) -> Unit = { _, _ -> }

    fun onItemDismiss(position: Int) {
        val newData = mutableListOf<Person>()
        newData.addAll(contactsData)
        newData.removeAt(position)
        swap(newData)
    }

    private fun swap(newData: List<Person>) {
        val diffCallback = DiffCallback(contactsData, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        contactsData.clear()
        contactsData.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Person)
    }
}
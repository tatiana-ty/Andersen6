package com.andersenlab.andersen.utils

import androidx.recyclerview.widget.DiffUtil
import com.andersenlab.andersen.model.Person

class DiffCallback(
        private val oldList: List<Person>,
        private val newList: List<Person>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].phone == newList[newItemPosition].phone
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
                && oldList[oldItemPosition].phone == newList[newItemPosition].phone
                && oldList[oldItemPosition].image == newList[newItemPosition].image
    }
}
package com.example.firebasedemo.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasedemo.data.Friend
import com.example.firebasedemo.databinding.ItemFriendBinding

class FriendAdapter (
    val fn: (ViewHolder, Friend) -> Unit = { _, _ -> }
) : ListAdapter<Friend, FriendAdapter.ViewHolder>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(a: Friend, b: Friend)    = a.id == b.id
        override fun areContentsTheSame(a: Friend, b: Friend) = a == b
    }

    class ViewHolder(val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = getItem(position)

        holder.binding.txtId.text   = friend.id
        holder.binding.txtName.text = friend.name
        holder.binding.txtAge.text  = friend.age.toString()

        // TODO: Load photo blob (use extension method)
//        holder.binding.imgPhoto.setImageDrawable(null) // default doesn't work
        holder.binding.imgPhoto.setImageBlob(friend.photo)

        fn(holder, friend)
    }

}
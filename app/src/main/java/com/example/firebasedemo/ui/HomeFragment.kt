package com.example.firebasedemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.firebasedemo.R
import com.example.firebasedemo.data.Friend
import com.example.firebasedemo.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val nav by lazy { findNavController() }
    private val col = "friends"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.btnFriend.setOnClickListener { nav.navigate(R.id.listFragment) }
        binding.btnRead.setOnClickListener { read() }
        binding.btnSet.setOnClickListener { set() }
        binding.btnUpdate.setOnClickListener { update() }
        binding.btnDelete.setOnClickListener { delete() }

        return binding.root
    }

    private fun read() {
        Firebase.firestore
            .collection(col)
//            .document("A001") // to read one document
            .get()
            .addOnSuccessListener {
                val list = it.toObjects<Friend>()
                val text = list.map { f -> "${f.id} ${f.name} ${f.age}" }
//                val text = list.map { f -> "ID: ${f.id} \nName: ${f.name} \nAge: ${f.age} years old \n" }
                binding.txtResult.text = text.joinToString("\n")    // join with \n
            }
    }

    private fun set() {
        val f = Friend("A239", "Canny", 34)

        Firebase.firestore
            .collection(col)
            .document(f.id)
            .set(f)
            .addOnSuccessListener {
                toast("Inserted")
                read()
            }
    }

    private fun update() {
        Firebase.firestore
            .collection(col)
            .document("A003")
            .update("name", "Cindy", "age", 99)
            .addOnSuccessListener {
                toast("Updated")
                read()
            }
    }

    private fun delete() {
        Firebase.firestore
            .collection(col)
            .document("A004")
            .delete()
            .addOnSuccessListener {
                toast("Deleted")
                read()
            }
    }

    private fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}
package com.example.firebasedemo.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase

class FriendViewModel : ViewModel() {

    private val friends = MutableLiveData<List<Friend>>()

    // TODO: Initialization
    private val col = Firebase.firestore.collection("friends")  // ref

    init {  // code that will be executed in constructor
        col.addSnapshotListener { value, _ -> friends.value = value?.toObjects() }
    }

    // ---------------------------------------------------------------------------------------------

    // dummy function to allow the ViewModel to execute earlier
    fun init() = Unit // Void

    fun get(id: String) = friends.value?.find { it.id == id }

    fun getAll() = friends // TODO

    fun delete(id: String) {
        col.document(id).delete()

    }

    fun deleteAll() {
        friends.value?.forEach { col.document(it.id).delete() }
    }

    fun set(f: Friend) {
        col.document(f.id).set(f)
    }

    //----------------------------------------------------------------------------------------------

    private fun idExists(id: String) = friends.value?.any { it.id == id } ?: false

    fun validate(f: Friend, insert: Boolean = true): String {
        val regexId = Regex("""^[A-Z]\d{3}$""")
        var e = ""

        if (insert) {
            e += if (f.id == "") "- Id is required.\n"
            else if (!f.id.matches(regexId)) "- Id format is invalid (format: X999).\n"
            else if (idExists(f.id)) "- Id is duplicated.\n"
            else ""
        }

        e += if (f.name == "") "- Name is required.\n"
        else if (f.name.length < 3) "- Name is too short (at least 3 letters).\n"
        else ""

        e += if (f.age == 0) "- Age is required.\n"
        else if (f.age < 18) "- Underage (at least 18).\n"
        else ""

        // TODO: Validate if photo is empty
        e += if (f.photo.toBytes().isEmpty()) "- Photo is required.\n"
        else ""

        return e
    }
}
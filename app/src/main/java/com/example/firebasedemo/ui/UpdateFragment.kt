package com.example.firebasedemo.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasedemo.data.Friend
import com.example.firebasedemo.data.FriendViewModel
import com.example.firebasedemo.databinding.FragmentUpdateBinding
import com.example.firebasedemo.util.cropToBlob
import com.example.firebasedemo.util.errorDialog
import com.example.firebasedemo.util.setImageBlob
import java.text.SimpleDateFormat
import java.util.*

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding
    private val nav by lazy { findNavController() }
    private val vm: FriendViewModel by activityViewModels()

    private val id by lazy { arguments?.getString("id") ?: "" }
    private val formatter = SimpleDateFormat("dd MMMM yyyy '-' hh:mm:ss a", Locale.getDefault())

    private val launcher = registerForActivityResult(StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgPhoto.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentUpdateBinding.inflate(inflater, container, false)

        reset()
        binding.imgPhoto.setOnClickListener  { select() }
        binding.btnReset.setOnClickListener  { reset()  }
        binding.btnSubmit.setOnClickListener { submit() }
        binding.btnDelete.setOnClickListener { delete() }

        return binding.root
    }

    private fun select() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun reset() {
        // TODO: Get
        val f = vm.get(id)

        if (f == null) {
            nav.navigateUp()
            return
        }

        binding.txtId.text = f.id
        binding.edtName.setText(f.name)
        binding.edtAge.setText(f.age.toString())

        // TODO: Load photo and date
        binding.imgPhoto.setImageBlob(f.photo)
        binding.txtDate.text = formatter.format(f.date)

        binding.edtName.requestFocus()
    }

    private fun submit() {
        val f = Friend(
            id   = binding.txtId.text.toString().trim(),
            name = binding.edtName.text.toString().trim(),
            age  = binding.edtAge.text.toString().toIntOrNull() ?: 0,
            // TODO: Photo
            photo = binding.imgPhoto.cropToBlob(300,300)
        )

        val err = vm.validate(f, false) // prevent duplicated ID when update
        if (err != "") {
            errorDialog(err)
            return
        }

        // TODO: Set (update)
        vm.set(f)

        nav.navigateUp()
    }

    private fun delete() {
        // TODO: Delete
        vm.delete(id)

        nav.navigateUp()
    }

}
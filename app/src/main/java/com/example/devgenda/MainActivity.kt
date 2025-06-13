package com.example.devgenda

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devgenda.adapter.ContactAdapter
import com.example.devgenda.databinding.ActivityMainBinding
import com.example.devgenda.model.Contact
import com.example.devgenda.viewmodel.ContactViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ContactViewModel by viewModels()
    private lateinit var adapter: ContactAdapter
    private var selectedImageUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedImageUri = uri
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupRecyclerView()
        setupSearch()
        setupFab()
        observeContacts()
    }

    private fun setupRecyclerView() {
        adapter = ContactAdapter(
            onContactClick = { contact ->
                showContactDetails(contact)
            },
            onMenuClick = { contact ->
                showContactMenu(contact)
            }
        )

        binding.contactsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun setupSearch() {
        binding.searchEditText.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                lifecycleScope.launch {
                    viewModel.searchContacts(newText ?: "").collectLatest { contacts ->
                        adapter.submitList(contacts)
                    }
                }
                return true
            }
        })
    }

    private fun setupFab() {
        binding.addContactFab.setOnClickListener {
            showAddContactDialog()
        }
    }

    private fun observeContacts() {
        lifecycleScope.launch {
            viewModel.allContacts.collectLatest { contacts ->
                adapter.submitList(contacts)
            }
        }
    }

    private fun showAddContactDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        val nameEditText = dialogBinding.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.nameEditText)
        val phoneEditText = dialogBinding.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.phoneEditText)
        val emailEditText = dialogBinding.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.emailEditText)
        val imageButton = dialogBinding.findViewById<com.google.android.material.button.MaterialButton>(R.id.selectImageButton)

        imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Add Contact")
            .setView(dialogBinding)
            .setPositiveButton("Add") { _, _ ->
                val name = nameEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val email = emailEditText.text.toString()

                if (name.isNotBlank() && phone.isNotBlank()) {
                    val contact = Contact(
                        name = name,
                        phone = phone,
                        email = email,
                        imageUri = selectedImageUri?.toString()
                    )
                    viewModel.insertContact(contact)
                    selectedImageUri = null
                } else {
                    Snackbar.make(binding.root, "Please fill in all required fields", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showContactDetails(contact: Contact) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_contact_details, null)
        val nameTextView = dialogBinding.findViewById<android.widget.TextView>(R.id.nameTextView)
        val phoneTextView = dialogBinding.findViewById<android.widget.TextView>(R.id.phoneTextView)
        val emailTextView = dialogBinding.findViewById<android.widget.TextView>(R.id.emailTextView)
        val imageView = dialogBinding.findViewById<com.google.android.material.imageview.ShapeableImageView>(R.id.contactImageView)

        nameTextView.text = contact.name
        phoneTextView.text = contact.phone
        emailTextView.text = contact.email

        contact.imageUri?.let { uri ->
            com.bumptech.glide.Glide.with(this)
                .load(Uri.parse(uri))
                .circleCrop()
                .into(imageView)
        }

        MaterialAlertDialogBuilder(this)
            .setView(dialogBinding)
            .setPositiveButton("Close", null)
            .show()
    }

    private fun showContactMenu(contact: Contact) {
        val options = arrayOf("Edit", "Delete")
        MaterialAlertDialogBuilder(this)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditContactDialog(contact)
                    1 -> showDeleteConfirmationDialog(contact)
                }
            }
            .show()
    }

    private fun showEditContactDialog(contact: Contact) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_add_contact, null)
        val nameEditText = dialogBinding.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.nameEditText)
        val phoneEditText = dialogBinding.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.phoneEditText)
        val emailEditText = dialogBinding.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.emailEditText)
        val imageButton = dialogBinding.findViewById<com.google.android.material.button.MaterialButton>(R.id.selectImageButton)

        nameEditText.setText(contact.name)
        phoneEditText.setText(contact.phone)
        emailEditText.setText(contact.email)
        selectedImageUri = contact.imageUri?.let { Uri.parse(it) }

        imageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle("Edit Contact")
            .setView(dialogBinding)
            .setPositiveButton("Save") { _, _ ->
                val name = nameEditText.text.toString()
                val phone = phoneEditText.text.toString()
                val email = emailEditText.text.toString()

                if (name.isNotBlank() && phone.isNotBlank()) {
                    val updatedContact = contact.copy(
                        name = name,
                        phone = phone,
                        email = email,
                        imageUri = selectedImageUri?.toString()
                    )
                    viewModel.updateContact(updatedContact)
                    selectedImageUri = null
                } else {
                    Snackbar.make(binding.root, "Please fill in all required fields", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showDeleteConfirmationDialog(contact: Contact) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Delete Contact")
            .setMessage("Are you sure you want to delete ${contact.name}?")
            .setPositiveButton("Delete") { _, _ ->
                viewModel.deleteContact(contact)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
} 
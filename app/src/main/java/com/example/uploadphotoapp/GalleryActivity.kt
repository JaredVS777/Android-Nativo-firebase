package com.example.uploadphotoapp

import android.os.Bundle
import android.widget.GridView
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class GalleryActivity : AppCompatActivity() {

    private lateinit var storageReference: StorageReference
    private lateinit var gridView: GridView
    private lateinit var imageUrls: MutableList<String>
    private lateinit var adapter: ImageAdapter
    private lateinit var btnBack: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        gridView = findViewById(R.id.gridView)
        btnBack = findViewById(R.id.btnBack)

        imageUrls = mutableListOf()
        adapter = ImageAdapter(this, imageUrls)
        gridView.adapter = adapter

        storageReference = FirebaseStorage.getInstance().reference.child("uploads")

        fetchImages()

        btnBack.setOnClickListener {
            finish()  // Cierra la actividad actual y regresa a la anterior
        }
    }

    private fun fetchImages() {
        storageReference.listAll().addOnSuccessListener { listResult ->
            listResult.items.forEach { item ->
                item.downloadUrl.addOnSuccessListener { uri ->
                    imageUrls.add(uri.toString())
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to list images", Toast.LENGTH_SHORT).show()
        }
    }
}

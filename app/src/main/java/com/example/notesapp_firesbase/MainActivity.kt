package com.example.notesapp_firesbase

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.ItemTouchHelper

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.util.HashMap

import com.google.firebase.firestore.QuerySnapshot

import com.google.android.gms.tasks.OnCompleteListener


class MainActivity : AppCompatActivity() {
    private lateinit var myRV: RecyclerView
    private lateinit var rvAdapter: RVAdapter
    private lateinit var etNote: EditText
    private lateinit var btSave: Button
    private lateinit var noteBook: ArrayList<NoteBook>
    var db = FirebaseFirestore.getInstance()

    var TAG: String = "IamMainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //instructions pop
        startAlert()
        noteBook = arrayListOf()

        //set up UI
        myRV = findViewById(R.id.rvMain)
        etNote = findViewById(R.id.etNote)
        btSave = findViewById(R.id.btSave)
        rvAdapter = RVAdapter()
        myRV.adapter = rvAdapter
        myRV.layoutManager = LinearLayoutManager(applicationContext)
        noteBook = getDate()

        //swipe gestures
        val swipeGestures = object : SwipeGestures(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        rvAdapter.deleteData(noteBook[viewHolder.adapterPosition])
                        noteBook = getDate()
                        Toast.makeText(this@MainActivity, "note deleted", LENGTH_SHORT).show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        val dialogBuilder =
                            androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)

                        val input = EditText(this@MainActivity)
                        dialogBuilder.setCancelable(false)
                        input.setText(noteBook[viewHolder.adapterPosition].Note)
                        dialogBuilder.setMessage("Enter your updated note:")
                            // positive button text and action
                            .setPositiveButton("save", DialogInterface.OnClickListener { _, id ->
                                if ((input.text.isNotEmpty()) || (input.equals(""))) {
                                    val updatedNoteBook =
                                        NoteBook(
                                            noteBook[viewHolder.adapterPosition].pk,
                                            input.text.toString()
                                        )
                                    rvAdapter.editData(updatedNoteBook)
                                    noteBook.add(viewHolder.adapterPosition, updatedNoteBook)
                                    noteBook.removeAt(viewHolder.adapterPosition)
                                    noteBook = getDate()
                                } else {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Please enter a value",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                Toast.makeText(
                                    this@MainActivity,
                                    "Note updated",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            })
                            // negative button text and action
                            .setNegativeButton("Cancel") { dialog, _ ->
                                dialog.cancel()
                                rvAdapter.update(noteBook)
                            }
                        // create dialog box
                        val alert = dialogBuilder.create()
                        alert.setTitle("Update")
                        alert.setView(input)
                        alert.show()

                    }

                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGestures)
        touchHelper.attachToRecyclerView(myRV)
        //button interactions
        //CRUD: 1- save data to Firestore
        btSave.setOnClickListener {
            val user: MutableMap<String, Any> = HashMap()
            val input = etNote.text
            if ((input.isNotEmpty()) || (input.equals(""))) {
                user["note"] = input.toString()

                // Add a new document with a generated ID
                db.collection("notes")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(this, "Added successfully to database", Toast.LENGTH_SHORT)
                            .show()

                        Log.d(
                            TAG,
                            "DocumentSnapshot added with ID: " + documentReference.id
                        )
                        noteBook.add(NoteBook(documentReference.id, etNote.text.toString()))
                        rvAdapter.update(noteBook)
                        etNote.text.clear()
                    }.addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }

            } else {
                Toast.makeText(
                    this,
                    "Please enter a value",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }

    }

    //CRUD: 2- get data from Firestore
    private fun getDate(): ArrayList<NoteBook> {
        var notes = arrayListOf<NoteBook>()
        db.collection("notes")
            .get()
            .addOnCompleteListener(OnCompleteListener<QuerySnapshot> { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        document.data.map { (key, value) ->
//                            noteBook = arrayListOf(NoteBook(document.id, "$value"))
                            notes.add(NoteBook(document.id, "$value"))

                            Log.d(
                                TAG,
                                document.data.values.toString() + " => " + document.data.keys
                            )
                        }
                    }
                    rvAdapter.update(notes)
                } else {
                    Log.w(TAG, "Error getting documents.", task.exception)
                }
            })
        noteBook.distinct()
        return notes
    }

    private fun startAlert() {
        val dialogBuilder = Dialog(this)
        dialogBuilder.setContentView(R.layout.start_dialog)
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.dialog_window)

        val okButton = dialogBuilder.findViewById<Button>(R.id.btOk)
        okButton.setOnClickListener {

            dialogBuilder.dismiss()
        }



        dialogBuilder.show()
    }

}
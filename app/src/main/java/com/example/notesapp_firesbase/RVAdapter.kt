package com.example.notesapp_firesbase

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp_firesbase.databinding.ItemRowBinding
import com.google.firebase.firestore.FirebaseFirestore


class RVAdapter() :
    RecyclerView.Adapter<RVAdapter.ItemViewHolder>() {
    private var noteBook = arrayListOf<NoteBook>()
    lateinit var viewContext: Context
    val db = FirebaseFirestore.getInstance()

    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        viewContext = parent.context
        return ItemViewHolder(
            ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val cards = noteBook[position]
        holder.binding.apply {
            tvContent.text = cards.Note.toString()
            tvPk.text = "no.$position"

        }
    }

    override fun getItemCount(): Int = noteBook.size

    fun update(notes: ArrayList<NoteBook>) {
        this.noteBook = notes
        this.notifyDataSetChanged()
    }

    //CRUD: 3- update data to Firestore
    fun editData(Note: NoteBook) {
        db.collection("notes").document("${Note.pk}").update("note", Note.Note)
    }

    //CRUD: 4- delete data from Firestore
    fun deleteData(Note: NoteBook) {
        db.collection("notes")
            .document("${Note.pk}").delete()
    }
}

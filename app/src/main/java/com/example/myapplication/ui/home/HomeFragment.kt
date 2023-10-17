package com.example.myapplication.ui.home

import CustomAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import android.widget.ListView



class HomeFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var notes: MutableList<MutableList<String>>
    private lateinit var editTitleNote: EditText
    private lateinit var editTextNote: EditText
    private lateinit var btnAjouter: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        listView = view.findViewById(R.id.listeNotes)
        editTitleNote = view.findViewById(R.id.editTitleNote)
        editTextNote = view.findViewById(R.id.editTextNote)
        btnAjouter = view.findViewById(R.id.btnAjouter)

        notes = mutableListOf() // Initialisez votre liste de listes vide

        val adapter = CustomAdapter(requireContext(), notes)
        listView.adapter = adapter

        // Lorsque le bouton d'ajout est cliqué
        btnAjouter.setOnClickListener {
            val title = editTitleNote.text.toString()
            val note = editTextNote.text.toString()

            if (title.isNotEmpty() && note.isNotEmpty()) {
                val newNote: MutableList<String> = mutableListOf(title, note)
                notes.add(newNote)
                adapter.notifyDataSetChanged()
                // Effacez les champs de texte après avoir ajouté la note
                editTitleNote.text.clear()
                editTextNote.text.clear()
            } else {
                // Affichez un message d'erreur si l'un des champs est vide
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
        // Supprimer la note lors d'un glissement
        listView.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                // Récupérer la position de l'élément touché
                val position = listView.pointToPosition(motionEvent.x.toInt(), motionEvent.y.toInt())
                // Supprimer l'élément à cette position
                if (position != AdapterView.INVALID_POSITION) {
                    notes.removeAt(position)
                    adapter.notifyDataSetChanged()
                }
            }
            true
        }


        return view
    }
}
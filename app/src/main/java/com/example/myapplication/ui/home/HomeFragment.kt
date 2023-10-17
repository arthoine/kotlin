package com.example.myapplication.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val editTextNote: EditText = root.findViewById(R.id.editTextNote)
        val buttonAdd: Button = root.findViewById(R.id.buttonAdd)
        val textViewSavedNotes: TextView = root.findViewById(R.id.textViewSavedNotes)

        buttonAdd.setOnClickListener {
            val noteText = editTextNote.text.toString()

            val sharedPreferences = requireContext().getSharedPreferences("Notes", Context.MODE_PRIVATE)

            // Récupérer toutes les notes existantes depuis les préférences partagées
            val notesSet = sharedPreferences.getStringSet("notesKey", mutableSetOf()) ?: mutableSetOf()

            // Ajouter la nouvelle note à la liste des notes
            notesSet.add(noteText)

            // Mettre à jour la liste des notes dans les préférences partagées
            val editor = sharedPreferences.edit()
            editor.putStringSet("notesKey", notesSet)
            editor.apply()

            // Afficher toutes les notes
            textViewSavedNotes.text = notesSet.joinToString("\n")
        }

        // Reste du code...

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
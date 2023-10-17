package com.example.myapplication.ui.home

import CustomAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import android.widget.ListView
import androidx.core.content.ContextCompat
import kotlin.math.abs


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

        val threshold = 500 // seuil en pixels
        var selectedView: View? = null
        var startX = 0f
        var startY = 0f
        var originalX = 0f
        var originalY = 0f

        listView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Trouvez l'élément de la liste sous le point de toucher
                    val position = listView.pointToPosition(motionEvent.x.toInt(), motionEvent.y.toInt())
                    selectedView = listView.getChildAt(position - listView.firstVisiblePosition)

                    // Enregistrez les coordonnées de départ et les coordonnées originales
                    selectedView?.let {
                        startX = motionEvent.x - it.x
                        startY = motionEvent.y - it.y
                        originalX = it.x
                        originalY = it.y
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    // Déplacez l'élément de la liste sélectionné uniquement horizontalement
                    selectedView?.let {
                        val newX = motionEvent.x - startX
                        // Restreindre le mouvement à l'axe X uniquement
                        it.x = newX

                        // Vérifiez si l'élément a été déplacé de plus de 500 pixels dans n'importe quelle direction
                        if (Math.abs(newX - originalX) > threshold) {
                            // Si oui, changez la couleur de fond de l'élément en rouge
                            it.setBackgroundColor(Color.RED)
                        } else {
                            // Sinon, réinitialisez la couleur de fond à sa valeur par défaut
                            it.setBackgroundColor(Color.WHITE)
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // Réinitialisez l'élément de la liste sélectionné à sa position d'origine
                    selectedView?.let {
                        it.x = originalX
                        it.y = originalY
                        it.setBackgroundColor(Color.WHITE)
                        selectedView = null

                        // Vérifiez si l'utilisateur a relâché l'élément au-delà du seuil
                        if (Math.abs(it.x - originalX) > threshold) {
                            // Si oui, supprimez l'élément de la liste
                            // Assurez-vous d'avoir la référence à l'objet à supprimer
                            // listView.removeView(it) // Supprimez l'élément de la vue
                        }
                    }
                }
            }
            true
        }



        return view
    }
}
package com.example.myapplication.ui.home

import CustomAdapter
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
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

        notes = mutableListOf()

        val adapter = CustomAdapter(requireContext(), notes)
        listView.adapter = adapter

        btnAjouter.setOnClickListener {
            val title = editTitleNote.text.toString()
            val note = editTextNote.text.toString()

            if (title.isNotEmpty() && note.isNotEmpty()) {
                val newNote: MutableList<String> = mutableListOf(title, note)
                notes.add(newNote)
                adapter.notifyDataSetChanged()
                editTitleNote.text.clear()
                editTextNote.text.clear()
            } else {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }

        val threshold = 500 // seuil en pixels
        var selectedView: View? = null
        var startX = 0f
        var originalX = 0f
        var originalY = 0f

        listView.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    val position = listView.pointToPosition(motionEvent.x.toInt(), motionEvent.y.toInt())
                    selectedView = listView.getChildAt(position - listView.firstVisiblePosition)

                    selectedView?.let {
                        startX = motionEvent.rawX
                        originalX = it.x
                        originalY = it.y
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    selectedView?.let {
                        val newX = motionEvent.rawX - startX
                        it.x = originalX + newX

                        if (abs(newX) > threshold) {
                            it.setBackgroundColor(Color.RED)
                            val paillettesAnimation = AnimationUtils.loadAnimation(context, R.anim.paillettes)
                            it.startAnimation(paillettesAnimation)
                        } else {
                            it.setBackgroundColor(Color.WHITE)
                            it.clearAnimation()
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    selectedView?.let {
                        it.x = originalX
                        it.y = originalY
                        it.clearAnimation()
                        it.setBackgroundColor(Color.WHITE)
                        selectedView = null

                        val totalXMoved = abs(motionEvent.rawX - startX)
                        if (totalXMoved > threshold) {
                            val position = listView.getPositionForView(it)
                            if (position != AdapterView.INVALID_POSITION) {
                                notes.removeAt(position)
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
            true
        }
        return view
    }
}
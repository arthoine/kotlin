import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAdapter(context: Context, private val notes: MutableList<MutableList<String>>) :
    ArrayAdapter<MutableList<String>>(context, android.R.layout.simple_list_item_2, notes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)

        val title = notes[position][0]
        val note = notes[position][1]

        view.findViewById<TextView>(android.R.id.text1).text = title
        view.findViewById<TextView>(android.R.id.text2).text = note

        return view
    }
}

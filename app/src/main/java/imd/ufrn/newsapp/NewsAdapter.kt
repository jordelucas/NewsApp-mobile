package imd.ufrn.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(
    private var context: Context,
    private var newsList: List<News>
    ) : BaseAdapter() {

    fun updateList(newsMList: MutableList<News>) {
        newsList = newsMList
    }

    override fun getCount(): Int = newsList.size

    override fun getItem(position: Int): Any = newsList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val news = newsList[position]
        val holder: ViewHolder
        val row: View

        if(convertView == null) {
            // Nova posição do adapter
            row = LayoutInflater.from(context)
                .inflate(R.layout.item_news, parent, false)
            holder = ViewHolder(row)
            row.tag = holder
        }
        else {
            row = convertView
            holder = convertView.tag as ViewHolder
        }

        holder.txtTitulo.text = news.titulo
        /*holder.txtTitulo.setOnClickListener {
            Toast.makeText(context, "$position: Funciona!", Toast.LENGTH_SHORT)
        }*/
         holder.txtData.text = news.date.toString()
        // holder.txtTempo.text = news.tempo.toString() + "min"

        return row
    }

    companion object {
        data class ViewHolder(val view: View) {
            val txtTitulo: TextView = view.findViewById(R.id.txtItemTitulo)
            val txtData: TextView = view.findViewById(R.id.txtItemData)
            val txtTempo: TextView = view.findViewById(R.id.txtItemTempo)
        }
    }
}
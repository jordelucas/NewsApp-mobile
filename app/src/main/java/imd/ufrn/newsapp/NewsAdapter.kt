package imd.ufrn.newsapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.sql.Date

class NewsAdapter(
    private var context: Context,
    private var newsList: List<News>
    ) : BaseAdapter() {

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

        holder.txtTitulo.text = "Título da notícia"
        holder.txtData.text = Date.valueOf("01-01-2001").toString()
        holder.txtTempo.text = "30min"

        return row
    }

    companion object {
        data class ViewHolder(val view: View) {
            val txtTitulo: TextView = view.findViewById(R.id.txt_titulo)
            val txtData: TextView = view.findViewById(R.id.txt_data)
            val txtTempo: TextView = view.findViewById(R.id.txt_tempo)
        }
    }
}
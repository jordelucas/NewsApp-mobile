package imd.ufrn.newsapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import imd.ufrn.newsapp.*
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity(), HTTPGetNewsList.UpdateNewsListListener {

    private lateinit var adapter: NewsAdapter
    private var newsList = mutableListOf<News>()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.getStringExtra("id")
        val userName = intent.getStringExtra("name")
        user = User(userId as String, userName as String)

        adapter = NewsAdapter(this, newsList)
        val lvNews: ListView = findViewById(R.id.lvNews)
        lvNews.adapter = adapter

        lvNews.setOnItemClickListener {
            parent, view, position, id ->
            // Toast.makeText(this, "$position: Funciona!", Toast.LENGTH_SHORT).show()
            val task = HTTPGetNewsById(
                    this,
                    "http://10.0.0.103:3333/news/",
                    newsList.get(position).id,
                    user.id
            )
            task.execute()
        }
    }

    override fun onResume() {
        super.onResume()
        val task = HTTPGetNewsList(
                this,
                this,
                "http://10.0.0.103:3333/user/",
                user.id,
                newsList
        )
        task.execute()
    }

    override fun updateNewsList(newsMList: MutableList<News>) {
        // adapter.updateList(newsMList)
        adapter.notifyDataSetChanged()
    }
}
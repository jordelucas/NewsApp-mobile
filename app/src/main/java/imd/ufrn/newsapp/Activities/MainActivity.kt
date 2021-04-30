package imd.ufrn.newsapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import imd.ufrn.newsapp.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

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

        val task = HTTPGetNewsList(
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
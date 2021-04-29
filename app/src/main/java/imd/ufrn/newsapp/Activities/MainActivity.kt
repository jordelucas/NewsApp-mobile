package imd.ufrn.newsapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import imd.ufrn.newsapp.News
import imd.ufrn.newsapp.NewsAdapter
import imd.ufrn.newsapp.R
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NewsAdapter
    private var newsList = mutableListOf<News>()


    val newsListTest = mutableListOf(
            News("Notícia 1", "Este é o corpo da primeira notícia", Date(), 1),
            News("Notícia 2", "Este é o corpo da segunda notícia", Date(), 1),
            News("Notícia 3", "Este é o corpo da terceira notícia", Date(), 1),
            News("Notícia 4", "Este é o corpo da quarta notícia", Date(), 1),
            News("Notícia 5", "Este é o corpo da quinta notícia", Date(), 1),
            News("Notícia 6", "Este é o corpo da sexta notícia", Date(), 1),
            News("Notícia 7", "Este é o corpo da sétima notícia", Date(), 1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = NewsAdapter(this, newsListTest)
        val lvNews: ListView = findViewById(R.id.lvNews)
        lvNews.adapter = adapter

    }
}
package imd.ufrn.newsapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import imd.ufrn.newsapp.News
import imd.ufrn.newsapp.NewsAdapter
import imd.ufrn.newsapp.R

class MainActivity : AppCompatActivity() {

    private var news = mutableListOf<News>()

    private var adapter = NewsAdapter(this, news)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
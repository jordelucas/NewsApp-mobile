package imd.ufrn.newsapp.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import imd.ufrn.newsapp.HTTPRead
import imd.ufrn.newsapp.News
import imd.ufrn.newsapp.R

class NewsActivity : AppCompatActivity() {

    // private lateinit var news: News

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        findViewById<TextView>(R.id.txtMainNome)
                .text = intent.getStringExtra("author")
        findViewById<TextView>(R.id.txtNewsViewTitulo)
                .text = intent.getStringExtra("title")
        findViewById<TextView>(R.id.txtNewsViewTexto)
                .text = intent.getStringExtra("description")
        findViewById<TextView>(R.id.txtNewsDate)
                .text = intent.getStringExtra("date")
        findViewById<TextView>(R.id.txtNewsTime)
                .text = intent.getStringExtra("time") + " min read"
        findViewById<TextView>(R.id.txtNewsViews)
                .text = intent.getStringExtra("views") + " visualizações"

        val userId = intent.getStringExtra("userId")
        val newsId = intent.getStringExtra("id")

        val task = HTTPRead(
                this,
                "http://10.0.0.103:3333/read",
                userId as String,
                newsId as String
        )
        task.execute()
    }


}
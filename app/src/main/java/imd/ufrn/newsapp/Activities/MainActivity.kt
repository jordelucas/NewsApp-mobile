package imd.ufrn.newsapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import imd.ufrn.newsapp.*


class MainActivity : AppCompatActivity(), HTTPGetNewsList.UpdateNewsListListener {

    private lateinit var layLogout: LinearLayout
    private lateinit var txtName: TextView
    private lateinit var btnGerarConvite: ImageButton

    private lateinit var adapter: NewsAdapter
    private var newsList = mutableListOf<News>()
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layLogout = findViewById(R.id.content_logout)
        txtName = findViewById(R.id.txtMainNome)
        btnGerarConvite = findViewById(R.id.btnGerarConvite)

        val userId = intent.getStringExtra("id")
        val userName = intent.getStringExtra("name")
        user = User(userId as String, userName as String)

        txtName.setText(user.name)

        adapter = NewsAdapter(this, newsList)
        val lvNews: ListView = findViewById(R.id.lvNews)
        lvNews.adapter = adapter

        lvNews.setOnItemClickListener {
            parent, view, position, id ->
            // Toast.makeText(this, "$position: Funciona!", Toast.LENGTH_SHORT).show()
            val task = HTTPGetNewsById(
                    this,
                    "http://10.0.2.2:3333/news/",
                    newsList.get(position).id,
                    user.id
            )
            task.execute()
        }

        btnGerarConvite.setOnClickListener {
            generateConvite()
            //Toast.makeText(this, "hehehe", Toast.LENGTH_SHORT).show()
        }

        layLogout.setOnClickListener {
            logout()
        }
    }

    override fun onResume() {
        super.onResume()
        val task = HTTPGetNewsList(
                this,
                this,
                "http://10.0.2.2:3333/user/",
                user.id,
                newsList
        )
        task.execute()
    }

    override fun updateNewsList(newsMList: MutableList<News>) {
        // adapter.updateList(newsMList)
        adapter.notifyDataSetChanged()
    }

    fun generateConvite() {
        val task = HTTPGenerateConvite(
            this,
            "http://10.0.2.2:3333/invitation",
            user.id
        )
        task.execute()
    }

    fun logout() {
        val intent = Intent(this, InitiationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
    }
}
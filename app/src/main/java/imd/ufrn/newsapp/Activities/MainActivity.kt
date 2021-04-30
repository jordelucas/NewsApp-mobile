package imd.ufrn.newsapp.Activities

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.ListView
import imd.ufrn.newsapp.News
import imd.ufrn.newsapp.NewsAdapter
import imd.ufrn.newsapp.R
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var layLogout: LinearLayout

    private lateinit var adapter: NewsAdapter
    private var newsList = mutableListOf<News>()


    /*val newsListTest = mutableListOf(
            News("Notícia 1", "Este é o corpo da primeira notícia", Date(), 1),
            News("Notícia 2", "Este é o corpo da segunda notícia", Date(), 1),
            News("Notícia 3", "Este é o corpo da terceira notícia", Date(), 1),
            News("Notícia 4", "Este é o corpo da quarta notícia", Date(), 1),
            News("Notícia 5", "Este é o corpo da quinta notícia", Date(), 1),
            News("Notícia 6", "Este é o corpo da sexta notícia", Date(), 1),
            News("Notícia 7", "Este é o corpo da sétima notícia", Date(), 1)
    )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layLogout = findViewById(R.id.content_logout)

        layLogout.setOnClickListener {
            finish()
        }

        adapter = NewsAdapter(this, newsList)
        val lvNews: ListView = findViewById(R.id.lvNews)
        lvNews.adapter = adapter

    }

    class HTTPGetNewsList(
            private var context: Context,
            private var urlAddress: String,
            private var userId: String
    ): AsyncTask<Int, Int, Unit>() {

        private var TAG = "mainTag"
        private var response = String()

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            // A fazer
            // Parser
        }

        override fun doInBackground(vararg params: Int?) {
            var urlConnection: HttpURLConnection? = null

            try {
                val getData = JSONObject()
                getData.put("user_id", userId)

                val url = URL(urlAddress)
                urlConnection = url.openConnection() as HttpURLConnection

                Log.i(TAG, urlAddress)
                Log.i(TAG, getData.toString())

                // Fluxo de saída para a requisição (envio)
                val out: OutputStream = BufferedOutputStream(urlConnection.outputStream)
                val writer = BufferedWriter(OutputStreamWriter(
                        out, "UTF-8"))
                writer.write(getData.toString())
                writer.flush()

                // Verificar conexão bem sucedida
                val code = urlConnection.responseCode
                if (code != 200) {
                    throw IOException("Server response $code")
                }

                // Fluxo de entrada para a requisição (resposta)
                val rd = BufferedReader(
                        InputStreamReader(
                                urlConnection.inputStream
                        )
                )

                val responseString = StringBuffer()
                var line: String?
                while (rd.readLine().also { line = it } != null) {
                    responseString.append(line + "\n")
                }

                response = responseString.toString()
                Log.i(TAG, response)
            }

            catch (e: Exception) {
                e.printStackTrace();
            } finally {
                urlConnection?.disconnect()
            }
        }

    }
}
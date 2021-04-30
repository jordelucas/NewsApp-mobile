package imd.ufrn.newsapp

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class HTTPGetNewsList(
        private var context: Context,
        private var listener: UpdateNewsListListener,
        private var urlAddress: String,
        private var userId: String,
        private var newsMList: MutableList<News>
): AsyncTask<Int, Int, Unit>() {

    private var TAG = "mainTag"
    private var response = String()
    private var error = String()

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

        if(!error.isEmpty()) {
            Toast.makeText(context, "Opa! Há algo de errado", Toast.LENGTH_SHORT).show()
            return
        }

        Log.i(TAG, response)
        val responseArray = JSONArray(response)
        var responseData: JSONObject

        newsMList.clear()
        for (i in 0 until responseArray.length()) {
            responseData = responseArray.getJSONObject(i)

            val id = responseData.getString("id")
            val title = responseData.getString("title")
            val date = responseData.getString("created_at")
            // val user_id = responseData.getString("id")
            val time = responseData.getString("time_to_read")

            val news = News(id, title, null, date, null, time)
            newsMList.add(news)
        }

        // update
        Log.i(TAG, "newsArray lenght = ${newsMList.size}")
        listener.updateNewsList(newsMList)
    }

    override fun doInBackground(vararg params: Int?) {
        var urlConnection: HttpURLConnection? = null

        try {

            val url = URL(urlAddress + userId + "/news")
            urlConnection = url.openConnection() as HttpURLConnection

            Log.i(TAG, urlAddress)

            // Verificar conexão bem sucedida
            val code = urlConnection.responseCode
            if (code != 200) {
                error = "$code"
                return
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
        } catch (e: Exception) {
            e.printStackTrace();
        } finally {
            urlConnection?.disconnect()
        }
    }

    interface UpdateNewsListListener {
        fun updateNewsList(newsMList: MutableList<News>)
    }
}
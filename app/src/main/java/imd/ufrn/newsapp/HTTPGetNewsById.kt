package imd.ufrn.newsapp

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import imd.ufrn.newsapp.Activities.MainActivity
import imd.ufrn.newsapp.Activities.NewsActivity
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HTTPGetNewsById(
    private var context: Context,
    private var urlAddress: String,
    private var newsId: String,
    private var userId: String
    ): AsyncTask<Int, Int, Unit>() {

        private var TAG = "mainTag"
        private var response = String()

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            Log.i(TAG, response)
            var responseData = JSONObject(response)


            val id = responseData.getString("id")
            val title = responseData.getString("title")
            val description = responseData.getString("description")

            val intent = Intent(context, NewsActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("title", title)
            intent.putExtra("description", description)
            intent.putExtra("userId", userId)

            context.startActivity(intent)

            // update
        }

        override fun doInBackground(vararg params: Int?) {
            var urlConnection: HttpURLConnection? = null

            try {

                val url = URL(urlAddress + newsId)
                urlConnection = url.openConnection() as HttpURLConnection

                Log.i(TAG, urlAddress)

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
            } catch (e: Exception) {
                e.printStackTrace();
            } finally {
                urlConnection?.disconnect()
            }
        }
}
package imd.ufrn.newsapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import imd.ufrn.newsapp.Activities.MainActivity
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class HTTPRead(
        private var context: Context,
        private var urlAddress: String,
        private var userId: String,
        private var newsId: String
):
        AsyncTask<Int, Int, Unit>() {

    private var response = String()
    private var error = String()

    private val TAG = "mainTag"
    override fun doInBackground(vararg params: Int?) {
        var urlConnection: HttpURLConnection? = null

        try {
            val postData = JSONObject()
            postData.put("user_id", userId)
            postData.put("news_id", newsId)

            // Estabelecer conexão
            val url = URL(urlAddress)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.requestMethod = "POST"
            urlConnection.doOutput = true
            urlConnection.doInput = true

            Log.i(TAG, urlAddress)
            Log.i(TAG, postData.toString())

            // Fluxo de saída para a requisição (envio)
            val out: OutputStream = BufferedOutputStream(urlConnection.outputStream)
            val writer = BufferedWriter(OutputStreamWriter(
                    out, "UTF-8"))
            writer.write(postData.toString())
            writer.flush()

            // Verificar conexão bem sucedida
            val code = urlConnection.responseCode
            if (code != 200) {
                Toast.makeText(context, "Opa! Há algo de errado", Toast.LENGTH_SHORT).show()
                return
            }

            // Fluxo de entrada para a requisição (resposta)
            val rd = BufferedReader(
                    InputStreamReader(
                            urlConnection.inputStream
                    )
            )

            /*val responseString = StringBuffer()
            var line: String?
            while (rd.readLine().also { line = it } != null) {
                responseString.append(line + "\n")
            }

            response = responseString.toString()*/
            Log.i(TAG, response)
        }

        catch (e: Exception) {
            e.printStackTrace();
        } finally {
            urlConnection?.disconnect()
        }
    }

}
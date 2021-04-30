package imd.ufrn.newsapp

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import imd.ufrn.newsapp.Activities.NewsActivity
import imd.ufrn.newsapp.Activities.SignUpActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HTTPValidateConvite(
    private var context: Context,
    private var urlAddress: String,
    private var conviteId: String
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
        var responseData = JSONObject(response)

        val isActive = responseData.getBoolean("isActive")
        if(!isActive) {
            Toast.makeText(context, "Esse convite expirou!", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(context, SignUpActivity::class.java)
        context.startActivity(intent)
    }

    override fun doInBackground(vararg params: Int?) {
        var urlConnection: HttpURLConnection? = null

        try {

            val url = URL(urlAddress + conviteId)
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
}
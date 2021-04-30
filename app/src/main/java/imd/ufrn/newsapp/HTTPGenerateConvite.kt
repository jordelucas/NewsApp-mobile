package imd.ufrn.newsapp

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.AsyncTask
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class HTTPGenerateConvite(
    private var context: Context,
    private var urlAddress: String,
    private var userId: String
):
    AsyncTask<Int, Int, Unit>() {

    private var response = String()
    private var error = String()

    private val TAG = "mainTag"

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

        if(!error.isEmpty()) {
            Toast.makeText(context, "Opa! Há algo de errado", Toast.LENGTH_SHORT).show()
            return
        }

        Log.i(TAG, response)
        val responseData = JSONObject(response)

        val id = responseData.getString("id")

        val myClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE)
                as ClipboardManager
        val myClip = ClipData.newPlainText("id", id)
        myClipboard.setPrimaryClip(myClip)
        Toast.makeText(context, "Convite copiado!", Toast.LENGTH_SHORT).show()

    }

    override fun doInBackground(vararg params: Int?) {
        var urlConnection: HttpURLConnection? = null

        try {
            val postData = JSONObject()
            postData.put("user_id", userId)

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
            val writer = BufferedWriter(
                OutputStreamWriter(
                    out, "UTF-8"
                )
            )
            writer.write(postData.toString())
            writer.flush()

            // Verificar conexão bem sucedida
            val code = urlConnection.responseCode
            if (code != 201) {
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
        }

        catch (e: Exception) {
            e.printStackTrace();
        } finally {
            urlConnection?.disconnect()
        }
    }

}
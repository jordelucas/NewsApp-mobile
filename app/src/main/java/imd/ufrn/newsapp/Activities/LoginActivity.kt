package imd.ufrn.newsapp.Activities

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import imd.ufrn.newsapp.R
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {

    private lateinit var edTxtEmail: TextView
    private lateinit var edTxtSenha: TextView
    private lateinit var txtVoltar: Button
    private lateinit var btnLogin: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edTxtEmail = findViewById(R.id.edTxtLoginEmail)
        edTxtSenha = findViewById(R.id.edTxtLoginSenha)
        txtVoltar = findViewById(R.id.txtLoginVoltar)
        btnLogin = findViewById(R.id.btnLogin)

        txtVoltar.setOnClickListener {
            finish()
        }

        btnLogin.setOnClickListener {
            loginAuthentication()
        }
    }

    fun loginAuthentication() {
        var task = HTTPAuthenticationTask(
                "http://10.0.0.103:3333/authentication",
                edTxtEmail.text.toString(),
                edTxtSenha.text.toString()
        )
        task.execute()
    }

    class HTTPAuthenticationTask(
            private var urlAddress: String,
            private var email: String,
            private var password: String
    ):
        AsyncTask<Int, Int, Unit>() {

        private val TAG = "authTag"

        override fun doInBackground(vararg params: Int?) {
            var urlConnection: HttpURLConnection? = null

            try {
                val postData = JSONObject()
                postData.put("email", email)
                postData.put("password", password)

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

                Log.i(TAG, responseString.toString())
            }

            catch (e: Exception) {
                e.printStackTrace();
            } finally {
                urlConnection?.disconnect()
            }

        }

    }
}
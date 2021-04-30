package imd.ufrn.newsapp.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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
    private val TAG = "authTag"

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

    private fun loginAuthentication() {

        val task = HTTPAuthenticationTask(
                this,
                "http://10.0.2.2:3333/authentication",
                edTxtEmail.text.toString(),
                edTxtSenha.text.toString()
        )
        task.execute()
    }

    class HTTPAuthenticationTask(
            private var context: Context,
            private var urlAddress: String,
            private var email: String,
            private var password: String,
    ):
        AsyncTask<Int, Int, Unit>() {

        private lateinit var pd: ProgressDialog

        private var error = String()
        private var response = String()

        override fun onPreExecute() {
            super.onPreExecute()

            pd = ProgressDialog(context)
            pd.setTitle("Autenticação")
            pd.setMessage("Aguarde...")
            pd.show()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            pd.dismiss()

            if(!error.isEmpty()) {
                val msg: String
                msg = when(error) {
                    "400" -> "Campos obrigatórios incorretos ou não informados!"
                    "404" -> "Usuário não encontrado"
                    else -> "Há algo de errado!"
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                return
            }

            Log.i(TAG, response)
            val responseData = JSONObject(response)

            val id = responseData.getString("id")
            val name = responseData.getString("name")

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)

            context.startActivity(intent)
        }

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
}
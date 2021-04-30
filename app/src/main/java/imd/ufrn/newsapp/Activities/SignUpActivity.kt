package imd.ufrn.newsapp.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import imd.ufrn.newsapp.R
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class SignUpActivity : AppCompatActivity() {

    private lateinit var edTxtNome: TextView
    private lateinit var edTxtEmail: TextView
    private lateinit var edTxtSenha: TextView
    private lateinit var btnVoltar: Button
    private lateinit var btnLogin: ImageButton

    //TODO
    private val TAG = "signUpTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edTxtNome = findViewById(R.id.edTxtSignUpNome)
        edTxtEmail = findViewById(R.id.edTxtSignUpEmail)
        edTxtSenha = findViewById(R.id.edTxtSignUpSenha)
        btnVoltar = findViewById(R.id.btnSignUpVoltar)
        btnLogin = findViewById(R.id.btnSignUpProximo)

        btnVoltar.setOnClickListener {
            toInvitation()
        }

        btnLogin.setOnClickListener {
            register()
        }
    }

    fun toInvitation() {
        val intent = Intent(this, ConviteSignUpActivity::class.java)
        startActivity(intent)
    }

    fun register() {
        var task = HTTPAuthenticationTask(
            this,
            "http://10.0.2.2:3333/users",
            edTxtNome.text.toString(),
            edTxtEmail.text.toString(),
            edTxtSenha.text.toString()
        )
        task.execute()
    }

    class HTTPAuthenticationTask(
        private var context: Context,
        private var urlAddress: String,
        private var nome: String,
        private var email: String,
        private var password: String,
    ): AsyncTask<Int, Int, Unit>() {

        private lateinit var pd: ProgressDialog
        private var response =  String()
        private var error = String()

        override fun onPreExecute() {
            super.onPreExecute()

            pd = ProgressDialog(context)
            pd.setTitle("Cadastro")
            pd.setMessage("Aguarde...")
            pd.show()
        }

        override fun onPostExecute(result: Unit?) {
            super.onPostExecute(result)

            pd.dismiss()

            if (error.length >= 0) {
                when (error) {
                    "400" -> Toast.makeText(context, "Informações inválidas!", Toast.LENGTH_SHORT).show()
                    "409" -> Toast.makeText(context, "Email já em uso!", Toast.LENGTH_SHORT).show()
                    else -> {
                        Toast.makeText(context, "Há algo de errado!", Toast.LENGTH_SHORT).show()
                    }
                }
                return;
            }

            Log.i("tt", response)
            val responseData = JSONObject(response)

            val id = responseData.getString("id")
            val name = responseData.getString("name")

            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("name", name)

            context.startActivity(intent)
        }

        override fun doInBackground(vararg params: Int?) {
            var urlConnection: HttpURLConnection? = null

            try {
                val postData = JSONObject()
                postData.put("name", nome)
                postData.put("email", email)
                postData.put("password", password)
                postData.put("permission", "subscriber")

                val url = URL(urlAddress)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.setRequestProperty("Content-Type", "application/json")
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true
                urlConnection.doInput = true

                Log.i("tt", urlAddress)
                Log.i("tt", postData.toString())

                val out: OutputStream = BufferedOutputStream(urlConnection.outputStream)
                val writer = BufferedWriter(OutputStreamWriter(out, "UTF-8"))
                writer.write(postData.toString())
                writer.flush()

                val code = urlConnection.responseCode
                if (code != 201) {
                    error = "$code"
                    return;
                }

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
                Log.i("tt", response)
            }
            catch (e: Exception) {
                e.printStackTrace();
            } finally {
                urlConnection?.disconnect()
            }
        }
    }
}
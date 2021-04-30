package imd.ufrn.newsapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import imd.ufrn.newsapp.HTTPValidateConvite
import imd.ufrn.newsapp.R

class ConviteSignUpActivity : AppCompatActivity() {

    private lateinit var edTxtCodigo: TextView
    private lateinit var btnVoltar: Button
    private lateinit var btnProximo: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convite_sign_up)

        edTxtCodigo = findViewById(R.id.edTxtConvite)
        btnVoltar = findViewById(R.id.txtConviteVoltar)
        btnProximo = findViewById(R.id.btnConviteProximo)

        btnVoltar.setOnClickListener {
            toInitiation()
        }

        btnProximo.setOnClickListener {
            check()
        }
    }

    fun toInitiation() {
        val intent = Intent(this, InitiationActivity::class.java)
        startActivity(intent)
    }

    fun check() {
        val task = HTTPValidateConvite(
            this,
            "http://10.0.2.2:3333/invitation/",
            edTxtCodigo.text.toString()
        )
        task.execute()
    }

}
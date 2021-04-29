package imd.ufrn.newsapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import imd.ufrn.newsapp.R

class InitiationActivity : AppCompatActivity() {

    private lateinit var layLogin: LinearLayout
    private lateinit var laySignUp: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initiation)

        layLogin = findViewById(R.id.layLogin)
        laySignUp = findViewById(R.id.laySignUp)

        layLogin.setOnClickListener {
            toLogin()
        }

        laySignUp.setOnClickListener {
            toConviteSignUp()
        }
    }

    fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun toConviteSignUp() {
        val intent = Intent(this, ConviteSignUpActivity::class.java)
        startActivity(intent)
    }
}
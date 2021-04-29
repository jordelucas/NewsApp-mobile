package imd.ufrn.newsapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import imd.ufrn.newsapp.R

class InitiationActivity : AppCompatActivity() {

    private lateinit var optLogin: TextView
    private lateinit var optSignup: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initiation)

        // optLogin = findViewById(R.id...)
        // optSignup = findViewById(R.id...)
    }

    fun toLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun toSignup(view: View) {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
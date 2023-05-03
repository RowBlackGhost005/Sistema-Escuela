package mx.itson.edu.pruebaautenticacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bundle : Bundle? = intent.extras
        val email = bundle?.getString ("email")
        setup (email?:"")
    }

    private fun setup (email: String) {
        title = "Inicio"
        val logOutButton: Button =findViewById(R.id.logOutButton)
        val emailTextView: TextView =findViewById(R.id.emailTextView)
        emailTextView.text = email
        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }
}
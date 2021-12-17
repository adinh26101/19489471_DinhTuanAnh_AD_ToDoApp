package iuh.ad.a19489471_dinhtuananh.views

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import iuh.ad.a19489471_dinhtuananh.TaskActivity
import iuh.ad.a19489471_dinhtuananh.R
import iuh.ad.a19489471_dinhtuananh.extensions.Extensions.toast
import iuh.ad.a19489471_dinhtuananh.utils.FirebaseUtils.firebaseAuth
import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_account)

        val name: TextView = findViewById(R.id.name)
        val email: TextView = findViewById(R.id.email)

        name.text = firebaseAuth.currentUser?.displayName
        email.text = firebaseAuth.currentUser?.email


        btnSignOut.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, SignInActivity::class.java))
            toast("signed out")
            finish()
        }

        homeBtn.setOnClickListener{
            startActivity(Intent(this, TaskActivity::class.java))
            toast("home")
            finish()
        }
    }
}
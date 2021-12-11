package iuh.ad.a19489471_dinhtuananh

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import iuh.ad.a19489471_dinhtuananh.views.AccountActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.accountBtn -> {
            startActivity(Intent(this, AccountActivity::class.java))
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
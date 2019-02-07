package kratos.events.kratos_tth

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Checking if already logged in
        val sharedPref = this@SplashScreenActivity.getPreferences(Context.MODE_PRIVATE)
        val startedTime = sharedPref.getInt(getString(R.string.starting_time), -1)

        // Already logged in
        if(startedTime != -1) {
            startActivity(Intent(applicationContext, QuestionsActivity::class.java));
        } else {
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }
        finish()
    }
}

package uz.abdulla.currencyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.abdulla.currencyapp.storage.AppSettings

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppSettings.init(this)
    }
}
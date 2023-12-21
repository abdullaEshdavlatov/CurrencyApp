package uz.abdulla.currencyapp.storage

import android.content.Context
import android.content.SharedPreferences

object AppSettings {
    private const val NAME = "currencyApp"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context){
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit){
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var checkConvert: Boolean?
        get() =  preferences.getBoolean("checkConvert",true)
        set(value) = preferences.edit {
            if (value != null){
                it.putBoolean("checkConvert",value)
            }
        }
    var value: Float?
        get() =  preferences.getFloat("value",1.0f)
        set(value) = preferences.edit {
            if (value != null){
                it.putFloat("value",value)
            }
        }
}
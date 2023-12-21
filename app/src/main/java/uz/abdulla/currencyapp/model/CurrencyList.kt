package uz.abdulla.currencyapp.model

import android.content.Context
import androidx.lifecycle.MutableLiveData

object CurrencyList {
    private var listCurrency = MutableLiveData<List<Currency>>()
    private val listCountryFlags:MutableList<String> = ArrayList()
    fun init(list: MutableLiveData<List<Currency>>){
       listCurrency = list
    }

    fun getCurrencyList(): MutableLiveData<List<Currency>> {
        return listCurrency
    }

    fun setCountryFlags(list: MutableList<String>){
        listCountryFlags.clear()
        listCountryFlags.addAll(list)
    }

    fun getCountryFlags(): MutableList<String>{
        return listCountryFlags
    }

}
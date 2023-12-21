package uz.abdulla.currencyapp


import retrofit2.Call
import retrofit2.http.GET
import uz.abdulla.currencyapp.model.CountryFlag
import uz.abdulla.currencyapp.model.Currency

interface ApiService {
    @GET("uz/arkhiv-kursov-valyut/json/")
    fun getAllCurrency(): Call<List<Currency>>
}
package uz.abdulla.currencyapp.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.abdulla.currencyapp.ApiClient
import uz.abdulla.currencyapp.ApiService
import uz.abdulla.currencyapp.adapters.CurrencyAdapter
import uz.abdulla.currencyapp.OnItemClickListener
import uz.abdulla.currencyapp.R
import uz.abdulla.currencyapp.databinding.FragmentCurrencyListBinding
import uz.abdulla.currencyapp.model.CountryFlag
import uz.abdulla.currencyapp.model.Currency
import uz.abdulla.currencyapp.model.CurrencyList
import uz.abdulla.currencyapp.storage.AppSettings
import java.io.IOException


class CurrencyListFragment : Fragment(R.layout.fragment_currency_list){

    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!

    private var listOfCurrency: MutableList<Currency> = ArrayList()
    private var listAllFlags: List<CountryFlag>? = ArrayList()

    private val listCountryFlags:MutableList<String> = ArrayList()
    private val currencyBaseURL = "https://cbu.uz/"

    inline fun <reified T> Gson.parseJson(json: String) = fromJson<T>(json, object : TypeToken<T>() {}.type)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = MutableLiveData<List<Currency>>()
        val apiClient = ApiClient()
        val apiServiceForCurrency = apiClient.getRetrofit(currencyBaseURL).create(ApiService::class.java)
        apiServiceForCurrency.getAllCurrency().enqueue(object : Callback<List<Currency>>{
            override fun onResponse(
                call: Call<List<Currency>>,
                response: Response<List<Currency>>
            ) {
                if (response.isSuccessful){
                    list.postValue(response.body() as MutableList<Currency>)
                }
            }

            override fun onFailure(call: Call<List<Currency>>, t: Throwable) {
                Toast.makeText(requireContext(), "Malumot kelmadi", Toast.LENGTH_SHORT).show()
            }
        })

        CurrencyList.init(list)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCurrencyListBinding.bind(view)

        AppSettings.checkConvert = true
        AppSettings.value = 1.0f

        val gson = Gson()
        listAllFlags = getJsonDataFromAsset(requireContext(),"country.json")?.let {
            gson.parseJson<List<CountryFlag>>(
                it
            )
        }

        CurrencyList.getCurrencyList().observe(viewLifecycleOwner){
            listOfCurrency = it.toMutableList()
            for (i in 0 until listOfCurrency!!.size - 3){
                for (j in listAllFlags!!.indices){
                    if (listOfCurrency!![i].Ccy.substring(0,2) == listAllFlags!![j].code){
                        listCountryFlags.add(listAllFlags!![j].image)
                        break
                    }
                }
            }

            CurrencyList.setCountryFlags(listCountryFlags)

            val adapter = CurrencyAdapter(listOfCurrency?: emptyList(),listCountryFlags)
            binding.recyclerView.adapter = adapter
            adapter.setOnClickListener(object : OnItemClickListener{
                override fun onClick(position: Int) {
                    val directions = CurrencyListFragmentDirections.actionCurrencyListFragmentToConvertCurrencyFragment(position)
                    findNavController().navigate(directions)
                }

            })
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })






    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
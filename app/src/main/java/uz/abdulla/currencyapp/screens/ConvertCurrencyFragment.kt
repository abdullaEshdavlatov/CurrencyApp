package uz.abdulla.currencyapp.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment

import android.view.View
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest

import uz.abdulla.currencyapp.R
import uz.abdulla.currencyapp.databinding.FragmentConvertCurrencyBinding
import uz.abdulla.currencyapp.model.CurrencyList
import uz.abdulla.currencyapp.storage.AppSettings
import kotlin.math.roundToInt


class ConvertCurrencyFragment : Fragment(R.layout.fragment_convert_currency) {
    private var _binding: FragmentConvertCurrencyBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<ConvertCurrencyFragmentArgs>()
    private val listCountryFlags:MutableList<String> = ArrayList()

    private  var value: Float = 1.0f
    private  lateinit var valueForConvert:String
    private lateinit var currencyCode: String
    private  var checkConvert = true

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConvertCurrencyBinding.bind(view)

        checkConvert = AppSettings.checkConvert?:true
        value = AppSettings.value?:1.0f

        val position = args.position
        listCountryFlags.addAll(CurrencyList.getCountryFlags())
        CurrencyList.getCurrencyList().observe(viewLifecycleOwner){
            if (checkConvert){
                binding.amountCurrency1.text = value.toString()
                binding.amountCurrency2.text = "${(it[position].Rate.toDouble() * value.toDouble()).roundToInt()}"
                binding.currencyCode1.text = "${it[position].Ccy}"
                binding.currencyCode2.text = "UZS"
                binding.countryImage1.loadSvg(listCountryFlags[position])
                binding.countryImage2.setImageResource(R.drawable.uz)
            }else{
                binding.amountCurrency1.text = value.toDouble().toString()
                val amount: Double = (value.toDouble() / it[position].Rate.toDouble())
                binding.amountCurrency2.text = String.format("%.3f",amount).replace(",",".").toDouble().toString()
                binding.currencyCode1.text = "UZS"
                binding.currencyCode2.text = "${it[position].Ccy}"
                binding.countryImage1.setImageResource(R.drawable.uz)
                binding.countryImage2.loadSvg(listCountryFlags[position])
            }

        }

        binding.icBack.setOnClickListener {
            findNavController().navigate(R.id.action_convertCurrencyFragment_to_currencyListFragment)
        }

        binding.converter.setOnClickListener {
            checkConvert = !checkConvert
            AppSettings.checkConvert = checkConvert

            valueForConvert = binding.amountCurrency1.text.toString()
            binding.amountCurrency1.text = binding.amountCurrency2.text
            binding.amountCurrency2.text = valueForConvert

            currencyCode = binding.currencyCode1.text.toString()
            binding.currencyCode1.text = binding.currencyCode2.text
            binding.currencyCode2.text = currencyCode

            if (checkConvert){
                binding.countryImage1.loadSvg(listCountryFlags[position])
                binding.countryImage2.setImageResource(R.drawable.uz)
            }else{
                binding.countryImage1.setImageResource(R.drawable.uz)
                binding.countryImage2.loadSvg(listCountryFlags[position])
            }
        }

        binding.editAmount.setOnClickListener {
            val directions = ConvertCurrencyFragmentDirections.actionConvertCurrencyFragmentToEditAmountFragment(position,binding.amountCurrency1.text.toString())
            findNavController().navigate(directions)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun ImageView.loadSvg(url: String) {

        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .placeholder(R.drawable.placeholder)
            .target(this)
            .build()
        imageLoader.enqueue(request)
    }
}
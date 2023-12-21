package uz.abdulla.currencyapp.screens

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import uz.abdulla.currencyapp.R
import uz.abdulla.currencyapp.databinding.FragmentEditAmountBinding
import uz.abdulla.currencyapp.model.CurrencyList
import uz.abdulla.currencyapp.storage.AppSettings
import kotlin.math.roundToInt

class EditAmountFragment : Fragment(R.layout.fragment_edit_amount) {
    private var _binding: FragmentEditAmountBinding? = null
    private val binding get() = _binding!!

    private val numbers = Array(3){
        arrayOfNulls<TextView>(3)
    }
    private var textBuilder: StringBuilder = StringBuilder("")

    private val args by navArgs<EditAmountFragmentArgs>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentEditAmountBinding.bind(view)

        val position = args.position
        val flag = CurrencyList.getCountryFlags()[position]

        textBuilder.append(args.value)
        binding.editAmount.setText(textBuilder.toString())

        CurrencyList.getCurrencyList().observe(viewLifecycleOwner) {
            val list = it
            if (AppSettings.checkConvert!!) {
                binding.countryImage.loadSvg(flag)
                binding.currencyRate.text = "1 ${list[position].Ccy} = ${list[position].Rate.toDouble().roundToInt()}"
            } else {
                binding.countryImage.setImageResource(R.drawable.uz)
                binding.currencyRate.text = "${list[position].Rate.toDouble().roundToInt()} UZS = 1"
            }
            binding.switchButton.setOnClickListener {
                AppSettings.checkConvert = !AppSettings.checkConvert!!
                if (AppSettings.checkConvert!!) {
                    binding.currencyCcy.text = "${list[position].Ccy}"
                    binding.countryImage.loadSvg(flag)
                    binding.currencyRate.text = "1 ${list[position].Ccy} = ${list[position].Rate.toDouble().roundToInt()}"
                } else {
                    binding.currencyCcy.text = "UZS"
                    binding.countryImage.setImageResource(R.drawable.uz)
                    binding.currencyRate.text = "${list[position].Rate.toDouble().roundToInt()} UZS = 1"
                }
            }
        }

        loadNumbersAndHandleClick()
        binding.btn0.setOnClickListener {
            textBuilder = StringBuilder(binding.editAmount.text)
            textBuilder.append(binding.btn0.text)
            binding.editAmount.setText(textBuilder.toString())
        }
        binding.btnDot.setOnClickListener {
            if (textBuilder.indexOf(".") == -1){
                if (textBuilder.isNotEmpty()){
                    textBuilder.append(binding.btnDot.text)
                    binding.editAmount.setText(textBuilder.toString())
                }
            }
        }
        binding.btnC.setOnClickListener {
            textBuilder.clear()
            binding.editAmount.setText(textBuilder.toString())
        }
        binding.btnDelete.setOnClickListener {
            if (binding.editAmount.text.isNotEmpty()){
                if (textBuilder.isNotEmpty()){
                    textBuilder.deleteCharAt(textBuilder.length - 1)
                    binding.editAmount.setText(textBuilder.toString())
                }
            }
        }

        binding.btnConvert.setOnClickListener {
            if (binding.editAmount.text.isNotEmpty()){
                AppSettings.value = binding.editAmount.text.toString().toDouble().toFloat()
                findNavController().navigateUp()
            }
        }

    }

    private fun loadNumbersAndHandleClick(){
        for (i in 0 until binding.gridLayout.childCount){
            val x = i / 3
            val y = i % 3
            numbers[y][x] = binding.gridLayout.getChildAt(i) as TextView
            numbers[y][x]?.setOnClickListener {
                onClickNumbers(x,y)
            }
        }
    }

    private fun onClickNumbers(x: Int, y: Int) {
        textBuilder = StringBuilder(binding.editAmount.text)
        textBuilder.append(numbers[y][x]?.text ?: "")
        binding.editAmount.setText(textBuilder.toString())
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
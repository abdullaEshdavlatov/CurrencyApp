package uz.abdulla.currencyapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import uz.abdulla.currencyapp.OnItemClickListener
import uz.abdulla.currencyapp.R
import uz.abdulla.currencyapp.databinding.CurrencyListItemBinding
import uz.abdulla.currencyapp.model.Currency


class CurrencyAdapter(private val listCurrency: List<Currency>, val list: List<String>) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener
    inner class ViewHolder(private val binding: CurrencyListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currency: Currency, url: String) {
            binding.currencyCode.text = currency.Ccy
            binding.currencyName.text = currency.CcyNm_UZ
            binding.imageFlag.loadSvg(url)
            binding.root.setOnClickListener {
                listener.onClick(adapterPosition)
            }
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CurrencyListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = listCurrency.size - 3

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(listCurrency[position], list[position])
    }

    fun setOnClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}
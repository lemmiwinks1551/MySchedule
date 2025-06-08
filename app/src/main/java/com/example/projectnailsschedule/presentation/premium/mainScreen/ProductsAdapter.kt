package com.example.projectnailsschedule.presentation.premium.mainScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.projectnailsschedule.R
import ru.rustore.sdk.billingclient.model.product.Product
import com.example.projectnailsschedule.util.createDiffUtil
import coil.load
import com.example.projectnailsschedule.databinding.ItemProductViewHolderBinding
import com.example.projectnailsschedule.domain.models.rustoreBilling.ext.getStringRes

class ProductsAdapter(
    private val onProductClick: (Product) -> Unit,
) : ListAdapter<Product, ProductsAdapter.ViewHolder>(DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.setOnClickListener { onProductClick.invoke(item) }
        holder.bind(item)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemProductViewHolderBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) {
            with(binding) {
                productImage.isVisible = product.imageUrl != null
                product.imageUrl?.let { productImage.load(it) }

                titleView.text = product.title
                product.productType?.getStringRes()?.let { productTypeView.setText(it) }
                productStatusView.setText(product.productStatus.getStringRes())
                priceLabelView.text = product.priceLabel
            }
        }
    }

    companion object {
        private val DIFF_UTIL = createDiffUtil<Product> { oldItem, newItem ->
            oldItem.productId == newItem.productId
        }
    }
}

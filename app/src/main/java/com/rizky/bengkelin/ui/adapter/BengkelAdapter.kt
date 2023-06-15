package com.rizky.bengkelin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rizky.bengkelin.R
import com.rizky.bengkelin.data.remote.response.BengkelResult
import com.rizky.bengkelin.databinding.ItemBengkelBinding
import com.rizky.bengkelin.utils.formatToDistance

class BengkelAdapter(
    private val onClick: (bengkelId: Int) -> Unit
) : ListAdapter<BengkelResult, BengkelAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemBengkelBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bengkelResult: BengkelResult) {
            Glide.with(itemView.context)
                .load(bengkelResult.photoURL)
                .apply(RequestOptions.placeholderOf(R.drawable.img_image_loading))
                .into(binding.ivPhoto)
            binding.apply {
                tvName.text = bengkelResult.nama
                tvRating.text = bengkelResult.totalNilaiJumlahReview.toString()
                bengkelResult.kendaraan.split(";").let {
                    tvTypeOne.text = it[0]
                    if (it.size == 2) tvTypeTwo.text = it[1] else tvTypeTwo.visibility = View.GONE
                }
                tvAddress.text = bengkelResult.alamat
                tvDistance.text = bengkelResult.distance?.formatToDistance()
                tvQueue.text = itemView.context.getString(R.string.number_of_queue, bengkelResult.antrian)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemBengkelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bengkel = getItem(position)
        holder.apply {
            bind(bengkel)
            itemView.setOnClickListener { onClick(bengkel.id) }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<BengkelResult> =
            object : DiffUtil.ItemCallback<BengkelResult>() {
                override fun areItemsTheSame(
                    oldUser: BengkelResult,
                    newUser: BengkelResult
                ): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldUser: BengkelResult,
                    newUser: BengkelResult
                ): Boolean {
                    return oldUser == newUser
                }
            }
    }
}
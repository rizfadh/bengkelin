package com.rizky.bengkelin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rizky.bengkelin.data.remote.response.BengkelResult
import com.rizky.bengkelin.databinding.ItemBengkelBinding

class BengkelAdapter(
    onClick: (BengkelResult) -> Unit
) : ListAdapter<BengkelResult, BengkelAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemBengkelBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(bengkelResult: BengkelResult) {
            Glide.with(itemView.context)
                .load(bengkelResult.photoUrl)
                .into(binding.ivPhoto)
            binding.apply {
                tvName.text = bengkelResult.name
                tvType.text = bengkelResult.id
                tvAddress.text = bengkelResult.desc
                tvDistance.text = bengkelResult.distance.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemBengkelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bengkel = getItem(position)
        holder.bind(bengkel)
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
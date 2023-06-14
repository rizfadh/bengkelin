package com.rizky.bengkelin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rizky.bengkelin.data.remote.response.ReviewResult
import com.rizky.bengkelin.databinding.ItemReviewBinding

class ReviewAdapter : ListAdapter<ReviewResult, ReviewAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemReviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(review: ReviewResult) {
            binding.tvName.text = review.namaUser
            binding.tvCreatedAt.text = review.createdAt
            binding.tvRating.text = review.rating.toString()
            binding.tvReview.text = review.isiReview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let {
            holder.bind(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<ReviewResult> =
            object : DiffUtil.ItemCallback<ReviewResult>() {
                override fun areItemsTheSame(
                    oldUser: ReviewResult,
                    newUser: ReviewResult
                ): Boolean {
                    return oldUser.id == newUser.id
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldUser: ReviewResult,
                    newUser: ReviewResult
                ): Boolean {
                    return oldUser == newUser
                }
            }
    }
}
package com.rizky.bengkelin.ui.common

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.rizky.bengkelin.databinding.LayoutAlertBinding

fun alert(context: Context, @DrawableRes icon: Int, title: String, message: String? = null) {
    val binding = LayoutAlertBinding.inflate(LayoutInflater.from(context))
    AlertDialog.Builder(context).apply {
        setView(binding.root)
        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(context, icon))
        binding.tvTitle.text = title
        binding.tvMessage.text = message
        show().apply {
            binding.btnOk.setOnClickListener { dismiss() }
        }
    }
}
package com.braly.draftproject

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.braly.draftproject.databinding.ItemAppBinding


class AppAdapter(private val onItemClickListener: ((PInfo) -> Unit)? = null) : ListAdapter<PInfo, AppAdapter.ViewHolder>(
    diffCallback
) {
    class ViewHolder(private val context: Context, private val binding: ItemAppBinding, private val onItemClickListener: ((PInfo) -> Unit)? = null) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(app: PInfo) {
//            getIconApp(app.pname)
            binding.icon.setImageBitmap(app.icon)
            binding.title.text = app.appname
            binding.packageApp.text = app.pname
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(app)
            }
        }

        private fun getIconApp(pkg: String) {
            try {
                val icon: Drawable = context.packageManager.getApplicationIcon(pkg)
                binding.icon.setImageDrawable(icon)
            } catch (ne: PackageManager.NameNotFoundException) {
            }
        }

    }


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<PInfo>() {
            override fun areItemsTheSame(oldItem: PInfo, newItem: PInfo): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PInfo, newItem: PInfo): Boolean {
                return false
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppAdapter.ViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(parent.context, binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: AppAdapter.ViewHolder, position: Int) {
        holder.bindData(currentList[position])
    }
}
package com.hepipat.bookish.feature.mybooks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.databinding.ItemBookBinding

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    var itemClickListener: ((item: BooksUi) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<BooksUi>() {
        override fun areItemsTheSame(oldItem: BooksUi, newItem: BooksUi): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BooksUi, newItem: BooksUi): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(movie: List<BooksUi>?) {
        differ.submitList(movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BooksUi) {
            with(binding) {
                Glide.with(itemView)
                    .load(item.image)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivCover)

                itemView.setOnClickListener {
                    itemClickListener?.invoke(item)
                }
            }
        }
    }
}
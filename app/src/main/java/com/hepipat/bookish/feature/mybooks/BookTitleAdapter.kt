package com.hepipat.bookish.feature.mybooks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hepipat.bookish.core.domain.model.BooksUi
import com.hepipat.bookish.core.domain.model.TitleBooksUi
import com.hepipat.bookish.databinding.ItemTitleBinding

class BookTitleAdapter: RecyclerView.Adapter<BookTitleAdapter.TitleViewHolder>() {

    var itemClickListener: ((item: BooksUi) -> Unit)? = null

    private val diffCallback = object : DiffUtil.ItemCallback<TitleBooksUi>() {
        override fun areItemsTheSame(oldItem: TitleBooksUi, newItem: TitleBooksUi): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: TitleBooksUi, newItem: TitleBooksUi): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun submitList(myBooks: List<TitleBooksUi>?) {
        differ.submitList(myBooks)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitleViewHolder {
        val binding = ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TitleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    inner class TitleViewHolder(private val binding: ItemTitleBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TitleBooksUi) {
            with(binding) {
                tvTitle.text = item.title

                val posterAdapter = BookAdapter()
                posterAdapter.submitList(item.list)
                posterAdapter.itemClickListener = itemClickListener
                rvCover.adapter = posterAdapter
                rvCover.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

                if (item.list?.isEmpty() == true) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.tvEmpty.text = "Empty"
                }
            }
        }
    }
}
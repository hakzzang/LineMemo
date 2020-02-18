package hbs.com.linememo.ui.memo_make

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hbs.com.linememo.databinding.ItemMemoAddGalleryBinding
import hbs.com.linememo.databinding.ItemMemoGalleryBinding
import hbs.com.linememo.domain.model.WrapMemoGallery

enum class MemoGalleryViewType constructor(val type:Int) {
    ADD(0), PICTURE(1)
}

class MemoMakeGalleryAdapter (private val memoMakeViewModel: MemoMakeViewModel):
    ListAdapter<WrapMemoGallery, RecyclerView.ViewHolder>(memoGalleryListAsyncListUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MemoGalleryViewType.ADD.type) {
            val binding =
                ItemMemoAddGalleryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            MemoAddGalleryViewHolder(binding)
        } else {
            val binding =
                ItemMemoGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MemoGalleryViewHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MemoGalleryViewHolder) {
            holder.bind()
        } else if (holder is MemoAddGalleryViewHolder) {
            holder.bind(memoMakeViewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    inner class MemoGalleryViewHolder(val binding: ItemMemoGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

        }
    }

    inner class MemoAddGalleryViewHolder(val binding: ItemMemoAddGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(memoMakeViewModel:MemoMakeViewModel) {
            binding.memoMakeViewModel = memoMakeViewModel
        }
    }
}

val memoGalleryListAsyncListUtil = object : DiffUtil.ItemCallback<WrapMemoGallery>() {
    override fun areItemsTheSame(oldItem: WrapMemoGallery, newItem: WrapMemoGallery): Boolean {
        return oldItem.memoGallery?.id == newItem.memoGallery?.id
    }

    override fun areContentsTheSame(oldItem: WrapMemoGallery, newItem: WrapMemoGallery): Boolean {
        return oldItem.memoGallery == newItem.memoGallery
    }
}
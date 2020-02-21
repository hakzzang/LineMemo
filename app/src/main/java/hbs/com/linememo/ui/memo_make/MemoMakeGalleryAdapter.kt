package hbs.com.linememo.ui.memo_make

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hbs.com.linememo.databinding.ItemMemoAddGalleryBinding
import hbs.com.linememo.databinding.ItemMemoGalleryBinding
import hbs.com.linememo.domain.model.MemoGallery
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
            holder.bind(getItem(position), position)
        } else if (holder is MemoAddGalleryViewHolder) {
            holder.bind(memoMakeViewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.type
    }

    inner class MemoGalleryViewHolder(val binding: ItemMemoGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WrapMemoGallery, position: Int) {
            binding.ivThumbnailRemove.setOnClickListener {
                removeItem(position)
            }

            Glide.with(binding.ivThumbnailItem)
                .load(item.memoGallery?.filePath)
                .apply(RequestOptions.centerCropTransform().override(binding.ivThumbnailItem.width, binding.ivThumbnailItem.height))
                .into(binding.ivThumbnailItem)
        }
    }

    inner class MemoAddGalleryViewHolder(val binding: ItemMemoAddGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(memoMakeViewModel:MemoMakeViewModel) {
            binding.memoMakeViewModel = memoMakeViewModel
        }
    }

    fun initItems(){
        val newList = mutableListOf<WrapMemoGallery>()
        newList.add(WrapMemoGallery(MemoGalleryViewType.ADD))
        submitList(newList)
    }

    fun addItem(imageUri:String){
        val newList = mutableListOf<WrapMemoGallery>()
        newList.add(WrapMemoGallery(MemoGalleryViewType.ADD))
        val wrapMemoGallery = WrapMemoGallery(
            MemoGallery(0, imageUri, "CAMERA"),
            MemoGalleryViewType.PICTURE
        )
        newList.add(wrapMemoGallery)
        submitList(newList)
    }

    fun removeItem(position: Int){
        val oldList = currentList
        val newList = mutableListOf<WrapMemoGallery>()
        newList.addAll(oldList)
        newList.removeAt(position)
        submitList(newList)
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
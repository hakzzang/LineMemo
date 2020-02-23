package hbs.com.linememo.ui.memo_make

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ItemMemoAddGalleryBinding
import hbs.com.linememo.databinding.ItemMemoGalleryBinding
import hbs.com.linememo.domain.model.MemoGallery
import hbs.com.linememo.domain.model.WrapMemoGallery

enum class MemoGalleryViewType constructor(val type:Int) {
    ADD(0), PICTURE(1)
}

class MemoMakeGalleryAdapter (private val memoMakeViewModel: MemoMakeViewModel):
    ListAdapter<WrapMemoGallery, RecyclerView.ViewHolder>(memoGalleryListAsyncListUtil) {
    init {
        setHasStableIds(true)
    }
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

    override fun getItemId(position: Int): Long {
        return getItem(position).memoGallery?.id?.toLong()?:hashCode().toLong()
    }

    inner class MemoGalleryViewHolder(val binding: ItemMemoGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WrapMemoGallery, position: Int) {
            binding.ivThumbnailRemove.setOnClickListener {
                removeItem(item.memoGallery?.id)
            }

            Glide.with(binding.ivThumbnailItem)
                .load(item.memoGallery?.filePath)
                .apply(RequestOptions.centerCropTransform().override(binding.ivThumbnailItem.width, binding.ivThumbnailItem.height))
                .error(R.drawable.ic_highlight_off_red_24dp)
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
        submitList(newList)
    }
    fun initItems(memoGalleries:List<MemoGallery>){
        val newList = mutableListOf<WrapMemoGallery>()
        for(memoGallery in memoGalleries){
            newList.add(WrapMemoGallery(memoGallery, MemoGalleryViewType.PICTURE))
        }
        submitList(newList)
    }

    fun addItem(imageUri:String){
        val newList = mutableListOf<WrapMemoGallery>()
        newList.addAll(currentList)
        val wrapMemoGallery = WrapMemoGallery(
            MemoGallery(0, imageUri, 0),
            MemoGalleryViewType.PICTURE
        )
        newList.add(wrapMemoGallery)
        submitList(newList)
    }

    fun removeItem(galleryId: Int?){
        val galleryId = galleryId?:return
        val oldList = currentList
        val newList = mutableListOf<WrapMemoGallery>()
        newList.addAll(oldList)
        newList.forEachIndexed { index, wrapMemoGallery ->
            if(wrapMemoGallery.memoGallery?.id == galleryId){
                newList.remove(wrapMemoGallery)
                submitList(newList)
                return
            }
        }
    }
}

val memoGalleryListAsyncListUtil = object : DiffUtil.ItemCallback<WrapMemoGallery>() {
    override fun areItemsTheSame(oldItem: WrapMemoGallery, newItem: WrapMemoGallery): Boolean {
        return oldItem.memoGallery == newItem.memoGallery
    }

    override fun areContentsTheSame(oldItem: WrapMemoGallery, newItem: WrapMemoGallery): Boolean {
        return oldItem.memoGallery?.id == newItem.memoGallery?.id
    }
}
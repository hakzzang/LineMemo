package hbs.com.linememo.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hbs.com.linememo.R
import hbs.com.linememo.databinding.ItemGalleryBinding
import hbs.com.linememo.domain.model.WrapMemoGallery
import hbs.com.linememo.ui.memo_make.memoGalleryListAsyncListUtil

class GalleryAdapter : ListAdapter<WrapMemoGallery, RecyclerView.ViewHolder>(memoGalleryListAsyncListUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MemoGalleryViewHolder(ItemGalleryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MemoGalleryViewHolder) {
            holder.bind(getItem(position), position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.type
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).memoGallery?.id?.toLong() ?: hashCode().toLong()
    }

    inner class MemoGalleryViewHolder(val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WrapMemoGallery, position: Int) {
            Glide.with(binding.ivGallery)
                .load(item.memoGallery?.filePath)
                .error(R.drawable.ic_highlight_off_red_24dp)
                .into(binding.ivGallery)
        }
    }
}
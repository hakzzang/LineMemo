package hbs.com.linememo.ui.memo

import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hbs.com.linememo.databinding.ItemMemoBinding
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.ui.memo_read.MemoReadActivity
import hbs.com.linememo.util.ResourceKeys

class MemoListAdapter(private val memoViewModel: MemoViewModel) : ListAdapter<MemoItem, RecyclerView.ViewHolder>(memoListAsyncListUtil){
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MemoItemViewHolder(
            ItemMemoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MemoItemViewHolder){
            holder.bind(getItem(position))
        }
    }

    inner class MemoItemViewHolder(val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(memoItem: MemoItem){
            binding.memoItem = memoItem
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, MemoReadActivity::class.java)
                intent.putExtra(ResourceKeys.MEMO_ITEM_KEY,memoItem)
                memoViewModel.navigator.callingIntent(intent)
            }
        }
    }
}

val memoListAsyncListUtil = object : DiffUtil.ItemCallback<MemoItem>() {
    override fun areItemsTheSame(oldItem: MemoItem, newItem: MemoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MemoItem, newItem: MemoItem): Boolean {
        return oldItem == newItem
    }
}
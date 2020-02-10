package hbs.com.linememo.ui.memo

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hbs.com.linememo.domain.model.MemoItem

class MemoListAdapter : ListAdapter<MemoItem, RecyclerView.ViewHolder>(memoListAsyncListUtil){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("VIEW HOLDER 만들기")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("VIEW HOLDER BIND 만들기")
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
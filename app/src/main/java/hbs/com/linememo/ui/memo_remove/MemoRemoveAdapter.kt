package hbs.com.linememo.ui.memo_remove

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hbs.com.linememo.databinding.ItemRemoveMemoBinding
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.ui.memo.memoListAsyncListUtil


class MemoRemoveAdapter(private val memoRemoveViewModel: MemoRemoveViewModel) : ListAdapter<MemoItem, RecyclerView.ViewHolder>(memoListAsyncListUtil){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MemoItemViewHolder(
            ItemRemoveMemoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is MemoItemViewHolder){
            holder.bind(getItem(position), position)
        }
    }

    inner class MemoItemViewHolder(val binding: ItemRemoveMemoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(memoItem: MemoItem, position: Int){
            binding.memoItem = memoItem
            binding.cbRemoveMemoItem.isChecked = memoRemoveViewModel.containsRemovePositionOf(position)
            binding.root.setOnClickListener {
                binding.cbRemoveMemoItem.isChecked = !binding.cbRemoveMemoItem.isChecked
                if(binding.cbRemoveMemoItem.isChecked){
                    memoRemoveViewModel.addRemovePosition(position)
                }else {
                    memoRemoveViewModel.removeRemovePosition(position)
                }
            }
        }
    }

    fun addItems(list: List<MemoItem>, runnable: Runnable) {
        val sortedList = list.sortedByDescending { it.makeAt.time }
        submitList(sortedList, runnable)
    }
}
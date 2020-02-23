package hbs.com.linememo.ui.memo_remove

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hbs.com.linememo.databinding.ItemRemoveMemoBinding
import hbs.com.linememo.domain.model.MemoItem
import hbs.com.linememo.ui.memo.memoListAsyncListUtil


class MemoRemoveAdapter(private val memoRemoveViewModel: MemoRemoveViewModel) : ListAdapter<MemoItem, RecyclerView.ViewHolder>(memoListAsyncListUtil){
    init {
        setHasStableIds(true)
    }
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
            holder.bind(getItem(position))
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.toLong()
    }

    inner class MemoItemViewHolder(val binding: ItemRemoveMemoBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(memoItem: MemoItem){
            binding.memoItem = memoItem
            binding.cbRemoveMemoItem.isChecked = memoRemoveViewModel.containsRemovePositionOf(memoItem.id)
            binding.root.setOnClickListener {
                binding.cbRemoveMemoItem.isChecked = !binding.cbRemoveMemoItem.isChecked
                if(binding.cbRemoveMemoItem.isChecked){
                    memoRemoveViewModel.addRemoveMemoId(memoItem.id)
                }else {
                    memoRemoveViewModel.removeRemoveMemoId(memoItem.id)
                }
            }
        }
    }

    fun notifyCurrentItems(){
        notifyDataSetChanged()
    }

    fun addAllItems(memoItems:List<MemoItem>){
        val sortedList = memoItems.sortedByDescending { it.makeAt.time }
        submitList(sortedList)
    }
}
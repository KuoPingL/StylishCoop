package app.appworks.school.stylish.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.UserRecord
import app.appworks.school.stylish.databinding.ItemDetailRecordBinding

class DetailUserRecordAdapter: ListAdapter<UserRecord, DetailUserRecordAdapter.RecordViewHolder>(RecordCallback) {

    companion object RecordCallback: DiffUtil.ItemCallback<UserRecord>() {
        override fun areContentsTheSame(oldItem: UserRecord, newItem: UserRecord): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: UserRecord, newItem: UserRecord): Boolean {
            return oldItem === newItem
        }
    }

    class RecordViewHolder(val binding: ItemDetailRecordBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(record: UserRecord) {
            binding.product = record
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(ItemDetailRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}
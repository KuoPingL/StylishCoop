package app.appworks.school.stylish.detail.chatbot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.ChatbotButton
import app.appworks.school.stylish.databinding.ItemChatbotButtonBinding


class ChatbotButtonAdapter(val viewModel: ChatbotViewModel):
    ListAdapter<ChatbotButton, ChatbotButtonAdapter.ButtonViewHolder>(ChatbotButtonCallback) {

    companion object ChatbotButtonCallback: DiffUtil.ItemCallback<ChatbotButton>() {
        override fun areContentsTheSame(oldItem: ChatbotButton, newItem: ChatbotButton): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: ChatbotButton, newItem: ChatbotButton): Boolean {
            return oldItem === newItem
        }
    }

    class ButtonViewHolder(val binding: ItemChatbotButtonBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatbotButton: ChatbotButton, viewModel: ChatbotViewModel) {
            binding.chatbotButton = chatbotButton
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        return ButtonViewHolder(ItemChatbotButtonBinding
            .inflate(LayoutInflater
                .from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }
}
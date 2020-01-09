package app.appworks.school.stylish.detail.chatbot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.data.ChatbotDialog
import app.appworks.school.stylish.data.UserDialog
import app.appworks.school.stylish.databinding.ItemChatbotButtonBinding
import app.appworks.school.stylish.databinding.ItemChatbotRespondBinding
import app.appworks.school.stylish.databinding.ItemChatbotUserBinding

private const val CHATBOT_DIALOG = 0
private const val USER_DIALOG = 1

class ChatbotAdapter(val chatbotViewModel: ChatbotViewModel): ListAdapter<ChatbotItem, RecyclerView.ViewHolder>(ChatbotDialogCallback) {

    companion object ChatbotDialogCallback: DiffUtil.ItemCallback<ChatbotItem>() {
        override fun areContentsTheSame(oldItem: ChatbotItem, newItem: ChatbotItem): Boolean {
            if (oldItem is ChatbotItem.ChatbotDialogItem && newItem is ChatbotItem.ChatbotDialogItem) {
                return oldItem.chatbotDialogItem == newItem.chatbotDialogItem
            } else if (oldItem is ChatbotItem.UserDialogItem && newItem is ChatbotItem.UserDialogItem) {
                return oldItem.userDialog == newItem.userDialog
            }
            return false
        }

        override fun areItemsTheSame(oldItem: ChatbotItem, newItem: ChatbotItem): Boolean {
            if (oldItem is ChatbotItem.ChatbotDialogItem && newItem is ChatbotItem.ChatbotDialogItem) {
                return oldItem.chatbotDialogItem == newItem.chatbotDialogItem
            } else if (oldItem is ChatbotItem.UserDialogItem && newItem is ChatbotItem.UserDialogItem) {
                return oldItem.userDialog == newItem.userDialog
            }
            return false
        }
    }


    class ChatbotDialogViewHolder(val binding: ItemChatbotRespondBinding, viewModel: ChatbotViewModel):
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.recyclerChatbotResponseButtons.adapter = ChatbotButtonAdapter(viewModel)
        }

        fun bind(chatbotDialog: ChatbotDialog) {
            binding.chatbotDialog = chatbotDialog
            binding.executePendingBindings()
        }
    }

    class UserDialogViewHolder(val binding: ItemChatbotUserBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(userDialog: UserDialog) {
            binding.userDialog = userDialog
            binding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is ChatbotItem.ChatbotDialogItem -> CHATBOT_DIALOG
            else -> USER_DIALOG
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {

            CHATBOT_DIALOG -> {
                ChatbotDialogViewHolder(ItemChatbotRespondBinding
                    .inflate(LayoutInflater
                        .from(parent.context), parent, false), chatbotViewModel)
            }

            else -> {
                UserDialogViewHolder(ItemChatbotUserBinding
                    .inflate(LayoutInflater
                        .from(parent.context), parent, false))
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when(holder) {
            is ChatbotDialogViewHolder -> {
                holder.bind((item as ChatbotItem.ChatbotDialogItem).chatbotDialogItem)
            }

            is UserDialogViewHolder -> {
                holder.bind((item as ChatbotItem.UserDialogItem).userDialog)
            }
        }
    }

}
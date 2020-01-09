package app.appworks.school.stylish.detail.chatbot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import app.appworks.school.stylish.data.Product
import app.appworks.school.stylish.databinding.FragmentChatbotBinding
import app.appworks.school.stylish.detail.DetailViewModel
import app.appworks.school.stylish.dialog.HeightWeightDialog
import app.appworks.school.stylish.ext.getVmFactory

class ChatbotMainFragment : Fragment() {

    var product: Product? = null
    var detailViewModel: DetailViewModel? = null
    private var viewModel: ChatbotViewModel? = null
    private lateinit var chatbotAdapter: ChatbotAdapter
    private var heightWeightDialog: HeightWeightDialog? = null

    lateinit var binding: FragmentChatbotBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatbotBinding.inflate(inflater)
        binding.lifecycleOwner = this

        product?.let {
            viewModel = getVmFactory(it).create(ChatbotViewModel::class.java)
            viewModel?.let {chatbotViewModel ->
                binding.viewModel = chatbotViewModel
                binding.detailViewModel = detailViewModel
                chatbotAdapter = ChatbotAdapter(chatbotViewModel)
                binding.recyclerChatbotChats.adapter = chatbotAdapter
            }
        }

        detailViewModel?.chatbotStatus?.observe(this, Observer {
            when (it) {
                DetailViewModel.ChatbotStatus.SHOWING -> {
                    if (viewModel?.chatItems?.value == null || viewModel?.chatItems?.value?.size == 0) {
                        viewModel?.fetchDialogFor("")
                    } else {
                        // Scroll to bottom
                    }
                }
            }
        })


        viewModel?.askForHeightAndWeight?.observe(this, Observer {
            it?.let {shouldAskForHeightAndWeight ->
                if (shouldAskForHeightAndWeight) {

                    // Display dialog
                    heightWeightDialog = HeightWeightDialog{height, weight ->
                        viewModel?.sentHeightAndWeight(height, weight)
                        heightWeightDialog?.dismiss()
                    }
                    heightWeightDialog?.show(fragmentManager!!, "tag")
                }
            }
        })




        return binding.root
    }


}
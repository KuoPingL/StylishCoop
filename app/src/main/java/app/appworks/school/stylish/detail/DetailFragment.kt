package app.appworks.school.stylish.detail

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.stylish.NavigationDirections
import app.appworks.school.stylish.databinding.FragmentDetailBinding
import app.appworks.school.stylish.detail.chatbot.ChatbotMainFragment
import app.appworks.school.stylish.ext.chatbotCollapse
import app.appworks.school.stylish.ext.chatbotExpand
import app.appworks.school.stylish.ext.getVmFactory
import app.appworks.school.stylish.ext.toPixel
import app.appworks.school.stylish.login.UserManager

/**
 * Created by Wayne Chen in Jul. 2019.
 */
class DetailFragment : Fragment() {

    /**
     * Lazily initialize our [DetailViewModel].
     */
    private val viewModel by viewModels<DetailViewModel> { getVmFactory(DetailFragmentArgs.fromBundle(arguments!!).productKey) }

//    private var previousCurrentFragmentType: CurrentFragmentType? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        init()
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.recyclerDetailGallery.adapter = DetailGalleryAdapter()
        binding.recyclerDetailCircles.adapter = DetailCircleAdapter()
        binding.recyclerDetailColor.adapter = DetailColorAdapter()

        binding.recyclerDetailRecords.adapter = DetailUserRecordAdapter()

        binding.recyclerDetailRecords.addItemDecoration(object: RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val index = parent.indexOfChild(view)
                val px = 10.toPixel().toInt()
                when (index) {
                    0 -> {
                        outRect.left = 0
                    }
                    else -> {
                        outRect.set(px, 0, 0, 0)
                    }
                }
            }
        })

        val linearSnapHelper = LinearSnapHelper().apply {
            attachToRecyclerView(binding.recyclerDetailGallery)
        }

        binding.recyclerDetailGallery.setOnScrollChangeListener { _, _, _, _, _ ->
                viewModel.onGalleryScrollChange(
                    binding.recyclerDetailGallery.layoutManager,
                    linearSnapHelper
                )
        }




        // set the initial position to the center of infinite gallery
        viewModel.product.value?.let { product ->
            binding.recyclerDetailGallery
                .scrollToPosition(product.images.size * 100)

            viewModel.snapPosition.observe(this, Observer {
                (binding.recyclerDetailCircles.adapter as DetailCircleAdapter).selectedPosition.value = (it % product.images.size)
            })
        }

        viewModel.navigateToAdd2cart.observe(this, Observer {
            it?.let {
                findNavController().navigate(NavigationDirections.navigateToAdd2cartDialog(it))
                viewModel.onAdd2cartNavigated()
            }
        })

        viewModel.leaveDetail.observe(this, Observer {
            it?.let {
                if (it) findNavController().popBackStack()
            }
        })

        /**
         * USERRECORD
         */

        viewModel.saveRecord()

        if (UserManager.isLoggedIn) {
            viewModel.record.observe(this, Observer {
                (binding.recyclerDetailRecords.adapter as? DetailUserRecordAdapter)?.apply {
                    submitList(it)
                    notifyDataSetChanged()
                }
            })
        } else {
            viewModel.remoteRecord.observe(this, Observer {
                (binding.recyclerDetailRecords.adapter as? DetailUserRecordAdapter)?.apply {
                    submitList(it)
                    notifyDataSetChanged()
                }
            })
        }



        /**
         * CHATBOT
         */
        viewModel.chatbotStatus.observe(this, Observer {
            it?.let { chatbotStatus ->
                when (chatbotStatus){
                    DetailViewModel.ChatbotStatus.SHOWING -> {
                        binding.layoutDetailChatbot.chatbotExpand()
                        Handler().postDelayed({
                            viewModel.resetChatbotStatus()
                        }, 600)
                    }

                    DetailViewModel.ChatbotStatus.HIDING -> {
                        binding.layoutDetailChatbot.chatbotCollapse()
                        viewModel.resetChatbotStatus()
                    }

                    else -> {}
                }
            }
        })


        return binding.root
    }

    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)

        when(childFragment) {
            is ChatbotMainFragment -> {
                childFragment.detailViewModel = viewModel
                childFragment.product = DetailFragmentArgs.fromBundle(arguments!!).productKey
            }
        }
    }


//    override fun onDestroy() {
//        super.onDestroy()
//        previousCurrentFragmentType?.let {
//            ViewModelProviders.of(activity!!).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = it
//            }
//        }
//    }

//    private fun init() {
//        activity?.let {
//            ViewModelProviders.of(it).get(MainViewModel::class.java).apply {
//                currentFragmentType.value = CurrentFragmentType.DETAIL
//            }
//        }
//    }
}

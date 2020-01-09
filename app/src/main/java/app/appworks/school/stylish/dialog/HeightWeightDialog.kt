package app.appworks.school.stylish.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import app.appworks.school.stylish.R
import app.appworks.school.stylish.databinding.DialogAskForHeightWeightBinding

class HeightWeightDialog (val callback:(height:Double, weight: Double)->Unit) : AppCompatDialogFragment() {

    private lateinit var binding: DialogAskForHeightWeightBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.MessageDialog)
        isCancelable = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAskForHeightWeightBinding.inflate(inflater)

        binding.buttonHeightandweightButton.setOnClickListener {
            val height = binding.editHeightandweightHeight.text.toString().toDoubleOrNull()
            val weight = binding.editHeightandweightWeight.text.toString().toDoubleOrNull()

            binding.textHeightandweightHeightError.visibility = View.GONE
            binding.textHeightandweightWeightError.visibility = View.GONE

            if (height == null || height <= 50.0) {
                binding.textHeightandweightHeightError.text = "請輸入正確身高"
                binding.textHeightandweightHeightError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            if (weight == null || weight <= 2.0) {
                binding.textHeightandweightWeightError.text = "請輸入正確體重"
                binding.textHeightandweightWeightError.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // pass height and weight back and dismiss

            callback(height, weight)
        }

        return binding.root
    }
}
package com.nitishsharma.frndcal.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nitishsharma.frndcal.databinding.FragmentHomeAddTaskBottomSheetBinding

class HomeAddTaskBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentHomeAddTaskBottomSheetBinding
    private val viewmodel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        FragmentHomeAddTaskBottomSheetBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClickListeners()
    }

    private fun initClickListeners() {
        with(binding) {
            submitBtn.setOnClickListener {
                viewmodel.storeTaskRemote(
                    binding.enterTitleText.text.toString(),
                    binding.enterDescriptionText.text.toString()
                )
                dismiss()
            }
        }
    }

}
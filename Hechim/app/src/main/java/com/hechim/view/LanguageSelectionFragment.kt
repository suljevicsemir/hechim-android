package com.hechim.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hechim.R
import com.hechim.databinding.FragmentLanguageSelectionBinding


class LanguageSelectionFragment : Fragment() {

    private lateinit var binding: FragmentLanguageSelectionBinding
    private var savedViewInstance: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(savedViewInstance != null) {
            return savedViewInstance as View
        }
        binding = FragmentLanguageSelectionBinding.inflate(inflater, container, false)
        savedViewInstance = binding.root

        return binding.root
    }


}
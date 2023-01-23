package com.hechim

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hechim.databinding.FragmentCodeBinding
import com.hechim.view.LoginFragment


class CodeFragment : Fragment() {

    private lateinit var binding: FragmentCodeBinding
    private val args: CodeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeBinding.inflate(inflater, container, false)

        initBindings()
        //binding.registerCodeField.editText.setText(args.code)
        binding.registerCodeField.editText.setText(args.code.toString())





        return binding.root
    }

    private fun initBindings() {
        binding.codeToolbar.title = getString(R.string.register_code_title)
        binding.registerCodeSubtitle.text = getString(R.string.register_code_subtitle)
        binding.registerCodeField.hint = getString(R.string.register_code_hint)
        binding.codeToolbar.onBackPressed = {
            findNavController().popBackStack()
        }
    }
}
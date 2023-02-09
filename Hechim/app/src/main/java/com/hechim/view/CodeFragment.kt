package com.hechim.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hechim.R
import com.hechim.databinding.FragmentCodeBinding
import com.hechim.models.data.Resource
import com.hechim.utils.Extensions.validateInput
import com.hechim.view_models.AuthenticationViewModel
import kotlinx.coroutines.flow.collectLatest


class CodeFragment : Fragment() {

    private lateinit var binding: FragmentCodeBinding
    private val args: CodeFragmentArgs by navArgs()
    private val authenticationViewModel : AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeBinding.inflate(inflater, container, false)

        initBindings()

        lifecycleScope.launchWhenCreated {
            authenticationViewModel.confirmEmailResource.collectLatest {
                when(it) {
                    is Resource.Success -> {
                        binding.codeSpinner.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.codeSpinner.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        binding.codeSpinner.visibility = View.INVISIBLE
                    }
                    is Resource.Nothing -> {
                        binding.codeSpinner.visibility = View.INVISIBLE
                    }
                }
            }
        }


        return binding.root
    }

    private fun initBindings() {
        binding.codeToolbar.title = getString(R.string.register_code_title)
        binding.registerCodeSubtitle.text = getString(R.string.register_code_subtitle)
        binding.registerCodeField.hint = getString(R.string.register_code_hint)
        binding.codeToolbar.onBackPressed = {
            findNavController().popBackStack()
        }
        binding.registerCodeButton.title = getString(R.string.register_code_button)

        if(args.code != 0) {
            binding.registerCodeField.editText.setText(args.code.toString())
        }

        binding.registerCodeButton.button.setOnClickListener {

            //validate input field, must be a 6 digit number
            val emailValid = binding.registerCodeField.textInputLayout.validateInput(
                isValid = {
                    it.length == 6 && it.toIntOrNull() != null
                },
                error = getString(R.string.register_code_validation_message)
            )

            if(!emailValid) {
                return@setOnClickListener
            }

            //case where flow is not from the dynamic link
            if(!args.dynamicLink && args.email != null) {
                authenticationViewModel.confirmEmail(
                    code = binding.registerCodeField.editText.text.toString().toInt(),
                    email = args.email!!,
                    navDirections = CodeFragmentDirections.actionCodeFragmentToTempHomeFragment()
                )
            }

        }
    }
}
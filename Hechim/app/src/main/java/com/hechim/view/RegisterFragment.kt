package com.hechim.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.hechim.BuildConfig
import com.hechim.R
import com.hechim.databinding.FragmentRegisterBinding
import com.hechim.models.data.Resource
import com.hechim.utils.Extensions.animatedNavigate
import com.hechim.utils.Extensions.errorResetListener
import com.hechim.utils.Extensions.validateInput
import com.hechim.utils.PasswordValidator
import com.hechim.view_models.AuthenticationViewModel
import kotlinx.coroutines.flow.collectLatest


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private val passwordValidator = PasswordValidator()

    private val authenticationViewModel: AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        setHints()
        attachInputListeners()
        buttonListener()

        lifecycleScope.launchWhenCreated {
            authenticationViewModel.registerResource.collectLatest {
                when(it) {
                    is Resource.Success -> {
                        binding.registerSpinner.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.registerSpinner.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        binding.registerSpinner.visibility = View.INVISIBLE
                    }
                    is Resource.Nothing -> {
                        binding.registerSpinner.visibility = View.INVISIBLE
                    }
                }
            }
        }

        return binding.root
    }

    //builds content for register page
    @Suppress("KotlinConstantConditions")
    private fun setHints() {

        //hints for text fields
        binding.registerEmailField.hint = getString(R.string.register_email_hint)
        binding.registerPasswordField.hint = getString(R.string.register_password_hint)
        binding.registerConfirmPasswordField.hint = getString(R.string.register_confirm_pass_hint)
        //submit button
        binding.registerContinueButton.title = getString(R.string.register_button)

        //icons at the end of the field
        binding.registerPasswordField.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.registerConfirmPasswordField.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE

        //display different image depending on the build flavor
        if(BuildConfig.FLAVOR == "development") {
            binding.appLogo.setBackgroundResource(R.drawable.register_icon)
        }
        else {
            binding.appLogo.setBackgroundResource(R.drawable.app_logo_prod)
        }

    }

    //validating input fields, sequentially
    //if field is invalid, we don't check the ones after it
    private fun validateInputs(): Boolean {
        val emailValid = binding.registerEmailField.textInputLayout.validateInput(
            isValid = {
                passwordValidator.isValidEmail(it)
            },
            error = getString(R.string.register_email_invalid)
        )
        if(!emailValid) {
            return false
        }
        val passwordValid = binding.registerPasswordField.textInputLayout.validateInput(
            isValid = {
                passwordValidator.isPasswordValid(it)
            },
            error = getString(R.string.register_password_invalid)
        )
        if(!passwordValid) {
            return false
        }
        val confirmedPasswordValid = binding.registerConfirmPasswordField.textInputLayout.validateInput(
            isValid = {
                passwordValidator.isConfirmedPasswordValid(binding.registerPasswordField.editText.text.toString(), it)
            },
            error = getString(R.string.register_passwords_identical)
        )
        if(!confirmedPasswordValid) {
            return false
        }
        return true
    }

    //validates input and begins registration flow
    private fun buttonListener() {
        binding.registerContinueButton.button.setOnClickListener {
            if(!validateInputs()) {
                return@setOnClickListener
            }
            authenticationViewModel.register(
                email = binding.registerEmailField.editText.toString(),
                password = binding.registerPasswordField.editText.toString(),
                confirmPassword = binding.registerConfirmPasswordField.editText.toString()
            )
        }

        binding.signInLabel.setOnClickListener {
            findNavController().animatedNavigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
    }

    //error reset listeners, remove error labels when user starts typing again
    private fun attachInputListeners() {
        binding.registerEmailField.textInputLayout.errorResetListener()
        binding.registerPasswordField.textInputLayout.errorResetListener()
        binding.registerConfirmPasswordField.textInputLayout.errorResetListener()
    }



}
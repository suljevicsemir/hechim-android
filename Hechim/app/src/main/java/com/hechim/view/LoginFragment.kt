package com.hechim.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.hechim.BuildConfig
import com.hechim.R
import com.hechim.databinding.FragmentLoginBinding
import com.hechim.utils.Extensions.errorResetListener
import com.hechim.utils.Extensions.validateInput
import com.hechim.utils.PasswordValidator


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val passwordValidator = PasswordValidator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        buildContent()
        buttonListener()
        attachInputListeners()

        return binding.root
    }

    //validating input fields, sequentially
    //if field is invalid, we don't check the ones after it
    private fun validateInputs(): Boolean {
        val emailValid = binding.loginEmailField.textInputLayout.validateInput(
            isValid = {
                passwordValidator.isValidEmail(it)
            },
            error = getString(R.string.register_email_invalid)
        )
        if(!emailValid) {
            return false
        }
        val passwordValid = binding.loginPasswordField.textInputLayout.validateInput(
            isValid = {
                passwordValidator.isPasswordValid(it)
            },
            error = getString(R.string.register_password_invalid)
        )
        return passwordValid
    }

    @Suppress("KotlinConstantConditions")
    private fun buildContent() {
        //display different image depending on the build flavor
        if(BuildConfig.FLAVOR == "development") {
            binding.appLogo.setBackgroundResource(R.drawable.register_icon)
        }
        else {
            binding.appLogo.setBackgroundResource(R.drawable.app_logo_prod)
        }

        //text fields hints
        binding.loginEmailField.hint = getString(R.string.register_email_hint)
        binding.loginPasswordField.hint = getString(R.string.register_password_hint)

        //button title
        binding.loginContinueButton.title = getString(R.string.register_button)

        //password end icon
        binding.loginPasswordField.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
    }

    //validates input and begins registration flow
    private fun buttonListener() {
        binding.loginContinueButton.button.setOnClickListener {
            if(!validateInputs()) {
                return@setOnClickListener
            }
        }

        binding.signUpLabel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    //error reset listeners, remove error labels when user starts typing again
    private fun attachInputListeners() {
        binding.loginEmailField.textInputLayout.errorResetListener()
        binding.loginPasswordField.textInputLayout.errorResetListener()
    }

}
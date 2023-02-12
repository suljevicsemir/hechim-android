package com.hechim.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.hechim.BuildConfig
import com.hechim.R
import com.hechim.databinding.FragmentLoginBinding
import com.hechim.models.data.Resource
import com.hechim.utils.Extensions.animatedNavigate
import com.hechim.utils.Extensions.errorResetListener
import com.hechim.utils.Extensions.validateInput
import com.hechim.utils.PasswordValidator
import com.hechim.view_models.AuthenticationViewModel
import kotlinx.coroutines.flow.collectLatest


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val passwordValidator = PasswordValidator()
    private val authenticationViewModel : AuthenticationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)


        lifecycleScope.launchWhenCreated {
            authenticationViewModel.loginResource.collectLatest {
                println(it::class.java)
                when(it) {
                    is Resource.Success -> {
                        binding.loginSpinner.visibility = View.GONE
                        binding.loginErrorContent.visibility = View.GONE
                        //findNavController().animatedNavigate(LoginFragmentDirections.actionLoginFragmentToTempHomeFragment())
                    }
                    is Resource.Error -> {


//                        binding.loginErrorContent.title = it.errorTitle
//                        binding.loginErrorContent.description = it.errorDescription
                        binding.loginErrorContent.visibility = View.VISIBLE

                    }
                    is Resource.Loading -> {
                        binding.loginSpinner.visibility = View.VISIBLE
                        binding.loginErrorContent.visibility = View.INVISIBLE
                    }
                    is Resource.Nothing -> {
                        binding.loginSpinner.visibility = View.INVISIBLE
                        binding.loginErrorContent.visibility = View.INVISIBLE
                    }
                }
            }
        }

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
//            if(!validateInputs()) {
//                return@setOnClickListener
//            }
            authenticationViewModel.login(
                email = "tesst_hechim1@mailinator.com",//binding.loginEmailField.editText.text.toString(),
                password = "Hechim123*"//binding.loginPasswordField.editText.text.toString()
            )

        }

        binding.signUpLabel.setOnClickListener {
            findNavController().animatedNavigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    //error reset listeners, remove error labels when user starts typing again
    private fun attachInputListeners() {
        binding.loginEmailField.textInputLayout.errorResetListener()
        binding.loginPasswordField.textInputLayout.errorResetListener()
    }

}
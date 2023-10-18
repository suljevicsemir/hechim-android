package com.hechim.view

import android.graphics.Color
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.hechim.R
import com.hechim.databinding.FragmentChangePasswordBinding
import com.hechim.utils.Extensions.setFocusListener
import com.hechim.view_models.ChangePasswordViewModel
import kotlinx.coroutines.flow.collectLatest

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private var savedViewInstance: View? = null

    private val changePasswordViewModel: ChangePasswordViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(savedViewInstance != null){
            return savedViewInstance as View
        }



        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        binding.changePassToolbar.title = getString(R.string.change_password_title)
        binding.changePassToolbar.onBackPressed = {
            findNavController().popBackStack()
        }
        savedViewInstance = binding.root
        initBindings()
        initListeners()

        lifecycleScope.launchWhenStarted {
            changePasswordViewModel.passwordValidation.collectLatest {
                setTextColor(it.length, binding.changePassLengthText)
                setTextColor(it.containsSpecialCharacter, binding.changePassCharacterText)
                setTextColor(it.containsNumber, binding.changePassNumberText)
                setTextColor(it.containsUppercaseLetter, binding.changePassUppercaseText)
            }
        }

        return binding.root
    }


    private fun setTextColor(valid: Boolean, view: TextView) {
        if(!valid) {
            view.setTextColor(Color.parseColor("#F44336"))
            return
        }
        view.setTextColor(Color.parseColor("#4C8C2B"))
    }

    private fun initBindings() {
        binding.changePassOldPass.textInputLayout.setFocusListener(getString(R.string.change_pass_old_pass_label), getString(R.string.change_pass_old_pass_hint))
        binding.changePassNewPass.textInputLayout.setFocusListener(getString(R.string.change_pass_new_pass_label), getString(R.string.change_pass_new_pass_hint))
        binding.changePassConfirmPass.textInputLayout.setFocusListener(getString(R.string.change_pass_confirm_pass_label), getString(R.string.change_pass_confirm_pass_hint),)

        binding.changePassOldPass.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.changePassNewPass.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        binding.changePassConfirmPass.textInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        val passwordTransformation = PasswordTransformationMethod.getInstance()
        binding.changePassOldPass.editText.transformationMethod = passwordTransformation
        binding.changePassNewPass.editText.transformationMethod = passwordTransformation
        binding.changePassConfirmPass.editText.transformationMethod = passwordTransformation
    }

    private fun initListeners() {
        binding.changePassNewPass.editText.addTextChangedListener {
            println("value is: ${it.toString()}")
            changePasswordViewModel.onPasswordChanged(it.toString())
        }
    }


}


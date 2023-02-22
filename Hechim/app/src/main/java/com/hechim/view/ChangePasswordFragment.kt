package com.hechim.view

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.hechim.R
import com.hechim.databinding.FragmentChangePasswordBinding
import com.hechim.utils.Extensions.setFocusListener

class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private var savedViewInstance: View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(savedViewInstance != null){
            return savedViewInstance as View
        }

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        binding.changePassToolbar.title = "Change pass"
        binding.changePassToolbar.onBackPressed = {
            findNavController().popBackStack()
        }
        savedViewInstance = binding.root
        initBindings()

        return binding.root
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


}


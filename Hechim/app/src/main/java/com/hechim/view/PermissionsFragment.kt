package com.hechim.view

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController

import com.hechim.R
import com.hechim.adapters.LanguageSelectionAdapter
import com.hechim.adapters.PermissionsAdapter
import com.hechim.databinding.FragmentPermissionsBinding
import com.hechim.models.local.AlertDialogConfig
import com.hechim.models.local.AppLanguageItem
import com.hechim.models.local.AppLocale
import com.hechim.models.local.AppPermission
import com.hechim.utils.Extensions.openAppSystemSettings
import com.hechim.utils.Extensions.showDialog
import com.hechim.utils.Extensions.themeColor
import com.hechim.view_models.LanguageSelectionViewModel
import com.hechim.view_models.PermissionsViewModel


class PermissionsFragment : Fragment() {

    private lateinit var binding: FragmentPermissionsBinding

    private var savedViewInstance: View? = null
    private val permissionsViewModel: PermissionsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(savedViewInstance != null) {
            return savedViewInstance as View
        }
        binding = FragmentPermissionsBinding.inflate(inflater, container, false)



        val adapter = PermissionsAdapter(permissionsViewModel.appPermissions)

        binding.onboardingViewPager.adapter = adapter

        binding.permissionSkipButton.setOnClickListener {
            binding.onboardingViewPager.currentItem++
        }
        binding.permissionSkipButton.text = getString(R.string.permissions_skip)
        binding.permissionsButton.title = getString(R.string.permissions_button_allow)

        val intent = Intent()
        val packageName: String = requireContext().packageName
        val pm = requireContext().getSystemService(Context.POWER_SERVICE) as PowerManager?
        intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
        intent.data = Uri.parse("package:$packageName")
        requireContext().startActivity(intent)

        onPermissionAllow()


        return binding.root
    }

    private fun onPermissionAllow() {
        binding.permissionsButton.button.setOnClickListener {
            val permission: AppPermission = permissionsViewModel.appPermissions[binding.onboardingViewPager.currentItem]
            val shouldShow = shouldShowRequestPermissionRationale(permission.string )

            if(!shouldShow) {
                permissionLauncher.launch(permission.string)
            }
            else {
                showConfirmationDialog(permission)
            }
        }
    }

    private fun showConfirmationDialog(permission: AppPermission) {
        val title = String.format(
            getString(R.string.permission_dialog_title),
            getString(permission.title).lowercase()
        )

        activity?.showDialog(AlertDialogConfig(
            positiveButtonText = getString(R.string.permission_dialog_open_settings),
            negativeButtonText = getString(R.string.common_no),
            negativeButtonCallback = {

            },
            positiveButtonCallback = {
                requireContext().openAppSystemSettings()
            },
            title = title,
            description = getString(R.string.permission_dialog_title,)
        ))
    }


    private val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
    ) {
        val permission: AppPermission = permissionsViewModel.appPermissions[binding.onboardingViewPager.currentItem]
        permission.granted = it
        if(permission.string == android.Manifest.permission.ACCESS_FINE_LOCATION) {
            backgroundLocation()
        }
        binding.onboardingViewPager.currentItem++
      }

    private fun backgroundLocation() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

    }

    }
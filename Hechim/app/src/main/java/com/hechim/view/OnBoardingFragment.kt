package com.hechim.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.size
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.hechim.R
import com.hechim.adapters.OnBoardingAdapter
import com.hechim.databinding.FragmentOnBoardingBinding
import com.hechim.models.local.OnBoardingItem
import com.hechim.services.TestForeground
import com.hechim.utils.Extensions.animatedNavigate
import com.vmadalin.easypermissions.dialogs.SettingsDialog


class OnBoardingFragment : Fragment() {

    private lateinit var binding: FragmentOnBoardingBinding
    private var savedViewInstance: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(savedViewInstance != null) {
            return savedViewInstance as View
        }


        binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        savedViewInstance = binding.root

        val recyclerView = binding.onboardingViewPager
        val list = listOf(
            OnBoardingItem(
                title = getString(R.string.onboarding_meditating_title),
                description = getString(R.string.onboarding_meditating_desc).repeat(1),
                image = R.drawable.onboarding_meditating
            ),
            OnBoardingItem(
                title = getString(R.string.onboarding_planning_title),
                description = getString(R.string.onboarding_planning_desc),
                image = R.drawable.onboarding_planing
            ),
            OnBoardingItem(
                title = getString(R.string.onboarding_recording_title),
                description = getString(R.string.onboarding_recording_desc),
                image = R.drawable.onboarding_recording
            ),
            OnBoardingItem(
                title = getString(R.string.onboarding_connecting_title),
                description = getString(R.string.onboarding_connecting_desc),
                image = R.drawable.onboarding_connecting
            ),
        )
        val adapter = OnBoardingAdapter(list)
        recyclerView.adapter = adapter

        binding.serviceButton.button.setOnClickListener {
            startService()
        }

        buttonListener()
        setIndicatorInitialBackground()
        registerPageListener(adapter)
        return binding.root

    }

    private fun startService() {
        val serviceIntent = Intent(requireContext(), TestForeground::class.java)
        println("starting service")
        requireContext().startService(serviceIntent)
    }

    private fun buttonListener() {

        val button = binding.onBoardingButton
        val recyclerView = binding.onboardingViewPager
        button.button.setOnClickListener {

            if(recyclerView.currentItem == 3) {
                findNavController().animatedNavigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToLanguageSelectionFragment())
            }
            else {
                recyclerView.currentItem = recyclerView.currentItem + 1
            }
        }
    }

    private fun setIndicatorInitialBackground(){
        binding.onBoardingFirstIndicator.setBackgroundResource(R.drawable.on_boarding_selected_dot)
        binding.onBoardingSecondIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
        binding.onBoardingThirdIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
        binding.onBoardingFourthIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
    }


    private fun updateIndicators(position: Int) {
        when (position) {
            0 -> {
                binding.onBoardingFirstIndicator.setBackgroundResource(R.drawable.on_boarding_selected_dot)
                binding.onBoardingSecondIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingThirdIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingFourthIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingButton.button.text = getString(R.string.onboarding_next_button)
            }
            1 -> {
                binding.onBoardingFirstIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingSecondIndicator.setBackgroundResource(R.drawable.on_boarding_selected_dot)
                binding.onBoardingThirdIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingFourthIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
            }
            2 -> {
                binding.onBoardingFirstIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingSecondIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingThirdIndicator.setBackgroundResource(R.drawable.on_boarding_selected_dot)
                binding.onBoardingFourthIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
            }
            else -> {
                binding.onBoardingFirstIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingSecondIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingThirdIndicator.setBackgroundResource(R.drawable.on_boarding_default_dot)
                binding.onBoardingFourthIndicator.setBackgroundResource(R.drawable.on_boarding_selected_dot)
                binding.onBoardingButton.button.text = getString(R.string.onboarding_proceed)
            }
        }
    }

    private fun registerPageListener(adapter: OnBoardingAdapter) {

        binding.onboardingViewPager.registerOnPageChangeCallback( object: ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                updateIndicators(position)

            }

        })
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startService()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                SettingsDialog.Builder(requireActivity()).build().show()
            }
            else -> {
                //permission has not been asked yet
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if(it) {
            startService()
        }
        else {

        }
    }

    override fun onResume() {
        super.onResume()
        requestPermission()

    }


}
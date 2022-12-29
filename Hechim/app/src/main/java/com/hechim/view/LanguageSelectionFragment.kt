package com.hechim.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hechim.R
import com.hechim.adapters.LanguageSelectionAdapter
import com.hechim.databinding.FragmentLanguageSelectionBinding
import com.hechim.models.local.AppLanguageItem
import com.hechim.models.local.AppLocale
import com.hechim.utils.Extensions.themeColor
import com.hechim.view_models.LanguageSelectionViewModel
import kotlinx.coroutines.flow.collectLatest


class LanguageSelectionFragment : Fragment() {

    private lateinit var binding: FragmentLanguageSelectionBinding
    private var savedViewInstance: View? = null
    private val languageViewModel: LanguageSelectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if(savedViewInstance != null) {
            return savedViewInstance as View
        }
        binding = FragmentLanguageSelectionBinding.inflate(inflater, container, false)
        savedViewInstance = binding.root

        val adapter = LanguageSelectionAdapter(
            languageViewModel.languages.value.data!!,
            itemClickListener = object : LanguageSelectionAdapter.ItemClickListener {
                //list item on click
                override fun onItemClick(item: AppLanguageItem) {
                    //show confirmation dialog to the user
                    //to confirm the language change
                    showConfirmationDialog(
                        getString(item.text),
                        item.locale,
                    )
                }
            }
        )

        binding.languagesList.adapter = adapter
        binding.languagesList.layoutManager = LinearLayoutManager(activity)

        //listen to values when selected locale is changed and update
        //the list
        lifecycleScope.launchWhenStarted {
            languageViewModel.selectedLocale.collectLatest {
                adapter.notifyDataSetChanged()
            }
        }

        return binding.root
    }

    private fun showConfirmationDialog(language: String, appLocale: AppLocale, ) {
        if(appLocale.locale == languageViewModel.selectedLocale.value.locale) {
            return
        }
        val message = getString(R.string.language_selection_alert_content) + " $language" + "?"
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                //confirm language change with view model method call
                //recreate the activity to trigger loading of the new resource files
                //(handled inside MainActivity)
                setPositiveButton(R.string.common_ok) { dialog, id ->
                    languageViewModel.selectAndConfirm(appLocale)
                    (activity as FragmentActivity).recreate()
                }

                setNegativeButton(R.string.common_no) { _, _ -> }
            }
            //apply dialog title and message
            builder.apply {
                setTitle(R.string.langauge_selection_alert_title)
                setMessage(message)
            }
            // Set other dialog properties
            // Create the AlertDialog
            builder.create()
        }
        alertDialog?.setOnShowListener {
            val buttonColor = requireContext().themeColor(com.google.android.material.R.attr.colorSecondary)
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)!!.setTextColor(buttonColor)
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)!!.setTextColor(buttonColor)
        }

        alertDialog?.show()
    }


}
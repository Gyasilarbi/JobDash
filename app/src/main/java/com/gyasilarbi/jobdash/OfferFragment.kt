package com.gyasilarbi.jobdash

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.gyasilarbi.jobdash.databinding.FragmentOfferBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val CURRENCY_SYMBOL = "GH₵ "

class OfferFragment<T> : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentOfferBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOfferBinding.inflate(inflater, container, false)
        setupCategorySpinner()
        setupAmountInput()
        binding.LocationSettings.setOnClickListener {
            navigateTo(MapsActivity::class.java)
        }
        return binding.root
    }

    private fun setupCategorySpinner() {
        val categoryEt = binding.categoryEt
        val cities = arrayOf("Accra", "Kumasi", "Takoradi", "Cape Coast", "Sunyani")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryEt.adapter = adapter

        categoryEt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCity = cities[position]
                Toast.makeText(requireContext(), "Selected city: $selectedCity", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun setupAmountInput() {
        val inputAmount: TextInputEditText = binding.inputAmount
        inputAmount.setText(CURRENCY_SYMBOL)

        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            if (dstart < CURRENCY_SYMBOL.length) {
                // Prevent changes to the currency symbol part
                return@InputFilter TextUtils.concat(dest.subSequence(dstart, dend), source.subSequence(start, end))
            }
            null
        }

        inputAmount.filters = arrayOf(filter)
    }

    private fun navigateTo(activityClass: Class<*>) {
        startActivity(Intent(requireContext(), activityClass))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OfferFragment<Any>().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

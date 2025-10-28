package com.example.homework5

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class ResultFragment : Fragment(R.layout.frag_result) {
    private val vm: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val txtResult = view.findViewById<TextView>(R.id.txtResult)

        vm.status.observe(viewLifecycleOwner) { status ->
            txtResult.text = status
        }
    }
}

package com.example.solarenergy.ui.Solar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.solarenergy.AutomaticSolarEnergyActivity
import com.example.solarenergy.ManualSolarEnergyModeActivity
import com.example.solarenergy.R
import kotlinx.android.synthetic.main.fragment_solarenergy.*

class SolarFragment : Fragment() {

    private lateinit var galleryViewModel: SolarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        galleryViewModel = ViewModelProviders.of(this).get(SolarViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_solarenergy, container, false)
      //  val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
      //      textView.text = it
        })
        return root
    }

    override fun onStart() {
        super.onStart()

        btn_AutomaticSolar.setOnClickListener {

            val intent = Intent(context,AutomaticSolarEnergyActivity::class.java)
            startActivity(intent)
        }
        btn_ManualSolar.setOnClickListener {

            val intent = Intent(context,ManualSolarEnergyModeActivity::class.java)
            startActivity(intent)
        }
    }
}

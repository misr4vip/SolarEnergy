package com.example.solarenergy.ui.Connection

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.solarenergy.R
import kotlinx.android.synthetic.main.fragment_connection.*
import java.util.ArrayList

class ConnectionFragment : Fragment() {
    private var myBluetooth: BluetoothAdapter? = null
    private var pairedDevices: Set<BluetoothDevice>? = null
    private lateinit var slideshowViewModel: ConnectionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        slideshowViewModel = ViewModelProviders.of(this).get(ConnectionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_connection, container, false)
     //   val textView: TextView = root.findViewById(R.id.text_slideshow)
        slideshowViewModel.text.observe(viewLifecycleOwner, Observer {
   //         textView.text = it
        })
        return root

    }

    override fun onStart() {
        super.onStart()
        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter()
        if (myBluetooth == null) { //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(context, "Bluetooth Device Not Available", Toast.LENGTH_LONG).show()
        } else if (!myBluetooth!!.isEnabled) { //Ask to the user turn the bluetooth on
            val turnBTon = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(turnBTon, 1)
        }
        btnPaired!!.setOnClickListener { pairedDevicesList() }
    }
    private fun pairedDevicesList() {
        pairedDevices = myBluetooth!!.bondedDevices
        val list: ArrayList<String> = ArrayList<String>()
        if ((pairedDevices as MutableSet<BluetoothDevice>?)!!.isNotEmpty()) {
            (pairedDevices as MutableSet<BluetoothDevice>?)!!.forEach { bt ->
                list.add(bt.name + "\n" + bt.address) //Get the device's name and the address
            }
        } else {
            Toast.makeText(context, "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show()
        }
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, list)
        devicelist!!.adapter = adapter
        devicelist!!.onItemClickListener =
            myListClickListener //Method called when the device from the list is clicked
    }

    private val myListClickListener =
        AdapterView.OnItemClickListener { av, v, arg2, arg3 ->
            // Get the device MAC address, the last 17 chars in the View
            val info = (v as TextView).text.toString()
            val address = info.substring(info.length - 17)
            val sharedPref = activity?.getSharedPreferences("BlutoothAddress",Context.MODE_PRIVATE)
            sharedPref!!.edit() {
                putString("bAdrress",address)
                commit()
            }


        }

}

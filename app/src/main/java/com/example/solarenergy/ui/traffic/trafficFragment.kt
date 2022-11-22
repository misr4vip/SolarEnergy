package com.example.solarenergy.ui.traffic

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.solarenergy.ManualTrafficModeActivity
import com.example.solarenergy.R
import kotlinx.android.synthetic.main.fragment_traffic.*
import java.io.IOException
import java.util.*

class trafficFragment : Fragment() {

    private lateinit var galleryViewModel: trafficViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        galleryViewModel = ViewModelProviders.of(this).get(trafficViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_traffic, container, false)
      //  val textView: TextView = root.findViewById(R.id.text_gallery)
        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
     //       textView.text = it
        })
        return root
    }
    var address: String? = null
    private var progress: ProgressDialog? = null
    var myBluetooth: BluetoothAdapter? = null
    var btSocket: BluetoothSocket? = null
    private var isBtConnected = false
    val myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    override fun onStart() {
        super.onStart()

        btn_SaveTraffic.setOnClickListener {

            if (rb_auto_traffic.isChecked)
            {
                val sharedPref = context!!.getSharedPreferences("BlutoothAddress", Context.MODE_PRIVATE)
                val bAddress = sharedPref.getString("bAdrress","")

                address = bAddress
                ConnectBT().execute()
                if (btSocket != null) {
                    try {
                        btSocket!!.getOutputStream().write("1".toByteArray())
                    } catch (e: IOException) {
                        msg("Error")
                    }
                }
            }
            if (rb_manual_traffic.isChecked)
            {

                val intent = Intent(context,ManualTrafficModeActivity::class.java)
                startActivity(intent)
            }
        }
    }
    inner class ConnectBT : AsyncTask<Void?, Void?, Void?>() // UI thread
    {
        private var ConnectSuccess = true //if it's here, it's almost connected
        override fun onPreExecute() {
            progress = ProgressDialog.show(context, "Connecting...", "Please wait!!!") //show a progress dialog
        }

        override fun doInBackground(vararg params: Void?): Void? //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    // myBluetooth = BluetoothAdapter.getDefaultAdapter() //get the mobile bluetooth device
                    val dispositivo: BluetoothDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address) //connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID) //create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()

                    btSocket!!.connect() //start connection
                }
            } catch (e: IOException) {
                ConnectSuccess = false //if the try failed, you can check the exception here
            }
            return null
        }

        override fun onPostExecute(result: Void?) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result)
            if (!ConnectSuccess) {
                msg("Connection Failed.  Try again.")

            } else {
                msg("Connected.")
                isBtConnected = true
            }
            progress!!.dismiss()
        }




    }
    private fun msg(s: String) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show()
    }
    private fun Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket!!.close() //close connection
            } catch (e: IOException) {
                msg("Error")
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()

            Disconnect()

    }
}

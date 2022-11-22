package com.example.solarenergy

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_manual_solar_energy_mode.*
import java.io.IOException
import java.util.*

class ManualSolarEnergyModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_solar_energy_mode)
    }

    var address: String? = null
    private var progress: ProgressDialog? = null
    var myBluetooth: BluetoothAdapter? = null
    var btSocket: BluetoothSocket? = null
    private var isBtConnected = false
    val myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    override fun onStart() {
        super.onStart()
        val sharedPref = this.getSharedPreferences("BlutoothAddress", MODE_PRIVATE)
        val bAddress = sharedPref.getString("bAdrress","")

        address = bAddress

        ConnectBT().execute()

        btn_SaveMode.setOnClickListener {
            if (btSocket != null)
            {
                try {
                    if (rb_fullOpt.isChecked)
                    {
                        btSocket!!.getOutputStream().write("9".toByteArray())
                    }
                    if (rb_MedOpt.isChecked)
                    {
                        btSocket!!.getOutputStream().write("11".toByteArray())
                    }
                    if (rb_LowOpt.isChecked)
                    {
                        btSocket!!.getOutputStream().write("13".toByteArray())
                    }

                    if (rb_CustomOpt.isChecked)
                    {
                        val intent = Intent(applicationContext,CustomOptimzatioActivity::class.java)
                        startActivity(intent)
                    }
                } catch (e: IOException) {
                    msg("Error")
                }
            }
        }




    }
    inner class ConnectBT : AsyncTask<Void?, Void?, Void?>() // UI thread
    {
        private var ConnectSuccess = true //if it's here, it's almost connected
        override fun onPreExecute() {
            progress = ProgressDialog.show(this@ManualSolarEnergyModeActivity, "Connecting...", "Please wait!!!") //show a progress dialog
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
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
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

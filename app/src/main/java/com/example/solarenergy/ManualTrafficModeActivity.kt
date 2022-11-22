package com.example.solarenergy

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.solarenergy.ui.Connection.ConnectionFragment
import kotlinx.android.synthetic.main.activity_manual_traffic_mode.*
import java.io.IOException
import java.util.*

class ManualTrafficModeActivity : AppCompatActivity() {

    var address: String? = null
    private var progress: ProgressDialog? = null
    var myBluetooth: BluetoothAdapter? = null
     var btSocket: BluetoothSocket? = null
    private var isBtConnected = false
    val myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_traffic_mode)
        val sharedPref = this.getSharedPreferences("BlutoothAddress", Context.MODE_PRIVATE)
        val bAddress = sharedPref.getString("bAdrress","")

        address = bAddress
    }

    override fun onStart() {
        super.onStart()
        ConnectBT().execute()
        btnFirstSignal.setOnClickListener {
            FirstSignal()
        }
        btnSecondSignal.setOnClickListener {
            SecondSignal()
        }
        btnThirdSignal.setOnClickListener {
            ThirdSignal()
        }
        btnFourthSignal.setOnClickListener {
            FourthSignal()
        }
//        btnAutoMaticMode.setOnClickListener {
//            TrafficAutomaticMode()
//        }
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
        finish() //return to the first layout
    }
    private fun TrafficAutomaticMode() {
        if (btSocket != null) {
            try {
                btSocket!!.getOutputStream().write("1".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }
        }
    }
    private fun FirstSignal() {
        if (btSocket != null) {
            try {
                btSocket!!.getOutputStream().write("3".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }
        }
    }

    private fun SecondSignal() {
        if (btSocket != null) {
            try {
                btSocket!!.getOutputStream().write("4".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }
        }
    }
    private fun ThirdSignal() {
        if (btSocket != null) {
            try {
                btSocket!!.getOutputStream().write("5".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }
        }
    }
    private fun FourthSignal() {
        if (btSocket != null) {
            try {
                btSocket!!.getOutputStream().write("6".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }
        }
    }
    // fast way to call Toast
    private fun msg(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_LONG).show()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_led_control, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }



    inner class ConnectBT : AsyncTask<Void?, Void?, Void?>() // UI thread
    {
        private var ConnectSuccess = true //if it's here, it's almost connected
        override fun onPreExecute() {
            progress = ProgressDialog.show(this@ManualTrafficModeActivity, "Connecting...", "Please wait!!!") //show a progress dialog
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
                finish()
            } else {
                msg("Connected.")
                isBtConnected = true
            }
            progress!!.dismiss()
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        Disconnect()
    }
}

package com.example.solarenergy

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_automatic_solar_energy.*
import java.io.IOException
import java.util.*

class AutomaticSolarEnergyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_automatic_solar_energy)


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

        btn_getData.setOnClickListener {
            if (btSocket != null)
            {
                try {

                    btSocket!!.getOutputStream().write("8".toByteArray())

                    if (btSocket!!.isConnected){
                        if (btSocket!!.inputStream.available() > 0 )
                        {
                            val byteCount: Int = btSocket!!.inputStream.available()
                            if (byteCount > 0) {
                                val rawBytes = ByteArray(byteCount)
                                btSocket!!.inputStream.read(rawBytes)
                                val string = String(rawBytes, charset("UTF-8"))
                                var temp = string.substring(0,4)
                                var batary = string.substring(5,string.count())
                                tv_Temperature.text = temp
                                tv_bataryCharge.text =batary

                                 when(batary.toDouble())
                                {
                                    in 76.0 ..100.00 ->
                                        if (temp.toDouble() > 29)
                                        {

                                            SunnyDayWithFullCharge()
                                        }else
                                        {
                                            CloudDayWithFullCharge()
                                        }
                                   in 51.0 ..75.99 ->
                                       if (temp.toDouble() > 29 )
                                       {
                                           SunnyDayWithMeduimCharge()

                                       }else
                                       {
                                           CloudDayWithMeduimCharge()

                                       }

                                   in 0.0 ..50.99 ->
                                       if (temp.toDouble() > 29 )
                                       {
                                           SunnyDayWithLowCharge()

                                       }else
                                       {
                                           CloudDayWithLowCharge()

                                       }
                                }
                            }

                        }

                    }else
                    {
                        Toast.makeText(this,"There Is an Error in Geting data from arduino",Toast.LENGTH_LONG).show()
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
            progress = ProgressDialog.show(this@AutomaticSolarEnergyActivity, "Connecting...", "Please wait!!!") //show a progress dialog
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

    private fun SunnyDayWithFullCharge()
    {

            tv_Result.text = " It's Mostly Sunny Day with Suitable Charge Of Batary Have A nice Day Your System Is Worked Fine In Low Optimization Mode"
        if (btSocket != null)
        {
            try {

                btSocket!!.getOutputStream().write("9".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }

        }
    }
    private fun CloudDayWithFullCharge()
    {

        tv_Result.text = " It's A Cloud Day with Suitable Charge Of Batary Have A nice Day Your System Is Worked Fine In Low Optimization Mode"
        if (btSocket != null)
        {
            try {

                btSocket!!.getOutputStream().write("10".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }

        }
    }


    ////////////////////////////////////////////////////////////////////////////

    private fun SunnyDayWithMeduimCharge()
    {

        tv_Result.text = " It's Mostly Sunny Day with Meduim Charge Of Batary Have A nice Day Your System Is Worked Fine In Meduim Optimization Mode"
        if (btSocket != null)
        {
            try {

                btSocket!!.getOutputStream().write("11".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }

        }
    }
    private fun CloudDayWithMeduimCharge()
    {

        tv_Result.text = " It's A Cloud Day with Meduim Charge Of Batary Have A nice Day Your System Is Worked Fine In Meduim Optimization Mode"
        if (btSocket != null)
        {
            try {

                btSocket!!.getOutputStream().write("12".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }

        }
    }
    ////////////////////////////////////////////////////////////////////////////

    private fun SunnyDayWithLowCharge()
    {

        tv_Result.text = " It's Mostly Sunny Day with Low Charge Of Batary Have A nice Day Your System Is Worked Fine In Full Optimization Mode"
        if (btSocket != null)
        {
            try {

                btSocket!!.getOutputStream().write("13".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }

        }
    }
    private fun CloudDayWithLowCharge()
    {

        tv_Result.text = " It's A Cloud Day with Low Charge Of Batary Have A nice Day Your System Is Worked Fine In Full Optimization Mode"
        if (btSocket != null)
        {
            try {

                btSocket!!.getOutputStream().write("14".toByteArray())
            } catch (e: IOException) {
                msg("Error")
            }

        }
    }

}

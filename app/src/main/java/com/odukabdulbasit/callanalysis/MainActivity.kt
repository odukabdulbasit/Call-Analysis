package com.odukabdulbasit.callanalysis

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val requestReadLog = 2

    var istenilen = 1

    var moreSpokenPersons : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val kabul = intent.getStringExtra("kabul")

        // Telefondan ilgili izinerin alınmasını sağlıyorum. eyer alınmışsa işlemi yapıyor. Yapılmamışsa izin istiyor.
        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED
         || ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){

            loadData()
        }
        else{
            if (kabul == "kabul"){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_CALL_LOG,android.Manifest.permission.READ_CONTACTS),
                    requestReadLog
                )
            }else{
                val intent = Intent(this, PermissionActivity ::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestReadLog) loadData()
    }

    private fun loadData(){

        val list = getCallsDetails(this)

        val adapter = ListAdapter(this,list)

        list_View.adapter = adapter

    }

    private fun getCallsDetails(context: Context): ArrayList<CallDetails> {
        var callDetails = ArrayList<CallDetails>()

        if (istenilen == 1){
            callDetails = getAllCallDetails(context)
        }
        else if (istenilen == 2){
            callDetails = getİncomingCallDetails(context)

        }
        else if (istenilen == 3){
            callDetails = getOutgoingCallDetails(context)

        }
        else{
            callDetails = getMissedCallDetails(context)

        }
        return callDetails
    }

    //üstten alınan callerURİ den callerName döndürülüyor burada
    private fun getCallerName(callerNameUri: String?): String {

        return  if(callerNameUri != null){

            //cursor tanımlıyorum
            val cursor = contentResolver.query(Uri.parse(callerNameUri),null,null,null, null)

            var name =""
            if ((cursor?.count ?: 0) > 0){
                while (cursor != null && cursor.moveToNext()){ //cursordan name'i alıyorum.
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                }
            }
            cursor!!.close()//cursoru kapatıyorum.

            return name
        }else{
            "Not a contact"
        }
    }

   fun graphicsButtonClicked(view: View){

       var gSortAl = getArraySortCallList()
       var c = gSortAl.count()
       val a1 = gSortAl[c -1]
       val a2 = gSortAl[c -2]
       val a3 = gSortAl[c-3]
       val a4 = gSortAl[c -4]
       val a5 = gSortAl[c -5]
       val a6 = gSortAl[c -6]
       var intent = Intent(this, CallGrapficsActivity ::  class.java)

       intent.putExtra("a1", a1)
       intent.putExtra("a2", a2)
       intent.putExtra("a3", a3)
       intent.putExtra("a4", a4)
       intent.putExtra("a5", a5)
       intent.putExtra("a6", a6)
        startActivity(intent)
    }

    fun tumuButtonClicked(view: View){
        if (istenilen != 1){
            istenilen = 1
            loadData()
        }

    }

    fun gelenButtonClicked(view: View){

        if (istenilen != 2){
            istenilen = 2
            loadData()
        }
    }
    fun gidenButtonClicked(view: View){

        if (istenilen != 3){
            istenilen = 3
            loadData()
        }
    }
    fun cevapsizButtonClicked(view: View){

        if (istenilen != 4){
            istenilen = 4
            loadData()
        }
    }

    fun getAllCallDetails(context: Context): ArrayList<CallDetails> {

        val callDetails = ArrayList<CallDetails>()
        val contentUri = CallLog.Calls.CONTENT_URI

        try {
            val cursor = context.contentResolver.query(contentUri, null, null, null, null)
            val nameUri = cursor!!.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val duraction = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val date = cursor.getColumnIndex(CallLog.Calls.DATE)
            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)

            if (cursor.moveToFirst()) {
                do {
                    val callType = when (cursor.getInt(type)) {
                        CallLog.Calls.INCOMING_TYPE -> "İncoming"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        CallLog.Calls.REJECTED_TYPE -> "Rejected"
                        else -> "Not Definied"
                    }
                    val phoneNumber = cursor.getString(number)
                    val callerNameUri = cursor.getString(nameUri)
                    val callDate = cursor.getString(date)
                    val callDayTime = Date(callDate.toLong()).toString()
                    val callDuration = cursor.getString(duraction)

                    val callername = getCallerName(callerNameUri)

                    moreSpokenPersons.add(callername + "," + callDuration)

                    callDetails.add(
                        CallDetails(
                            callername,
                            phoneNumber,
                            callDuration,
                            callType,
                            callDayTime
                        )
                    )
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SecurityException) {
            Toast.makeText(this, "User denied permission", Toast.LENGTH_SHORT).show()
        }
        return callDetails
    }

    fun getİncomingCallDetails(context: Context): ArrayList<CallDetails> {

        val callDetails = ArrayList<CallDetails>()
        val contentUri = CallLog.Calls.CONTENT_URI

        try {
            val cursor = context.contentResolver.query(contentUri, null, null, null, null)
            val nameUri = cursor!!.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val duraction = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val date = cursor.getColumnIndex(CallLog.Calls.DATE)
            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)

            if (cursor.moveToFirst()) {
                do {
                    val callType = when (cursor.getInt(type)) {
                        CallLog.Calls.INCOMING_TYPE -> "İncoming"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        CallLog.Calls.REJECTED_TYPE -> "Rejected"
                        else -> "Not Definied"
                    }
                    val phoneNumber = cursor.getString(number)
                    val callerNameUri = cursor.getString(nameUri)
                    val callDate = cursor.getString(date)
                    val callDayTime = Date(callDate.toLong()).toString()
                    val callDuration = cursor.getString(duraction)

                    val callername = getCallerName(callerNameUri)

                    moreSpokenPersons.add(callername + "," + callDuration)

                    if (callType == "İncoming"){
                        callDetails.add(
                            CallDetails(
                                callername,
                                phoneNumber,
                                callDuration,
                                callType,
                                callDayTime
                            )
                        )
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SecurityException) {
            Toast.makeText(this, "User denied permission", Toast.LENGTH_SHORT).show()
        }
        return callDetails
    }

    fun getOutgoingCallDetails(context: Context): ArrayList<CallDetails> {

        val callDetails = ArrayList<CallDetails>()
        val contentUri = CallLog.Calls.CONTENT_URI

        try {
            val cursor = context.contentResolver.query(contentUri, null, null, null, null)
            val nameUri = cursor!!.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val duraction = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val date = cursor.getColumnIndex(CallLog.Calls.DATE)
            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)

            if (cursor.moveToFirst()) {
                do {
                    val callType = when (cursor.getInt(type)) {
                        CallLog.Calls.INCOMING_TYPE -> "İncoming"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        CallLog.Calls.REJECTED_TYPE -> "Rejected"
                        else -> "Not Definied"
                    }
                    val phoneNumber = cursor.getString(number)
                    val callerNameUri = cursor.getString(nameUri)
                    val callDate = cursor.getString(date)
                    val callDayTime = Date(callDate.toLong()).toString()
                    val callDuration = cursor.getString(duraction)

                    val callername = getCallerName(callerNameUri)

                    moreSpokenPersons.add(callername + "," + callDuration)

                    if (callType == "Outgoing"){
                        callDetails.add(
                            CallDetails(
                                callername,
                                phoneNumber,
                                callDuration,
                                callType,
                                callDayTime
                            )
                        )
                    }

                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SecurityException) {
            Toast.makeText(this, "User denied permission", Toast.LENGTH_SHORT).show()
        }
        return callDetails
    }

    fun getMissedCallDetails(context: Context): ArrayList<CallDetails> {

        val callDetails = ArrayList<CallDetails>()
        val contentUri = CallLog.Calls.CONTENT_URI

        try {
            val cursor = context.contentResolver.query(contentUri, null, null, null, null)
            val nameUri = cursor!!.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val duraction = cursor.getColumnIndex(CallLog.Calls.DURATION)
            val date = cursor.getColumnIndex(CallLog.Calls.DATE)
            val type = cursor.getColumnIndex(CallLog.Calls.TYPE)

            if (cursor.moveToFirst()) {
                do {
                    val callType = when (cursor.getInt(type)) {
                        CallLog.Calls.INCOMING_TYPE -> "İncoming"
                        CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                        CallLog.Calls.MISSED_TYPE -> "Missed"
                        CallLog.Calls.REJECTED_TYPE -> "Rejected"
                        else -> "Not Definied"
                    }
                    val phoneNumber = cursor.getString(number)
                    val callerNameUri = cursor.getString(nameUri)
                    val callDate = cursor.getString(date)
                    val callDayTime = Date(callDate.toLong()).toString()
                    val callDuration = cursor.getString(duraction)

                    val callername = getCallerName(callerNameUri)

                    moreSpokenPersons.add(callername + "," + callDuration)

                    if (callType == "Missed"){
                        callDetails.add(
                            CallDetails(
                                callername,
                                phoneNumber,
                                callDuration,
                                callType,
                                callDayTime
                            )
                        )
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SecurityException) {
            Toast.makeText(this, "User denied permission", Toast.LENGTH_SHORT).show()
        }
        return callDetails
    }


    fun getArrayForGraphics() : Map<String, Int>{

        val numbersMap = mutableMapOf("one" to 1, "two" to 2)

        for(i in moreSpokenPersons) {

            val d : List<String>  = i.split(',')

            if (d[0] in numbersMap){

                numbersMap[d[0]] = d[1].toInt() + numbersMap.getValue(d[0])
            }else{

                numbersMap.put(d[0] , d[1].toInt())
            }
        }
        return numbersMap
    }

    fun getArraySortCallList() : ArrayList<MoreSpokenModel>{
       val newAl : ArrayList<MoreSpokenModel> = ArrayList()
       val gelenMap = getArrayForGraphics()
       for (i in gelenMap){
           newAl.add(MoreSpokenModel(i.key, i.value))
       }

       newAl.sortBy { it.duraction }
       return newAl
   }
}


/*val callDetails = ArrayList<CallDetails>()
       val contentUri = CallLog.Calls.CONTENT_URI

       try {
           val cursor = context.contentResolver.query(contentUri,null,null, null, null)
           val nameUri = cursor!!.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
           val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
           val duraction = cursor.getColumnIndex(CallLog.Calls.DURATION)
           val date = cursor.getColumnIndex(CallLog.Calls.DATE)
           val type = cursor.getColumnIndex(CallLog.Calls.TYPE)

           if (cursor.moveToFirst()){
               do {
                   val callType = when(cursor.getInt(type)){
                       CallLog.Calls.INCOMING_TYPE -> "İncoming"
                       CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                       CallLog.Calls.MISSED_TYPE -> "Missed"
                       CallLog.Calls.REJECTED_TYPE -> "Rejected"
                       else -> "Not Definied"
                   }
                   val phoneNumber = cursor.getString(number)
                   val callerNameUri = cursor.getString(nameUri)
                   val callDate = cursor.getString(date)
                   val callDayTime = Date(callDate.toLong()).toString()
                   val callDuration = cursor.getString(duraction)

                   val callername = getCallerName(callerNameUri)

                   moreSpokenPersons.add(callername+","+ callDuration)

                   callDetails.add(CallDetails(
                       callername,
                       phoneNumber,
                       callDuration,
                       callType,
                       callDayTime
                   ))
               }while (cursor.moveToNext())
           }
           cursor.close()
       }catch (e : SecurityException){
           Toast.makeText(this, "User denied permission", Toast.LENGTH_SHORT).show()
       }
       return  callDetails */
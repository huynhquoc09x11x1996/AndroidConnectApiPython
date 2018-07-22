package com.example.quocb14005xx.apipython

import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.provider.MediaStore
import android.graphics.Bitmap
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.os.*
import android.widget.Toast
import com.example.quocb14005xx.apipython.adapter.AdapterHistoryVocabulary

import com.example.quocb14005xx.apipython.api.APIService
import com.example.quocb14005xx.apipython.db.DatabaseVocabularyHelper
import com.example.quocb14005xx.apipython.model.HistoryVocabulary
import com.example.quocb14005xx.apipython.util.Utils
import com.example.quocb14005xx.apipython.util.Utils.encodeToBase64
import com.example.quocb14005xx.apipython.view.UpdateUI
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dialog_choose_image.*


class MainActivity : AppCompatActivity(), UpdateUI {
    private lateinit var dialogProcess: ProgressDialog
    private lateinit var dialog: Dialog
    private lateinit var listener: UpdateUI
    private val TAG = "HUYNHBAOQUOC"
    private var imgEncodedString: String = ""
    private val PICK_IMAGE_REQUEST = 999
    private val CAPTURE_IMAGE_REQUEST = 9999
    private lateinit var historyVocabularyHelper: DatabaseVocabularyHelper

    private lateinit var listData: ArrayList<HistoryVocabulary>
    private lateinit var adapter: AdapterHistoryVocabulary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listener = this

        initialize()

        addEvents()

    }

    private fun initialize() {
        historyVocabularyHelper = DatabaseVocabularyHelper(this)

        listData = ArrayList()
        listData = historyVocabularyHelper.queryALlHistory()

        adapter = AdapterHistoryVocabulary(this, listData)
        lvHistory.adapter = adapter


    }


    private fun addEvents() {
        img.setOnClickListener {
            dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_choose_image)
            dialog.show()
            dialog.btnLibraryImage.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
            }
            dialog.btnNewImage.setOnClickListener {
                val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAPTURE_IMAGE_REQUEST)
            }

        }
        btnClick.setOnClickListener {
            dialogProcess = ProgressDialog(this)
            dialogProcess.setTitle("Đang dự đoán ảnh này là gì...")

            if (imgEncodedString != "") {
                dialogProcess.show()
                val taskProcessResponeRetrofitInMainThread = ProcessResponeRetrofitInMainThread(this)
                taskProcessResponeRetrofitInMainThread.execute()

            }//if null
            else {
                Toast.makeText(this, "Chọn hoặc chụp ảnh để học từ vựng!", Toast.LENGTH_LONG).show()
            }
        }//btn click
        lvHistory.setOnItemLongClickListener { parent, view, position, id ->
            Log.e(TAG,historyVocabularyHelper.delete(position).toString()+" delete response")
            Toast.makeText(this,"Xoa vocabulary ${position}",Toast.LENGTH_LONG).show()
            notifyListView()
            true
        }
    }


    override fun update(text: String) {
        Log.e(TAG, "Interface log: $text")
        txtResult.text = text
    }

    override  fun notifyListView() {
        listData = historyVocabularyHelper.queryALlHistory()
        adapter= AdapterHistoryVocabulary(this,listData)
        lvHistory.adapter=adapter
        adapter.notifyDataSetChanged()
    }
    override fun closeProgressDialog() {
        dialogProcess.dismiss()
    }

    override fun displayProgressDialog() {
        dialogProcess.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                    imgEncodedString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100)
                    Log.e(TAG, imgEncodedString)
                    img.setImageBitmap(bitmap)
                }
                CAPTURE_IMAGE_REQUEST -> {
                    val bitmap = data.extras.get("data") as Bitmap
                    imgEncodedString = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100)
                    Log.e(TAG, imgEncodedString)
                    img.setImageBitmap(bitmap)
                }
            }
            dialog.dismiss()
        }
    }


    /*
    * class Asynctask return kết quả trả về khi POST request lên API và trả về ResponesBody là két quả mong muốn
    * */
    inner class ProcessResponeRetrofitInMainThread : AsyncTask<Void, Void, String> {
        private var listener: UpdateUI
        private var mHandler = Handler()

        constructor(listenerActivity: UpdateUI) {
            listener = listenerActivity
        }

        override fun doInBackground(vararg params: Void?): String {
            val jsonObj = JsonObject()
            jsonObj.addProperty("image", imgEncodedString)
            val apiService = APIService.create().getPredictLabelPost(jsonObj)
            try {
                val label = apiService.execute().body()!!.string()
                return label
            } catch (e: Exception) {
                Log.e(TAG, "doInBackGround exception $e")
                return "[ERROR] Server bị die hoặc internet bạn chưa kết nối!"
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            Log.e(TAG, "onPostExcute: $result")
            txtResult.text = result
            if (!result!!.contains("[ERROR]")) {
                val insertSuccess = historyVocabularyHelper.insert(HistoryVocabulary(imgEncodedString, result!!, Utils.now(System.currentTimeMillis())))
                Log.e(TAG,insertSuccess.toString()+ "insert response")
                if (insertSuccess) {
                    listener.notifyListView()
                }
            }
            listener.closeProgressDialog()
        }

    }

}




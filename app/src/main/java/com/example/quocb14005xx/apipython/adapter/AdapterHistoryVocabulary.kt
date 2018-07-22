package com.example.quocb14005xx.apipython.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.quocb14005xx.apipython.R
import com.example.quocb14005xx.apipython.model.HistoryVocabulary
import kotlinx.android.synthetic.main.item_vocabulary_search.view.*
import java.util.*

class AdapterHistoryVocabulary(var ctx: Context, var listData: ArrayList<HistoryVocabulary>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var row = LayoutInflater.from(ctx).inflate(R.layout.item_vocabulary_search,null)
        val item = listData.get(position)

        val imageByte = Base64.decode(item.base64str,Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imageByte,0,imageByte.size)
        row.imgItem.setImageBitmap(bitmap)
        row.txtContentItem.text=item.content
        row.txtDateItem.text=item.date
        return row
    }

    override fun getItem(position: Int): Any {
        return listData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listData.size
    }
}
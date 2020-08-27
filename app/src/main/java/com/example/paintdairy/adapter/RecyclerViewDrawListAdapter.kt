package com.example.paintdairy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.paintdairy.R
import com.example.paintdairy.dataclass.Draws

class RecyclerViewDrawListAdapter(
    private val context : Context
) :
    RecyclerView.Adapter<RecyclerViewDrawListAdapter.ViewHolder>() {
    var mInList = mutableListOf<Draws>()
    val inflater: LayoutInflater = LayoutInflater.from(context)
    var mOnItemCheckListener: OnItemCheckListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflater.inflate(R.layout.recyclerview_drawlist_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  mInList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var mCheckBox = view.findViewById<CheckBox>(R.id.checkBoxDraw)
        var mDrawImageView= view.findViewById<ImageView>(R.id.imageViewDraw)
        var mTextViewDrawFileName= view.findViewById<TextView>(R.id.textViewDrawFileName)

        fun bind(position: Int){
            mCheckBox.isChecked = mInList[position].checked
            mCheckBox.setOnClickListener {
                mOnItemCheckListener!!.onCheck(position)
            }
            Glide.with(context!!)
            .load(context.getExternalFilesDir(null)!!.toString() + "/${mInList[position].drawPath}")
            .into(mDrawImageView)
            mDrawImageView.setOnClickListener {
                var mPath = context.getExternalFilesDir(null)!!.toString() + "/${mInList[position].drawPath}"
                mOnItemCheckListener!!.onSelect(mPath)
            }
            mTextViewDrawFileName.text = mInList[position].drawPath
        }
    }

    fun UpdateData(mList:MutableList<Draws>){
        mInList=mList
        notifyDataSetChanged()
    }
    fun Refresh(){
        notifyDataSetChanged()
    }
    interface OnItemCheckListener {
        fun onCheck(mPosition:Int)
        fun onSelect(mDrawPath:String)
    }
    fun setOnItemCheckListener(mOnItemCheckListener: OnItemCheckListener) {
        this.mOnItemCheckListener = mOnItemCheckListener
    }
}
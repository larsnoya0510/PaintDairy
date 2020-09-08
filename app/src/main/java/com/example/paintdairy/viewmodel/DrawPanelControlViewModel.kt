package com.example.paintdairy.viewmodel

import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.set
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paintdairy.customview.CustomPanel

class DrawPanelControlViewModel : ViewModel() {
    private var drawPanelLiveData = MutableLiveData<CustomPanel>()
    fun setDrawPanelData(mPanel: CustomPanel) {
        drawPanelLiveData.value = mPanel
    }

    fun getDrawPanelData(): MutableLiveData<CustomPanel> {
        return drawPanelLiveData
    }

    fun setDrawColor(mColor: Int) {
        var mPanel: CustomPanel = drawPanelLiveData.value!!
        mPanel.drawColor = mColor
        mPanel.paintBackGroundColor
        drawPanelLiveData.value = mPanel
    }

    fun setDrawStyle(mStyle : Paint.Style){
        var mPanel: CustomPanel = drawPanelLiveData.value!!
        mPanel.drawStyle = mStyle
        drawPanelLiveData.value = mPanel
    }
    fun setDrawWidth(mWidth : Float){
        var mPanel: CustomPanel = drawPanelLiveData.value!!
//        mPanel.mpaint.strokeWidth = mWidth
        mPanel.drawWidth = mWidth
        drawPanelLiveData.value = mPanel
    }

    fun setPaintBackGroundColor(mColor: Int){
        var mPanel: CustomPanel = drawPanelLiveData.value!!
        mPanel.setBackgroundColor(mColor)
//        mPanel.vBitmapCanvas.drawColor(mColor)
        drawPanelLiveData.value = mPanel
    }
}
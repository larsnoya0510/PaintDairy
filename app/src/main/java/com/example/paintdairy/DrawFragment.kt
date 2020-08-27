package com.example.paintdairy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.paintdairy.customview.CustomPanel
import kotlinx.android.synthetic.main.fragment_draw.view.*

class DrawFragment : Fragment() {
    lateinit var drawFragmentRootView:View
    private var mCustomPanel: CustomPanel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        drawFragmentRootView= inflater.inflate(R.layout.fragment_draw, container, false)
        mCustomPanel = drawFragmentRootView.findViewById(R.id.mPanel)
        drawFragmentRootView.buttonSave.setOnClickListener {
//            mCustomPanel?.savePicture()
            mCustomPanel?.savePicture((activity as DateActivity).mDate)
        }
        drawFragmentRootView.buttonReset.setOnClickListener {
            mCustomPanel?.resetCanvas()
        }
        drawFragmentRootView.buttonClose.setOnClickListener {
            clickBackIcon()
        }
        return drawFragmentRootView
    }
    companion object {
        @JvmStatic
        fun newInstance() = DrawFragment()
    }
    private fun clickBackIcon() {
        if(this.parentFragmentManager!!.backStackEntryCount>0){
            this.parentFragmentManager!!.popBackStack()
        }else{
            this.activity!!.finish()
        }
    }
}

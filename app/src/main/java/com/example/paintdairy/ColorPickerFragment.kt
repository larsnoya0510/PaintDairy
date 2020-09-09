package com.example.paintdairy


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.paintdairy.customview.CustomPanel
import com.example.paintdairy.viewmodel.DrawPanelControlViewModel
import kotlinx.android.synthetic.main.fragment_color_picker.view.*

class ColorPickerFragment : Fragment() {
    lateinit var colorPickerFragmentRootView : View
    lateinit var mDrawPanelControlViewModel : DrawPanelControlViewModel
    lateinit var type : String
    var color :Int = Color.WHITE
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        type = arguments?.getString("Type")  ?: ""
        mDrawPanelControlViewModel = ViewModelProvider(activity as DateActivity).get(
            DrawPanelControlViewModel::class.java)
        colorPickerFragmentRootView= inflater.inflate(R.layout.fragment_color_picker, container, false)
        return colorPickerFragmentRootView
    }

    override fun onResume() {
        super.onResume()
        //init get color
        initGetColor()
        colorPickerFragmentRootView.seekBarA.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                checkColor()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        colorPickerFragmentRootView.seekBarR.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                checkColor()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        colorPickerFragmentRootView.seekBarG.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                checkColor()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
        colorPickerFragmentRootView.seekBarB.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                checkColor()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })
    }
    private fun initGetColor() {
        color = arguments?.getInt("Color") ?: Color.WHITE
        colorPickerFragmentRootView.seekBarA.progress = color.alpha
        colorPickerFragmentRootView.seekBarR.progress = color.red
        colorPickerFragmentRootView.seekBarG.progress = color.green
        colorPickerFragmentRootView.seekBarB.progress = color.blue
        checkColor()
    }

    private fun checkColor() {
        var colorHexA = String.format("%02x", colorPickerFragmentRootView.seekBarA.progress)
        var colorHexR = String.format("%02x", colorPickerFragmentRootView.seekBarR.progress)
        var colorHexG = String.format("%02x", colorPickerFragmentRootView.seekBarG.progress)
        var colorHexB = String.format("%02x", colorPickerFragmentRootView.seekBarB.progress)
        var colorHex = "#" + colorHexA + colorHexR + colorHexG + colorHexB

        val color = Color.parseColor(colorHex)
        colorPickerFragmentRootView.imageViewColorDemo.setBackgroundColor(color)
        colorPickerFragmentRootView.textViewColorAlphaProgress.text = colorPickerFragmentRootView.seekBarA.progress.toString()
        colorPickerFragmentRootView.textViewColorRedProgress.text =  colorPickerFragmentRootView.seekBarR.progress.toString()
        colorPickerFragmentRootView.textViewColorGreenProgress.text =  colorPickerFragmentRootView.seekBarG.progress.toString()
        colorPickerFragmentRootView.textViewColorBlueProgress.text = colorPickerFragmentRootView.seekBarB.progress.toString()

    }

    companion object {
        @JvmStatic
        fun newInstance() = ColorPickerFragment()
    }
    private fun clickBackIcon() {
        when(type){
            "DrawColor" ->{
                val ColorDrawable = colorPickerFragmentRootView.imageViewColorDemo.background as ColorDrawable?
                if(ColorDrawable != null) {
                    mDrawPanelControlViewModel.setDrawColor(ColorDrawable.color)
                }
            }
            "BackGroundColor" ->{
                val ColorDrawable = colorPickerFragmentRootView.imageViewColorDemo.background as ColorDrawable?
                if(ColorDrawable != null) {
                    mDrawPanelControlViewModel.setPaintBackGroundColor(ColorDrawable.color)
                }
            }
        }
//        if(this.parentFragmentManager!!.backStackEntryCount>0){
//            this.parentFragmentManager!!.popBackStack()
//        }else{
//            this.activity!!.finish()
//        }
    }

    override fun onDestroyView() {
        clickBackIcon()
        super.onDestroyView()
    }
}

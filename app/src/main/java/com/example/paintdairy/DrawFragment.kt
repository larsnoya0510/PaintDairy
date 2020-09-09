package com.example.paintdairy


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.paintdairy.customview.CustomPanel
import com.example.paintdairy.customview.CustomVerticalSeekBar
import com.example.paintdairy.viewmodel.DrawPanelControlViewModel
import kotlinx.android.synthetic.main.fragment_draw.view.*

class DrawFragment : Fragment() {
    lateinit var fragmentPool:FragmentPool
    lateinit var drawFragmentRootView:View
    private var mCustomPanel: CustomPanel? = null
    private lateinit var mSeekBarSetDrawWidth : CustomVerticalSeekBar
    lateinit var mDrawPanelControlViewModel : DrawPanelControlViewModel
    private lateinit var mDrawPanelControlObserve: Observer<CustomPanel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPool = (activity as DateActivity).fragmentPool
        drawFragmentRootView= inflater.inflate(R.layout.fragment_draw, container, false)
        mCustomPanel = drawFragmentRootView.findViewById(R.id.mPanel)
        mSeekBarSetDrawWidth = drawFragmentRootView.findViewById(R.id.seekBarSetDrawWidth)

        mSeekBarSetDrawWidth.setOnStateChangeListener(object : CustomVerticalSeekBar.OnStateChangeListener{
            override fun OnStateChangeListener(view: View?, progress: Float) {
                drawFragmentRootView.textViewDrawWidthProgress.text = progress.toInt().toString()
                mDrawPanelControlViewModel.setDrawWidth(progress)
            }
            override fun onStopTrackingTouch(view: View?, progress: Float) {}
        })
        drawFragmentRootView.floatingActionButtonDrawMenu.setOnClickListener {
            popupMenu(it)
        }
//        drawFragmentRootView.buttonSave.setOnClickListener {
////            mCustomPanel?.savePicture()
//            mCustomPanel?.savePicture((activity as DateActivity).mDate)
//        }
//        drawFragmentRootView.buttonReset.setOnClickListener {
//            mCustomPanel?.resetCanvas()
//        }
//        drawFragmentRootView.buttonClose.setOnClickListener {
//            clickBackIcon()
//        }
        //viewmodel
        mDrawPanelControlViewModel = ViewModelProvider(activity as DateActivity).get(DrawPanelControlViewModel::class.java)
        mDrawPanelControlViewModel.setDrawPanelData(mCustomPanel!!)
        mDrawPanelControlObserve = Observer {
            it.invalidate()
            drawFragmentRootView.viewDrawColorDemo.setBackgroundColor(it.drawColor)
            drawFragmentRootView.viewBackgroundColorDemo.setBackgroundColor(it.paintBackGroundColor)
            drawFragmentRootView.seekBarSetDrawWidth.progress = it.drawWidth
        }
        mDrawPanelControlViewModel.getDrawPanelData().observe(viewLifecycleOwner,mDrawPanelControlObserve)

        drawFragmentRootView.constraintLayoutDrawWidth.setOnClickListener {
           when( drawFragmentRootView.constrainLayoutDrawWidth.visibility){
               View.GONE ->{
                   drawFragmentRootView.constrainLayoutDrawWidth.visibility = View.VISIBLE
               }
               View.VISIBLE ->{
                   drawFragmentRootView.constrainLayoutDrawWidth.visibility = View.GONE
               }
           }
        }
        drawFragmentRootView.constraintLayoutDrawColor.setOnClickListener {
            var mFragment =fragmentPool.mColorPickerFragment
            mFragment.arguments?.clear()
            var mBundle = Bundle()
            mBundle.putString("Type","DrawColor")
//            mBundle.putInt("Color",mCustomPanel!!.drawColor!!)
            mBundle.putInt("Color",mDrawPanelControlViewModel.getDrawPanelData().value?.drawColor!!)

            mFragment.arguments = mBundle
            val transaction = this.parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.fragmentContainer,
                mFragment,
                "ColorPickerFragment"
            ).addToBackStack(null)
            transaction.commit()
        }
        drawFragmentRootView.constraintLayoutBackGroundColor.setOnClickListener {
            var mFragment =fragmentPool.mColorPickerFragment
            mFragment.arguments?.clear()
            var mBundle = Bundle()
            mBundle.putString("Type","BackGroundColor")
//            mBundle.putInt("Color",mCustomPanel!!.paintBackGroundColor!!)
            mBundle.putInt("Color",mDrawPanelControlViewModel.getDrawPanelData().value?.paintBackGroundColor!!)
            mFragment.arguments = mBundle
            val transaction = this.parentFragmentManager.beginTransaction()
            transaction.add(
                R.id.fragmentContainer,
                mFragment,
                "ColorPickerFragment"
            ).addToBackStack(null)
            transaction.commit()
        }
        //popup menu

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
    fun popupMenu(view:View){
        val popupMenu = PopupMenu(context!!, view)
        popupMenu.getMenuInflater().inflate(R.menu.draw_menu, popupMenu.getMenu())
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.drawMenuItemSave ->{
                    mCustomPanel?.savePicture((activity as DateActivity).mDate)
                }
                R.id.drawMenuItemClose ->{
                    clickBackIcon()
                }
                R.id.drawMenuItemReset ->{
                    mCustomPanel?.resetCanvas()
                }
            }
            false
        }
        popupMenu.setOnDismissListener {

        }
        popupMenu.show()
    }
}

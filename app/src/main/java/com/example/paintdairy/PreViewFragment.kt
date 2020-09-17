package com.example.paintdairy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_pre_view.view.*

class PreViewFragment : Fragment() {
    lateinit var preViewFragmentView: View
    var path : String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preViewFragmentView = inflater.inflate(R.layout.fragment_pre_view, container, false)
        path = arguments?.getString("Path")
        if(!path.isNullOrEmpty()){
            Glide.with(context!!)
                .load(context?.getExternalFilesDir(null)!!.toString() + "/${path}")
                .into(preViewFragmentView.imageViewPreViewForFragment)
        }
        preViewFragmentView.buttonPreviewClose.setOnClickListener {
            clickBackIcon()
        }
        return preViewFragmentView
    }

    companion object {
        @JvmStatic
        fun newInstance() = PreViewFragment()
    }

    private fun clickBackIcon() {
        if (this.parentFragmentManager!!.backStackEntryCount > 0) {
            this.parentFragmentManager!!.popBackStack()
        } else {
            this.activity!!.finish()
        }
    }

}

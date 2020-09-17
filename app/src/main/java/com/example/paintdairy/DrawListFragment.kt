package com.example.paintdairy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.paintdairy.adapter.RecyclerViewDrawListAdapter
import com.example.paintdairy.dataclass.Draws
import com.example.paintdairy.viewmodel.GetDrawsByDateViewModel
import kotlinx.android.synthetic.main.fragment_draw_list.view.*


class DrawListFragment : Fragment() {
    lateinit var SqlConnect : DBHelper
    lateinit var drawListFragmentRootView : View
    lateinit var fragmentPool:FragmentPool
    lateinit var mGetDrawsByDateViewModel : GetDrawsByDateViewModel
    private lateinit var mGetDrawsByDateObserve: Observer<MutableList<Draws>>
    lateinit var mRecyclerViewDrawListAdapter:RecyclerViewDrawListAdapter
    lateinit var selectList : MutableList<Draws>
    enum class DrawFragmentMode{
        View ,
        New
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        selectList = mutableListOf<Draws>()
        fragmentPool = (activity as DateActivity).fragmentPool
        drawListFragmentRootView= inflater.inflate(R.layout.fragment_draw_list, container, false)
        drawListFragmentRootView.floatingActionButtonAddDraw.setOnClickListener {
//            openDrawFragmentMode(DrawFragmentMode.New)
            val transaction = this.parentFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragmentContainer,
                fragmentPool.mDrawFragment,
                "DrawFragment"
            ).addToBackStack(null)
            transaction.commit()
        }
        drawListFragmentRootView.floatingActionButtonDeleteDraw.setOnClickListener {
            if(selectList.size >0){
                var deleteCmd : String = """ 
                     drawPath in (${selectList.map { it.drawPath }.joinToString("','", "'","'")})
                """.trimIndent()
                var result =SqlConnect.delete(deleteCmd)
                when{
                    result>= 1 ->{
                        Toast.makeText(this.context,"刪除成功",Toast.LENGTH_SHORT).show()
                    }
                    else ->{
                        Toast.makeText(this.context,"刪除失敗",Toast.LENGTH_SHORT).show()
                    }
                }
                selectList.clear()
            }
            mGetDrawsByDateViewModel.getDrawsByDate((activity as DateActivity).mDate)
        }
        drawListFragmentRootView.floatingActionButtonEditPreview.setOnClickListener {
//            openDrawFragmentMode(DrawFragmentMode.View)
            if(selectList.size ==1) {
                var mFragment = fragmentPool.mPreViewFragment
                var mBundle = Bundle()
                mBundle.putString("Path", selectList[0].drawPath)
                mFragment.arguments?.clear()
                mFragment.arguments = mBundle
                val transaction = this.parentFragmentManager.beginTransaction()
                transaction.replace(
                    R.id.fragmentContainer,
                    mFragment,
                    "PreViewFragment"
                ).addToBackStack(null)
                transaction.commit()
            }
        }
        mRecyclerViewDrawListAdapter = RecyclerViewDrawListAdapter(this.context!!)
        mRecyclerViewDrawListAdapter.setOnItemCheckListener(object : RecyclerViewDrawListAdapter.OnItemCheckListener{
            override fun onSelect(mDrawPath: String) {
                Glide.with(context!!)
                    .load(mDrawPath)
                    .into(drawListFragmentRootView.imageViewPreview)
            }

            override fun onCheck(mPosition: Int) {
                mRecyclerViewDrawListAdapter.mInList[mPosition].checked =  !mRecyclerViewDrawListAdapter.mInList[mPosition].checked
                var mDraws = mRecyclerViewDrawListAdapter.mInList[mPosition]
                if(mRecyclerViewDrawListAdapter.mInList[mPosition].checked==true){
                    selectList.add(mDraws)
                }
                else{
                    selectList.remove(mDraws)
                }
                drawListFragmentRootView.floatingActionButtonEditPreview.isEnabled = selectList.size < 2
                mRecyclerViewDrawListAdapter.Refresh()
            }
        })
        drawListFragmentRootView.recyclerViewDrawList.layoutManager = GridLayoutManager(this.context,3)
        drawListFragmentRootView.recyclerViewDrawList.adapter = mRecyclerViewDrawListAdapter

        SqlConnect=DBHelper.getInstance(this.context!!.applicationContext)
        mGetDrawsByDateViewModel = ViewModelProvider(this).get(GetDrawsByDateViewModel::class.java)
        mGetDrawsByDateViewModel.mDBHelper = SqlConnect
        mGetDrawsByDateObserve = Observer {
//            if(it.size>0){
                mRecyclerViewDrawListAdapter.UpdateData(it)
//            }
        }
        mGetDrawsByDateViewModel.getData().observe(viewLifecycleOwner,mGetDrawsByDateObserve)

        //==
//        Glide.with(context!!)
//            .load(this.context?.getExternalFilesDir(null)!!.toString() + "/202008251537.jpg")
//            .into(drawListFragmentRootView.imageViewPreview)
        //==

        return drawListFragmentRootView
    }

    private fun openDrawFragmentMode( mode : DrawFragmentMode) {
//        var mFragment = fragmentPool.mDrawFragment
//        mFragment.arguments?.clear()
//        var mBundle = Bundle()
//        when(mode){
//            DrawFragmentMode.New ->{
//                mBundle.putString("Mode", "New")
//            }
//            DrawFragmentMode.View ->{
//                mBundle.putString("Mode", "View")
//            }
//            else ->{
//                mBundle.putString("Mode", "")
//            }
//        }
//        mFragment.arguments = mBundle
//        val transaction = this.parentFragmentManager.beginTransaction()
//        transaction.replace(
//            R.id.fragmentContainer,
//            mFragment,
//            "DrawFragment"
//        ).addToBackStack(null)
//        transaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = DrawListFragment()
    }

    override fun onResume() {
        super.onResume()
        val mDate = (activity as DateActivity).mDate
        mGetDrawsByDateViewModel.getDrawsByDate(mDate)
    }

    override fun onDestroy() {
        mGetDrawsByDateViewModel.getData().removeObserver(mGetDrawsByDateObserve)
        super.onDestroy()
    }
}

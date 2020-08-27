package com.example.paintdairy


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var deleteList : MutableList<Draws>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        deleteList = mutableListOf<Draws>()
        fragmentPool = (activity as DateActivity).fragmentPool
        drawListFragmentRootView= inflater.inflate(R.layout.fragment_draw_list, container, false)
        drawListFragmentRootView.floatingActionButtonAddDraw.setOnClickListener {
            val transaction = this.parentFragmentManager.beginTransaction()
            transaction.replace(
                R.id.fragmentContainer,
                fragmentPool.mDrawFragment,
                "DrawFragment"
            ).addToBackStack("DrawFragment")
            transaction.commit()
        }
        drawListFragmentRootView.floatingActionButtonDeleteDraw.setOnClickListener {
            if(deleteList.size >0){
                var deleteCmd : String = """ 
                     drawPath in (${deleteList.map { it.drawPath }.joinToString("','", "'","'")})
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
                deleteList.clear()
            }
            mGetDrawsByDateViewModel.getDrawsByDate((activity as DateActivity).mDate)
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
                    deleteList.add(mDraws)
                }
                else{
                    deleteList.remove(mDraws)
                }
                mRecyclerViewDrawListAdapter.Refresh()
            }
        })
        drawListFragmentRootView.recyclerViewDrawList.layoutManager = LinearLayoutManager(this.context)
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
    companion object {
        @JvmStatic
        fun newInstance() = DrawListFragment()
    }

    override fun onResume() {
        super.onResume()
        val mDate = (activity as DateActivity).mDate
        mGetDrawsByDateViewModel.getDrawsByDate(mDate)
    }
}

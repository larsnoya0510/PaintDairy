package com.example.paintdairy

class FragmentPool {
    val mDrawListFragment :DrawListFragment by lazy { DrawListFragment.newInstance()}
    val mDrawFragment :DrawFragment by lazy { DrawFragment.newInstance()}
    val mColorPickerFragment :ColorPickerFragment by lazy { ColorPickerFragment.newInstance()}
    val mPreViewFragment :PreViewFragment by lazy { PreViewFragment.newInstance()}

}


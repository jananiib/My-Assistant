package com.bot.alvinbot.ui.dashBoard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter


/**
 * Created by Arul Kumar on 29-05-2019.
 */
class ViewPagerAdapter<VM : ViewDataBinding?, T>(
    context: Context?,
    private var items: List<T>,
    private val layoutId: Int,
    pagerItemCallBack: PagerItemCallBack<VM, T>
) : PagerAdapter() {

    private val pagerItemCallBack: PagerItemCallBack<VM, T>

    override fun getCount(): Int {
        return items.size
    }

    fun getAllItems(): List<T> {
        return items
    }

    fun notifie(allItems: List<T>, position: Int) {
        this.items = allItems;
        notifyDataSetChanged()
    }

    fun notifyData() {
        notifyDataSetChanged()
    }


    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {
        container.removeView(`object` as View)
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view =
            LayoutInflater.from(container.context).inflate(layoutId, container, false)
        DataBindingUtil.bind<VM>(view)
            ?.let {
                pagerItemCallBack.onBindItem(it, items[position], position) }
        container.addView(view)
        return view
    }

    override fun isViewFromObject(
        view: View,
        `object`: Any
    ): Boolean {
        return view === `object`
    }

    interface PagerItemCallBack<VM : ViewDataBinding?, T> {
        fun onBindItem(viewBinding: VM, data: T, position: Int)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
    init {
        this.pagerItemCallBack = pagerItemCallBack
    }
}
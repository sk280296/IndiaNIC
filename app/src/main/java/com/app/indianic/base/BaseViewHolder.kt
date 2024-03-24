package com.app.indianic.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding


abstract class BaseViewHolder(itemView: ViewBinding?) : RecyclerView.ViewHolder(
    itemView?.root!!
) {
    private var currentPosition: Int = 0

    protected abstract fun clear()

    open fun onBind(position: Int) {
        currentPosition = position
        clear()
    }

}
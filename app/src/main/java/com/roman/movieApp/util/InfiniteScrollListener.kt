package com.roman.movieApp.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InfiniteScrollListener(
    val func: () -> Unit,
    val layoutManager: LinearLayoutManager
) : RecyclerView.OnScrollListener() {

    var continueLoading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (totalItemCount < 4) {
            return  // workaround, func is called before the data is set and call listener method
        }
        if (visibleItemCount + firstVisibleItem >= totalItemCount) {
            func()
        }
    }
}
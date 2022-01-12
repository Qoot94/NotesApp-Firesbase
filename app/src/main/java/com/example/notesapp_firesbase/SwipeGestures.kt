package com.example.notesapp_firesbase

import android.content.Context
import android.graphics.Canvas
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


open abstract class SwipeGestures(context: Context) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    private val colorForDelete = ContextCompat.getColor(context, R.color.deletColor)
    val colorForEdit = ContextCompat.getColor(context, R.color.editColor)
    val iconDelete = R.drawable.ic_outline_remove_24
    val iconEdit = R.drawable.ic_sharp_edit_note_24


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        TODO("Not yet implemented")
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(colorForDelete)
            .addSwipeLeftActionIcon(iconDelete)
            .addSwipeRightBackgroundColor(colorForEdit)
            .addSwipeRightActionIcon(iconEdit)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
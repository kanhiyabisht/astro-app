package com.example.astrodashalib.view.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.astrodashalib.R
import com.example.astrodashalib.inflate
import android.graphics.PorterDuff
import android.support.v4.graphics.drawable.DrawableCompat
import android.os.Build


/**
 * Created by himanshu on 23/09/17.
 */
class ExpandableListAdapter(val context: Context, val listDataHeader: ArrayList<String>, val listChildData: HashMap<String, ArrayList<String>>) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any = listDataHeader[groupPosition]

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean = true

    override fun hasStableIds(): Boolean = false

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View? {

        val headerTitle = getGroup(groupPosition).toString()
        val view: View? = if (convertView == null) parent.inflate(R.layout.list_group) else convertView
        val lblListHeader = view?.findViewById(R.id.lblListHeader) as TextView
        lblListHeader.apply {
            text = headerTitle
            val imageResourceId = if (isExpanded) R.drawable.arrow_up else R.drawable.arrow_down
            val colorResourceId = if (isExpanded) R.color.colorPrimaryDark else R.color.black
            setCompoundDrawablesWithIntrinsicBounds(0, 0, imageResourceId, 0)
            setTextColor(ContextCompat.getColor(context, colorResourceId))

        }


        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        val child = listChildData.get(listDataHeader.get(groupPosition))
        return if (child != null) child.size else 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        try {
            return listChildData[listDataHeader[groupPosition]]!!.get(childPosition)
        } catch (e: Exception) {
            return ""
        }
    }

    override fun getGroupId(groupPosition: Int): Long = groupPosition.toLong()

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View? {

        val childText = getChild(groupPosition, childPosition) as String

        val view: View? = if (convertView == null) parent.inflate(R.layout.list_child) else convertView

        val txtListChild = view?.findViewById(R.id.lblListItem) as TextView
        txtListChild.apply {
            text = childText
            val drawable = ContextCompat.getDrawable(context, R.drawable.send_icon)
            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            val color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                DrawableCompat.setTint(drawable, color)
            else
                drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long = childPosition.toLong()

    override fun getGroupCount(): Int = listDataHeader.size


}
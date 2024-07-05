package com.gyasilarbi.jobdash

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MyExpandableListAdapter(
    private val context: Context,
    private val listDataHeader: List<String>,
    private val listDataChild: HashMap<String, List<String>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return listDataHeader.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listDataChild[listDataHeader[groupPosition]]?.size ?: 0
    }

    override fun getGroup(groupPosition: Int): Any {
        return listDataHeader[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return listDataChild[listDataHeader[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = inflater.inflate(R.layout.group_item, null)
        }
        val lblListHeader = convertView!!.findViewById<TextView>(R.id.lblListHeader)
        lblListHeader.text = headerTitle
        return convertView
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (groupPosition == 3 && childPosition == 0) { // This is the group with the logout button
            convertView = inflater.inflate(R.layout.child_item_button, null)
            val button = convertView!!.findViewById<Button>(R.id.btnLogout)
            button.setOnClickListener {
                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show()
                // Handle logout logic here
            }
        } else {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.child_item, null)
            }
            val childText = getChild(groupPosition, childPosition) as String
            val txtListChild = convertView!!.findViewById<TextView>(R.id.lblListItem)
            txtListChild.text = childText
        }

        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}

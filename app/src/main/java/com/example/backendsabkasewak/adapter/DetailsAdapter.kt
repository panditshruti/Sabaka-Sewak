package com.example.backendsabkasewak.adapter
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.backendsabkasewak.R
import com.example.backendsabkasewak.db.DetailsItem


class DetailsAdapter(
    private var detailsList: ArrayList<DetailsItem>, private var context: Context
) : RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val mobbile: TextView = itemView.findViewById(R.id.mobbile)
        val email: TextView = itemView.findViewById(R.id.email)
        val address: TextView = itemView.findViewById(R.id.address)
        val defence: TextView = itemView.findViewById(R.id.defence)

        val currentDate: TextView = itemView.findViewById(R.id.date)
        val image1: ImageView = itemView.findViewById(R.id.image1)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.same_details, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val details = detailsList[position]


            holder.name.text = details.name
            holder.mobbile.text = details.mobbile
            holder.email.text = details.emailId
            holder.address.text = details.address
            holder.currentDate.text = details.date
            holder.defence.text = details.examCategories.toString()


        Glide.with(context).load(details.image1).into(holder.image1)



    }

    override fun getItemCount(): Int {
        return detailsList.size
    }
}

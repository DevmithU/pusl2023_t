package com.example.pusl2023_t.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pusl2023_t.R
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.ui.activities.BorrowedBookDetailsActivity
import com.example.pusl2023_t.ui.fragments.BorrowedBooksFragment
import com.example.pusl2023_t.utils.Constants
import com.example.pusl2023_t.utils.GlideLoader
import kotlinx.android.synthetic.main.item_borrowed_list_layout.view.*


open class BorrowedListAdapter(
    private val context: Context,
    private var list: ArrayList<Book>,
    private val fragment: BorrowedBooksFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_borrowed_list_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadBookPicture(
                model.image,
                holder.itemView.iv_item_image
            )

            holder.itemView.tv_item_name.text = model.title
            //holder.itemView.tv_item_price.text = "$${model.price}"

            holder.itemView.setOnClickListener {
                val intent = Intent(context, BorrowedBookDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS, model)
                context.startActivity(intent)
            }
            holder.itemView.btn_return.setOnClickListener {
                fragment.updateRequestBook(model.book_id,"returning")

            }
        }
    }

    override fun getItemCount(): Int {

        return list.size

    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
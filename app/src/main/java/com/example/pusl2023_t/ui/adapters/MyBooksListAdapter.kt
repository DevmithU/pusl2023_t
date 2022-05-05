package com.example.pusl2023_t.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pusl2023_t.R
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.ui.activities.EditBookActivity
import com.example.pusl2023_t.ui.fragments.MyBooksFragment
import com.example.pusl2023_t.utils.Constants
import com.example.pusl2023_t.utils.GlideLoader
import kotlinx.android.synthetic.main.item_my_books_list_layout.view.*

open class MyBooksListAdapter(
    private val context: Context,
    private var list: ArrayList<Book>,
    private val fragment: MyBooksFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_my_books_list_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            GlideLoader(context).loadBookPicture(model.image, holder.itemView.iv_item_image)

            holder.itemView.tv_item_name.text = model.title
            holder.itemView.ib_delete_book.setOnClickListener {



                fragment.deleteBook(model.book_id)
            }

            holder.itemView.setOnClickListener {

                // Launch Book details screen.

                val intent = Intent(context, EditBookActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID, model.book_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, model.user_id)


                context.startActivity(intent)
            }


        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}
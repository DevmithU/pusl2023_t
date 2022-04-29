package com.example.pusl2023_t.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.ui.adapters.BorrowedListAdapter
import kotlinx.android.synthetic.main.fragment_borrowed_books.*


class BorrowedBooksFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_borrowed_books, container, false)
    }

    override fun onResume() {
        super.onResume()

        getBorrowedList()
    }

    private fun getBorrowedList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getBorrowedList(this@BorrowedBooksFragment)
    }

    fun successGetBorrowedList(soldBooksList: ArrayList<Book>) {

        // Hide Progress dialog.
        hideProgressDialog()


        if (soldBooksList.size > 0) {
            rv_sold_book_items.visibility = View.VISIBLE
            tv_no_sold_books_found.visibility = View.GONE

            rv_sold_book_items.layoutManager = LinearLayoutManager(activity)
            rv_sold_book_items.setHasFixedSize(true)

            val soldBooksListAdapter =
                BorrowedListAdapter(requireActivity(), soldBooksList,this@BorrowedBooksFragment)
            rv_sold_book_items.adapter = soldBooksListAdapter
        } else {
            rv_sold_book_items.visibility = View.GONE
            tv_no_sold_books_found.visibility = View.VISIBLE
        }
        // END
    }
    fun updateRequestBook(book_id: String, status: String) {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.
        //showAlertDialogToDeleteBook(bookID)
        FirestoreClass().updateRequestBook(this@BorrowedBooksFragment,book_id,status)

        //Toast.makeText(requireActivity(), "You can now delete the book. $bookID", Toast.LENGTH_SHORT).show()
    }
    fun updateRequestBookSuccess() {
        hideProgressDialog()

        getBorrowedList()

    }




}
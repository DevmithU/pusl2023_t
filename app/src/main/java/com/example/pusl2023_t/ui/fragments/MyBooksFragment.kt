package com.example.pusl2023_t.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.ui.activities.AddBookActivity
import com.example.pusl2023_t.ui.adapters.MyBooksListAdapter
import kotlinx.android.synthetic.main.fragment_my_books.*


class MyBooksFragment : BaseFragment() {

    //private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val mRootView = inflater.inflate(R.layout.fragment_my_books, container, false)

        //homeViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })
        return mRootView
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_book_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_book) {

            startActivity(Intent(activity, AddBookActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun successBooksListFromFireStore(booksList: ArrayList<Book>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (booksList.size > 0) {
            rv_my_book_items.visibility = View.VISIBLE
            tv_no_books_found.visibility = View.GONE

            rv_my_book_items.layoutManager = LinearLayoutManager(activity)
            rv_my_book_items.setHasFixedSize(true)

            // START
            val adapterBooks = MyBooksListAdapter(requireActivity(), booksList, this@MyBooksFragment)
            // END
            rv_my_book_items.adapter = adapterBooks


        } else {
            rv_my_book_items.visibility = View.GONE
            tv_no_books_found.visibility = View.VISIBLE
        }
        for (i in booksList){
            Log.i("booksList item Name",i.title)
            Log.i("booksList doc id",i.book_id)


        }
    }


    override fun onResume() {
        super.onResume()

        getBookListFromFireStore()
    }

    private fun getBookListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        //FirestoreClass().getBooksList(this@MyBooksFragment)
        FirestoreClass().getBooksList(this@MyBooksFragment)

    }

    fun deleteBook(bookID: String) {

        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.
        showAlertDialogToDeleteBook(bookID)

        //Toast.makeText(requireActivity(), "You can now delete the book. $bookID", Toast.LENGTH_SHORT).show()
    }

    fun bookDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.book_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest books list from cloud firestore.
        getBookListFromFireStore()
    }




    private fun showAlertDialogToDeleteBook(bookID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action - yes delete
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->


            showProgressDialog(resources.getString(R.string.please_wait))

            // Call the function of Firestore class.
            FirestoreClass().deleteBook(this@MyBooksFragment, bookID)
            // END

            dialogInterface.dismiss()
        }

        //performing negative action - no dont delete
        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        // Create the AlertDialog

        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}
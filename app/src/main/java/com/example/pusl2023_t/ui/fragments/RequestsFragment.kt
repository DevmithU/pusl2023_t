package com.example.pusl2023_t.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.ui.adapters.RequestListAdapter
import kotlinx.android.synthetic.main.fragment_requests.*

class RequestsFragment : BaseFragment() {

    //private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val root = inflater.inflate(R.layout.fragment_requests, container, false)
       // val textView: TextView = root.findViewById(R.id.text_notifications)
       // textView.text = "This is notifications Fragment"


        //notificationsViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })


        return root
    }

    override fun onResume() {
        super.onResume()

        getMyRequestsList()
    }

    private fun getMyRequestsList() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getMyRequestsList(this@RequestsFragment)
    }

    fun populateRequestsListInUI(ordersList: ArrayList<Book>) {

        // Hide the progress dialog.
        hideProgressDialog()


        if (ordersList.size > 0) {

            rv_my_order_items.visibility = View.VISIBLE
            tv_no_orders_found.visibility = View.GONE

            rv_my_order_items.layoutManager = LinearLayoutManager(activity)
            rv_my_order_items.setHasFixedSize(true)

            val myOrdersAdapter = RequestListAdapter(requireActivity(), ordersList,this@RequestsFragment)
            rv_my_order_items.adapter = myOrdersAdapter
        } else {
            rv_my_order_items.visibility = View.GONE
            tv_no_orders_found.visibility = View.VISIBLE
        }
        // END
    }
    fun updateRequestBook(book_id: String, status: String) {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.
        //showAlertDialogToDeleteBook(bookID)
        FirestoreClass().updateRequestBook(this@RequestsFragment,book_id,status)

        //Toast.makeText(requireActivity(), "You can now delete the book. $bookID", Toast.LENGTH_SHORT).show()
    }
    fun updateRequestBookSuccess() {
        hideProgressDialog()

        getMyRequestsList()

    }
}
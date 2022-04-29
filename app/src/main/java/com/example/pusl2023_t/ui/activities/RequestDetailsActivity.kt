package com.example.pusl2023_t.ui.activities

import android.os.Bundle
import android.view.View
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.utils.Constants
import com.example.pusl2023_t.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_request_details.*


class RequestDetailsActivity : BaseActivity() {
    var myRequestsDetails: Book = Book()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_details)
        if (intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)) {
            myRequestsDetails =
                intent.getParcelableExtra<Book>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }
        setupActionBar()

        setupUI()

    }
    private fun setupActionBar() {

        setSupportActionBar(toolbar_book_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_book_details_activity.setNavigationOnClickListener { onBackPressed() }
    }
    private fun setupUI(){
        GlideLoader(this@RequestDetailsActivity).loadBookPicture(myRequestsDetails.image, iv_book_detail_image)
        tv_book_details_title.text=myRequestsDetails.title
        tv_book_status.text=myRequestsDetails.status
        tv_address.text=myRequestsDetails.borrower_address
        tv_mobile_number.text=myRequestsDetails.borrower_phone

        if (myRequestsDetails.status=="requested"){
            btn_returned.visibility = View.GONE

            //tv_item_status_label.text="Status  :  "
            btn_accept_request.setOnClickListener {
                updateRequestBook(myRequestsDetails.book_id,"borrowed")

            }
            btn_decline_request.setOnClickListener {
                updateRequestBook(myRequestsDetails.book_id,"")

            }
        }

        if (myRequestsDetails.status=="returning"){
            //tv_item_status_label.text="Status  :  "

            btn_returned.setOnClickListener {
                updateRequestBook(myRequestsDetails.book_id,"")

            }
            btn_decline_request.visibility = View.GONE
            btn_accept_request.visibility = View.GONE


        }
        if (myRequestsDetails.status=="borrowed"){
            //tv_item_status_label.text="Status  :  "
            btn_returned.visibility = View.GONE
            btn_decline_request.visibility = View.GONE
            btn_accept_request.visibility = View.GONE


        }

    }
    private fun updateRequestBook(book_id: String, status: String) {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.
        //showAlertDialogToDeleteBook(bookID)
        FirestoreClass().updateRequestBook(this@RequestDetailsActivity,book_id,status)

        //Toast.makeText(requireActivity(), "You can now delete the book. $bookID", Toast.LENGTH_SHORT).show()
    }
    fun updateRequestBookSuccess() {


        hideProgressDialog()
        finish()

    }
}
package com.example.pusl2023_t.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.models.User
import com.example.pusl2023_t.utils.Constants
import com.example.pusl2023_t.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_book_details.*

class BookDetailsActivity : BaseActivity(), View.OnClickListener  {
    private lateinit var mBookDetails: Book
    private var mBookOwnerId: String = ""
    private lateinit var mUserDetails: User

    private var mBookId: String = ""
    private var mborrower_address: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        //set book id to variable from extra
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mBookId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("details Book Id", mBookId)
        }
        //var bookOwnerId: String = ""
        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)) {
            mBookOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }


        setupActionBar()

        if (FirestoreClass().getCurrentUserID() == mBookOwnerId) {
            //btn_add_to_cart.visibility = View.GONE
            //btn_go_to_cart.visibility = View.GONE

        } else {
            //btn_add_to_cart.visibility = View.VISIBLE
        }


        btn_add_to_cart.setOnClickListener(this)
        btn_go_to_cart.setOnClickListener(this)

        getUserDetails()


    }
    private fun getUserDetails() {

        // Show the progress dialog
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@BookDetailsActivity)
    }
    fun userDetailsSuccess(user: User) {
        mUserDetails = user

        getBookDetails()

    }

    private fun requestBook() {


        val bookHashMap = HashMap<String, Any>()


        showProgressDialog(resources.getString(R.string.please_wait))
        bookHashMap["borrower_id"] = FirestoreClass().getCurrentUserID()
        bookHashMap["status"] = "requested"
        bookHashMap["borrower_address"] = mUserDetails.address//parse as string long type of mobile
        bookHashMap["borrower_phone"] ="${mUserDetails.mobile}"




        FirestoreClass().requestBook(this@BookDetailsActivity,bookHashMap,mBookId)
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

    fun bookDetailsSuccess(book: Book) {

        mBookDetails = book



        // Populate the book details in the UI.
        GlideLoader(this@BookDetailsActivity).loadBookPicture(book.image, iv_book_detail_image)

        tv_book_details_title.text = book.title
        tv_book_details_description.text = book.description

        if(book.status=="requested"){

            // Hide Progress dialog.

            // Hide the AddToCart button if the item is already in the cart.

            if (book.borrower_id!=FirestoreClass().getCurrentUserID()){


            }
            if (book.borrower_id==FirestoreClass().getCurrentUserID()){
                btn_go_to_cart.visibility = View.VISIBLE

            }
            btn_add_to_cart.visibility = View.GONE

        }else{

            // There is no need to check the cart list if the book owner himself is seeing the book details.
            if (FirestoreClass().getCurrentUserID() == book.user_id) {
                //btn_add_to_cart.visibility = View.GONE
                //btn_go_to_cart.visibility = View.GONE
                // Hide Progress dialog.
            } else {
                btn_add_to_cart.visibility = View.VISIBLE


                //FirestoreClass().checkIfItemExistInCart(this@BookDetailsActivity, mBookId)
            }
        }

        hideProgressDialog()

    }


    fun requestBookSuccess() {
        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(this@BookDetailsActivity, resources.getString(R.string.success_message_item_added_to_cart), Toast.LENGTH_SHORT).show()

        btn_add_to_cart.visibility = View.GONE
        // Show the GoToCart button if the item is already in the cart. User can update the quantity from the cart list screen if he wants.
        btn_go_to_cart.visibility = View.VISIBLE
    }

    private fun getBookDetails() {


        // Show the book dialog
        //showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of FirestoreClass to get the book details.
        //FirestoreClass().getBookDetails(this@BookDetailsActivity, mBookId)
        FirestoreClass().getBookDetails(this@BookDetailsActivity, mBookId)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.btn_add_to_cart -> {
                    requestBook()
                    //addToCart()
                }

                R.id.btn_go_to_cart->{
                    startActivity(Intent(this@BookDetailsActivity, SentRequestListActivity::class.java))
                }
            }
        }
    }

}
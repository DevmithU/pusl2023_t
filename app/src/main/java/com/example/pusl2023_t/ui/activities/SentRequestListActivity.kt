package com.example.pusl2023_t.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Book
import com.example.pusl2023_t.ui.adapters.SentRequestListAdapter
import kotlinx.android.synthetic.main.activity_sent_request_list.*

class SentRequestListActivity : BaseActivity() {

    //private lateinit var mBooksList: ArrayList<Book>

    //private lateinit var mCartListItems: ArrayList<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sent_request_list)


        setupActionBar()

        /*
        btn_checkout.setOnClickListener {
            val intent = Intent(this@SentRequestListActivity, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }*/

    }

    override fun onResume() {
        super.onResume()

        //getCartItemsList()

        getSentRequestList()


    }
    fun removeRequestSuccess(){
        hideProgressDialog()

        getSentRequestList()


    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_cart_list_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_cart_list_activity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successSentRequestList(cartList: ArrayList<Book>) {

        // Hide progress dialog.

        hideProgressDialog()


        if (cartList.size > 0) {

            rv_cart_items_list.visibility = View.VISIBLE
            tv_no_cart_item_found.visibility = View.GONE

            rv_cart_items_list.layoutManager = LinearLayoutManager(this@SentRequestListActivity)
            rv_cart_items_list.setHasFixedSize(true)

            val cartListAdapter = SentRequestListAdapter(this@SentRequestListActivity, cartList)
            rv_cart_items_list.adapter = cartListAdapter



        } else {
            rv_cart_items_list.visibility = View.GONE
            tv_no_cart_item_found.visibility = View.VISIBLE
        }
    }

    private fun getSentRequestList() {
        showProgressDialog(resources.getString(R.string.please_wait))

        // Show the progress dialog.
       // showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getSentRequestList(this@SentRequestListActivity)
    }






    private fun getBookList() {

        // Show the progress dialog.
        //showProgressDialog(resources.getString(R.string.please_wait))

        //FirestoreClass().getAllBooksList(this@SentRequestListActivity)
    }

    fun itemRemovedSuccess() {

        //hideProgressDialog()

        //Toast.makeText(this@SentRequestListActivity, resources.getString(R.string.msg_item_removed_successfully), Toast.LENGTH_SHORT).show()

        //getCartItemsList()
    }

    fun itemUpdateSuccess() {

        //hideProgressDialog()

        //getCartItemsList()
    }



}
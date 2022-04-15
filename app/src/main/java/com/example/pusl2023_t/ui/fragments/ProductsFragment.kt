package com.example.pusl2023_t.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pusl2023_t.R
import com.example.pusl2023_t.firestore.FirestoreClass
import com.example.pusl2023_t.models.Product
import com.example.pusl2023_t.ui.activities.AddProductActivity
import com.example.pusl2023_t.ui.adapters.MyProductsListAdapter
import kotlinx.android.synthetic.main.fragment_products.*


class ProductsFragment : BaseFragment() {

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
        val mRootView = inflater.inflate(R.layout.fragment_products, container, false)

        //homeViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })
        return mRootView
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_product) {

            startActivity(Intent(activity, AddProductActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        // Hide Progress dialog.
        hideProgressDialog()

        if (productsList.size > 0) {
            rv_my_product_items.visibility = View.VISIBLE
            tv_no_products_found.visibility = View.GONE

            rv_my_product_items.layoutManager = LinearLayoutManager(activity)
            rv_my_product_items.setHasFixedSize(true)

            // START
            val adapterProducts = MyProductsListAdapter(requireActivity(), productsList, this@ProductsFragment)
            // END
            rv_my_product_items.adapter = adapterProducts


        } else {
            rv_my_product_items.visibility = View.GONE
            tv_no_products_found.visibility = View.VISIBLE
        }
        for (i in productsList){
            Log.i("productsList item Name",i.title)
            Log.i("productsList doc id",i.product_id)


        }
    }

    override fun onResume() {
        super.onResume()

        getProductListFromFireStore()
    }

    private fun getProductListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        FirestoreClass().getProductsList(this@ProductsFragment)
    }

    fun deleteProduct(productID: String) {

        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.
        showAlertDialogToDeleteProduct(productID)

        //Toast.makeText(requireActivity(), "You can now delete the product. $productID", Toast.LENGTH_SHORT).show()
    }

    fun productDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.product_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()

        // Get the latest products list from cloud firestore.
        getProductListFromFireStore()
    }

    private fun showAlertDialogToDeleteProduct(productID: String) {

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
            FirestoreClass().deleteProduct(this@ProductsFragment, productID)
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
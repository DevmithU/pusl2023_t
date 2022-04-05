package com.example.pusl2023


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pusl2023.model.Product
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val products = arrayListOf<Product>()

        for (i in 0..100) {
            products.add(Product("Organic Apple", "https://via.placeholder.com/350/FFFF00/ff0000", 1.99))


        }
        recycler_view.apply {
            layoutManager = GridLayoutManager(this@MainActivity,2)
            adapter = ProductsAdapter(products)
            /*
        *
        *
            recycler_view.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ProductsAdapter(products)
        }
        * */


        }


    }
}
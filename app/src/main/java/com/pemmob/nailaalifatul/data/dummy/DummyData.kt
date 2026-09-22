package com.pemmob.nailaalifatul.data.dummy

import android.hardware.display.DeviceProductInfo
import android.icu.util.ULocale
import com.pemmob.nailaalifatul.data.model.Category
import com.pemmob.nailaalifatul.data.model.Product
import java.util.Locale

object DummyData {
    val categories = listOf(
        Category(id  = 1, name = "Makanan", description = "Aneka Makanan Lokal",products_count = 5),
        Category(id  = 2, name = "Minuman", description = "Minuman Segar",products_count = 5),
        Category(id  = 3, name = "Kerajinan", description = "Kerajinan Tangan",products_count = 5)
    )

    val product = listOf(
        Product(id = 1, category_id = 1, category = categories[0], name = "Kripik Singkong", description = "Kripik Gurih", price = 15000.0, stock = 20, img = "dummy_product"),
        Product(id = 2, category_id = 1, category = categories[0], name = "Mendoan", description = "Mendoan Khas Banyumas", price = 20000.0, stock = 15, img = "dummy_product"),
        Product(id = 3, category_id = 1, category = categories[0], name = "Sale Pisang", description = "Sale Pisang Manis", price = 25000.0, stock = 25, img = "dummy_product"),
        Product(id = 4, category_id = 1, category = categories[0], name = "Getuk Goreng", description = "Getuk Lezat", price = 30000.0, stock = 10, img = "dummy_product"),
        Product(id = 5, category_id = 1, category = categories[0], name = "Nopia", description = "Nopia Khas Daerah", price = 18000.0, stock = 12, img = "dummy_product"),

        Product(id = 6, category_id = 2, category = categories[1], name = "Es Badeg", description = "Es Badeg Segar", price = 5000.0, stock = 30, img = "dummy_product"),
        Product(id = 7, category_id = 2, category = categories[1], name = "Kopi Kunyit", description = "Kopi Herbal", price = 12000.0, stock = 20, img = "dummy_product"),
        Product(id = 8, category_id = 2, category = categories[1], name = "Wedang Jahe", description = "Wedang Hangat", price = 8000.0, stock = 15, img = "dummy_product"),
        Product(id = 9, category_id = 2, category = categories[1], name = "Es Degan", description = "Es Degan Murni", price = 10000.0, stock = 25, img = "dummy_product"),
        Product(id = 10, category_id = 2, category = categories[1], name = "Sirup Pala", description = "Sirup Khas", price = 15000.0, stock = 18, img = "dummy_product"),

        Product(id = 11, category_id = 3, category = categories[2], name = "Batik Banyumasan", description = "Kain Batik", price = 150000.0, stock = 5, img = "dummy_product"),
        Product(id = 12, category_id = 3, category = categories[2], name = "Sapu Glagah", description = "Sapu Awet", price = 25000.0, stock = 40, img = "dummy_product"),
        Product(id = 13, category_id = 3, category = categories[2], name = "Tas Anyaman", description = "Tas Lontar", price = 45000.0, stock = 10, img = "dummy_product"),
        Product(id = 14, category_id = 3, category = categories[2], name = "Kerajinan Batok", description = "Hiasan Batok", price = 35000.0, stock = 8, img = "dummy_product"),
        Product(id = 15, category_id = 3, category = categories[2], name = "Ukiran Kayu", description = "Ukiran Halus", price = 200000.0, stock = 3, img = "dummy_product")
    )
}
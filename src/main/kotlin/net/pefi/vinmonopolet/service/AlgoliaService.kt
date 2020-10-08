package net.pefi.vinmonopolet.service

import com.algolia.search.ApacheAPIClientBuilder
import com.algolia.search.Index
import com.algolia.search.objects.Query
import net.pefi.vinmonopolet.model.Product
import org.springframework.stereotype.Service

@Service
class AlgoliaService {

    val appId = "*************";
    val apiKey = "*********************"

    val indexName = "your_index"
    val chunkSize = 10000
    
    private fun insertRecords(products: List<Product>) {
        println("Indexing ${products.size} products")
        val client = ApacheAPIClientBuilder(appId, apiKey).build()
        val index: Index<Product> = client.initIndex(indexName, Product::class.java)
        val chunked = products.chunked(chunkSize)
        chunked.forEach{
            index.addObjects(it)
        }
        println("Done indexing products")
    }

    fun updateRecords(products: List<Product>) {
        println("Indexing ${products.size} products")
        val client = ApacheAPIClientBuilder(appId, apiKey).build()
        val index: Index<Product> = client.initIndex(indexName, Product::class.java)
        val chunked = products.chunked(chunkSize)
        chunked.forEach{
            index.partialUpdateObjects(it, true)
        }
        println("Done indexing products")
    }

    private fun getIndexedProductCount(): Long {
        val client = ApacheAPIClientBuilder(appId, apiKey).build()
        val index: Index<Product> = client.initIndex(indexName, Product::class.java)
        val browse = index.browse(Query(""))
        return browse.stream().count()
    }
}

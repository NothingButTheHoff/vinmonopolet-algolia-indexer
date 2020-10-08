package net.pefi.vinmonopolet.service

import net.pefi.vinmonopolet.model.Headers.*
import net.pefi.vinmonopolet.model.Image
import net.pefi.vinmonopolet.model.Product
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import java.io.InputStreamReader
import java.math.BigDecimal
import java.net.URL
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime


@Service
class VinmonopoletService(val algoliaService: AlgoliaService){

    val productsFile : String = "https://www.vinmonopolet.no/medias/sys_master/products/products/hbc/hb0/8834253127710/produkter.csv"

    fun fetchProductsCsvFile() {
        val records = fetchCsv()

        val productList = mutableListOf<Product>()
        for (record in records) {
            val date = LocalDateTime.parse(record.get(Datotid))
            val id = record.get(Varenummer).toBigInteger()
            val name = record.get(Varenavn)
            val volume = record.get(Volum)
            val price = toBigDecimal(record.get(Pris))
            val pricePrLtr = toBigDecimal(record.get(Literpris))
            val type = record.get(Varetype)
            val selection = record.get(Produktutvalg)
            val storeCategory = record.get(Butikkategori)
            val body = record.get(Fylde).toInt()
            val freshness = record.get(Friskhet).toInt()
            val tannins = record.get(Garvestoffer).toInt()
            val bitterness = record.get(Bitterhet).toInt()
            val sweetness = record.get(Sodme).toInt()
            val color = record.get(Farge)
            val aroma = record.get(Lukt)
            val flavor = record.get(Smak)
            val foodParings = toStringListFoodPairings(record)
            val country = record.get(Land)
            val district = record.get(Distrikt)
            val subDistrict = record.get(Underdistrikt)
            val year = record.get(Argang)
            val material = record.get(Rastoff)
            val method = record.get(Metode)
            val alcohol = toDouble(record.get(Alkohol))
            val sugar = toDouble(record.get(Sukker))
            val acid = toDouble(record.get(Syre))
            val maturity = record.get(Lagringsgrad)
            val producer = record.get(Produsent)
            val wholesaleDealer = record.get(Grossist)
            val distributor = record.get(Distributor)
            val packagingType = record.get(Emballasjetype)
            val corkType = record.get(Korktype)
            val url = record.get(Vareurl)
            val images = toImageResources(record.get(Varenummer))
            val eco = record.get(Okologisk)!!.toBoolean()
            val bio = record.get(Biodynamisk)!!.toBoolean()
            val fairtrade = record.get(Fairtrade)!!.toBoolean()
            val ecoPackaging = record.get(Miljosmart_emballasje)!!.toBoolean()
            val lowGluten = record.get(Gluten_lav_pa)!!.toBoolean()
            val kosher = record.get(Kosher)!!.toBoolean()
            val mainGTIN = record.get(HovedGTIN)
            val otherGTINs = toStringListGTINs(record.get(AndreGTINs))

            val product = Product(date, id, name, volume, price, pricePrLtr, type, selection,
                    storeCategory, body, freshness, tannins, bitterness, sweetness, color,
                    aroma, flavor, foodParings, country, district, subDistrict, year,
                    material, method, alcohol, sugar, acid, maturity, producer,
                    wholesaleDealer, distributor, packagingType, corkType, url, images,
                    eco, bio, fairtrade, ecoPackaging, lowGluten, kosher, mainGTIN, otherGTINs )

            productList.add(product)

        }

        /*productList.forEach {
            println("${it.pricePrLtr} \t  ${it.otherGTINs.forEach {
                println(it)
            }} ")
        }*/
        algoliaService.updateRecords(productList)
    }

    fun fetchCsv() : Iterable<CSVRecord> {
        val url = URL(productsFile)
        val reader = InputStreamReader(url.openStream(), StandardCharsets.ISO_8859_1)
        return CSVFormat.RFC4180
                .withHeader()
                .withDelimiter(';')
                .parse(reader)
    }

    private fun toBigDecimal(value: String): BigDecimal {
        return value.replace(",", ".").toBigDecimal()
    }

    private fun toDouble(value: String): Double {
        return value
                .replace(",", ".")
                .replace("Ukjent", "-1")
                .replace("< ", "")
                .toDouble()
    }

    private fun toStringListFoodPairings(record: CSVRecord): List<String> {
        val val1 = record.get(Passertil01)
        val val2 = record.get(Passertil02)
        val val3 = record.get(Passertil03)

        val delimiter = ","
        var splitList = val1.split(delimiter)
        if (val2.isNotEmpty()) splitList = splitList.plus(val2.split(delimiter))
        if (val3.isNotEmpty()) splitList = splitList.plus(val3.split(delimiter))
        return splitList
    }

    private fun toStringListGTINs(gtins: String): List<String> {
        return gtins.split("§")
    }

    private fun toImageResources(productId: String): List<Image> {
        val baseUrl = "https://bilder.vinmonopolet.no/cache"
        val images = mutableListOf<Image>()
        images.add(Image(baseUrl.plus("/65x65-0/").plus(productId).plus("-1.jpg"),"cartIcon", mapOf("maxWidth" to 65).plus("maxHeight" to 65)))
        images.add(Image(baseUrl.plus("/96x96-0/").plus(productId).plus("-1.jpg"), "thumbnail", mapOf("maxWidth" to 96).plus("maxHeight" to 96)))
        images.add(Image(baseUrl.plus("/300x300-0/").plus(productId).plus("-1.jpg"), "product", mapOf("maxWidth" to 300).plus("maxHeight" to 300)))
        images.add(Image(baseUrl.plus("/515x515-0/").plus(productId).plus("-1.jpg"), "zoom", mapOf("maxWidth" to 515).plus("maxHeight" to 515)))
        return images
    }
}

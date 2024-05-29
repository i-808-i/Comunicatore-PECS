package com.example.provamaturita

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.GridView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class GridActivity : AppCompatActivity(), GridAdapter.OnItemClickListener {
    private var clickedImages = mutableListOf<Int>()
    private lateinit var boxLayout: LinearLayout
    private lateinit var sharedPreferences: SharedPreferences

    private val imageNamesMap = mapOf(
        R.drawable.andare to "Voglio andare",
        R.drawable.desiderare to "Voglio",
        R.drawable.nonvoglio to "Non voglio",
        R.drawable.bere to "Bere",
        R.drawable.mangiare to "Margiare",
        R.drawable.vestirsi to "Vestirmi",
        R.drawable.acqua to "Acqua",
        R.drawable.arancia to "Arancia",
        R.drawable.aranciata to "Aranciata",
        R.drawable.banana to "Banana",
        R.drawable.biscotti to "Biscotti",
        R.drawable.caramelle to "Caramelle",
        R.drawable.carne to "Carne",
        R.drawable.carota to "Carote",
        R.drawable.cereali to "Cereali",
        R.drawable.ciliegie to "Ciliegie",
        R.drawable.cioccolato to "Cioccolato",
        R.drawable.coca to "Coca Cola",
        R.drawable.croissant to "Merendina",
        R.drawable.formaggio to "Formaggio",
        R.drawable.fragola to "Fragola",
        R.drawable.gelato to "Gelato",
        R.drawable.hamburger to "Hamburger",
        R.drawable.latte to "Latte",
        R.drawable.maccheroni to "Pasta",
        R.drawable.mela to "Mela",
        R.drawable.melone to "Melone",
        R.drawable.muffin to "Muffin",
        R.drawable.pandoro to "Pandoro",
        R.drawable.pane to "Pane",
        R.drawable.panettone to "Panettone",
        R.drawable.panino to "Panino",
        R.drawable.patatine to "Patatine",
        R.drawable.pera to "Pera",
        R.drawable.pesca to "Pesca",
        R.drawable.pesce to "Pesce",
        R.drawable.patata to "Patata",
        R.drawable.pizza to "Pizza",
        R.drawable.pollo to "Pollo",
        R.drawable.pomodoro to "Pomodoro",
        R.drawable.prosciutto to "Prosciutto",
        R.drawable.salame to "Salame",
        R.drawable.succo to "Succo",
        R.drawable.torta to "Torta",
        R.drawable.uovo to "Uovo",
        R.drawable.yogurt to "Yogurt",
        R.drawable.zucchina to "Zucchina",
        R.drawable.bambola to "Bambola",
        R.drawable.bicchiere to "Bicchiere",
        R.drawable.cartaig to "Carta Igienica",
        R.drawable.colori to "Colori",
        R.drawable.cellulare to "Cellulare",
        R.drawable.computer to "Computer",
        R.drawable.cucchiaio to "Cucchiaio",
        R.drawable.dentifricio to "Dentifricio",
        R.drawable.foglio to "Foglio",
        R.drawable.forchetta to "Forchetta",
        R.drawable.libro to "Libro",
        R.drawable.matita to "Matita",
        R.drawable.mattoncini to "Costruzioni",
        R.drawable.palla to "Palla",
        R.drawable.pettine to "Pettine",
        R.drawable.piatto to "Piatto",
        R.drawable.puzzle to "Puzzle",
        R.drawable.sapone to "Sapone",
        R.drawable.seccpalet to "Secchiello e paletta",
        R.drawable.tablet to "Tablet",
        R.drawable.televisione to "TV",
        R.drawable.zaino to "Zaino",
        R.drawable.calzini to "Calzini",
        R.drawable.cappello to "Cappello",
        R.drawable.cappotto to "Cappotto",
        R.drawable.gonna to "Gonna",
        R.drawable.maglietta to "Maglietta",
        R.drawable.pantalone to "Pantalone",
        R.drawable.pigiama to "Pigiama",
        R.drawable.scarpe to "Scarpe",
        R.drawable.sciarpa to "Sciarpa",
        R.drawable.mutande to "Mutande",
        R.drawable.bagno to "Bagno",
        R.drawable.camera to "Camera",
        R.drawable.casa to "Casa",
        R.drawable.cinema to "Cinema",
        R.drawable.parco to "Parco",
        R.drawable.spiaggia to "Spiaggia",
        R.drawable.cucina to "Cucina",
        R.drawable.salone to "Salone",
        R.drawable.montagna to "Montagna",
        R.drawable.bambina to "Bambina",
        R.drawable.bambino to "Bambino",
        R.drawable.famiglia to "Famiglia",
        R.drawable.mamma to "Mamma",
        R.drawable.padre to "Papà",
        R.drawable.nonna to "Nonna",
        R.drawable.nonno to "Nonno",
        R.drawable.fratello to "Fratello",
        R.drawable.sorella to "Sorella"
    )

    private val clickedImageNames = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        val sharedLayout = findViewById<View>(R.id.sharedLayout)
        val button1 = sharedLayout.findViewById<Button>(R.id.deleteall)
        val button2 = sharedLayout.findViewById<Button>(R.id.playaudio)

        button1.visibility = View.GONE
        button2.visibility = View.GONE

        boxLayout = findViewById(R.id.boxLayout)
        sharedPreferences = getSharedPreferences("ClickedImages", Context.MODE_PRIVATE)

        val imageList = intent.getIntegerArrayListExtra("imageList") ?: ArrayList()
        val textList = intent.getStringArrayListExtra("textList") ?: ArrayList()

        val recyclerView = findViewById<RecyclerView>(R.id.gridRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val adapter = GridAdapter(imageList, textList, this)
        recyclerView.adapter = adapter

        val savedImages = getClickedImages()
        savedImages.forEach { imageId ->
            addImageToBox(imageId)
        }

        val backgroundColor = intent.getStringExtra("backgroundColor")
        findViewById<RelativeLayout>(R.id.rootgrid).setBackgroundColor(Color.parseColor(backgroundColor))

        val backButton = findViewById<Button>(R.id.backButton)

        // Set an OnClickListener for the back button
        backButton.setOnClickListener {

            clickedImages = getClickedImages().toMutableList()
            saveClickedImages(clickedImages)
            val clickedNames = getClickedImageName(clickedImageNames).toMutableList()
            saveClickedImageNames(clickedNames)
            val resultIntent = Intent()
            resultIntent.putIntegerArrayListExtra("ClickedImages", ArrayList(clickedImages)) // Use "selectedImages" key
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }

    }

    override fun onItemClick(imageId: Int) {
        clickedImages = getClickedImages().toMutableList()
        clickedImages.add(imageId)
        saveClickedImages(clickedImages)

        val imageName = imageNamesMap[imageId]
        imageName?.let {
            clickedImageNames.add(it)
        }
        saveClickedImageNames(clickedImageNames)

        // Creare un nuovo ImageView
        val imageView = ImageView(this).apply {
            // Impostare l'immagine
            setImageResource(imageId)

            // Calcolare le dimensioni desiderate per l'immagine
            val desiredWidth = resources.getDimensionPixelSize(R.dimen.desired_image_width)
            val desiredHeight = resources.getDimensionPixelSize(R.dimen.desired_image_height)

            // Scalare l'immagine alle dimensioni desiderate
            val drawable = this.drawable
            val scaledDrawable = BitmapDrawable(resources, Bitmap.createScaledBitmap((drawable as BitmapDrawable).bitmap, desiredWidth, desiredHeight, true))

            // Impostare l'immagine scalata
            setImageDrawable(scaledDrawable)

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
            }

            // Impostare l'azione onClick per rimuovere l'immagine
            setOnClickListener {
                boxLayout.removeView(this)
                clickedImages.remove(imageId)
                clickedImageNames.remove(imageNamesMap[imageId])
                saveClickedImages(clickedImages)
                saveClickedImageNames(clickedImageNames)

            }
        }

        // Aggiungere l'ImageView al layout della box
        boxLayout.addView(imageView)
        boxLayout.requestLayout()



    }





    private fun saveClickedImages(clickedImages: List<Int>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(clickedImages)
        editor.putString("imageList", json)
        editor.apply()
    }

    private fun saveClickedImageNames(imageNames: List<String>) {
        val sharedPref = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val json = Gson().toJson(imageNames)
        editor.putString("clickedImageNames", json)
        editor.apply()
    }

    private fun getClickedImages(): List<Int> {
        val json = sharedPreferences.getString("imageList", null)
        return Gson().fromJson(json, object : TypeToken<List<Int>>() {}.type) ?: emptyList()
    }

    private fun getClickedImageName(clickedImageNames: List<String>): List<String>{
        return clickedImageNames
    }


    private fun addImageToBox(imageId: Int) {
        val imageView = ImageView(this).apply {
            setImageResource(imageId)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        boxLayout.addView(imageView)
        boxLayout.requestLayout()
    }

    override fun onDestroy() {
        super.onDestroy()
        clearStoredImages()
    }

    private fun clearStoredImages() {
        sharedPreferences.edit().clear().apply()
    }
}
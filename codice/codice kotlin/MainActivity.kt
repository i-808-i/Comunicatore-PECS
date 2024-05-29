package com.example.provamaturita

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.provamaturita.ui.theme.ProvaMaturitaTheme
import java.util.concurrent.Executors
import com.example.provamaturita.SharedPreferencesHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*


class MainActivity : ComponentActivity(), CustomAdapter.OnItemClickListener, TextToSpeech.OnInitListener {


    private lateinit var boxLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView
    private val colors = arrayOf("#d2b491", "#e95b55", "#b485f8", "#94cafa", "#83c972", "#f5cecc")
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var tts: TextToSpeech
    private var imagesnames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        boxLayout = findViewById(R.id.boxLayout)
        sharedPreferences = getSharedPreferences("ClickedImages", Context.MODE_PRIVATE)

        clearStoredImages()

        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val listofPECS = ArrayList<PECS>()

        listofPECS.add(
            PECS(
                R.drawable.azioni,
                "Azioni",
                listOf(R.drawable.andare, R.drawable.desiderare, R.drawable.nonvoglio, R.drawable.bere, R.drawable.mangiare, R.drawable.vestirsi),
                listOf("Voglio andare", "Voglio", "Non voglio", "Bere", "Mangiare", "Vestirsi")
            )
        )

        /*audio/scritta -> andare = voglio andare
        */


        listofPECS.add(
            PECS(
                R.drawable.cibo,
                "Cibo",
                listOf(R.drawable.acqua, R.drawable.arancia,R.drawable.aranciata, R.drawable.banana, R.drawable.biscotti,
                    R.drawable.caramelle, R.drawable.carne, R.drawable.carota, R.drawable.cereali, R.drawable.ciliegie, R.drawable.cioccolato,
                    R.drawable.coca, R.drawable.croissant, R.drawable.formaggio, R.drawable.fragola, R.drawable.gelato, R.drawable.hamburger,
                    R.drawable.latte, R.drawable.maccheroni, R.drawable.mela, R.drawable.melone, R.drawable.muffin, R.drawable.pandoro, R.drawable.pane,
                    R.drawable.panettone, R.drawable.panino, R.drawable.patatine, R.drawable.pera, R.drawable.pesca, R.drawable.pesce, R.drawable.patata, R.drawable.pizza,
                    R.drawable.pollo, R.drawable.pomodoro, R.drawable.prosciutto, R.drawable.salame, R.drawable.succo, R.drawable.torta, R.drawable.uovo,
                    R.drawable.yogurt, R.drawable.zucchina),
                listOf("Acqua", "Arancia", "Aranciata", "Banana", "Biscotti", "Caramelle", "Carne", "Carota", "Cereali", "Ciliegie", "Cioccolato",
                    "Coca Cola", "Merendina", "Formaggio", "Fragola", "Gelato", "Hamburger", "Latte", "Pasta", "Mela", "Melone", "Muffin", "Pandoro",
                    "Pane", "Panettone", "Panino", "Patatine", "Pera", "Pesca", "Pesce", "Patata", "Pizza", "Pollo", "Pomodoro", "Prosciutto",
                    "Salame", "Succo", "Torta", "Uovo", "Yogurt", "Zucchina")
            )
        )

        listofPECS.add(
            PECS(
                R.drawable.oggetti,
                "Oggetti",
                listOf(R.drawable.bambola, R.drawable.bicchiere, R.drawable.cartaig, R.drawable.colori, R.drawable.cellulare, R.drawable.computer, R.drawable.cucchiaio,
                    R.drawable.dentifricio, R.drawable.foglio, R.drawable.forchetta, R.drawable.libro, R.drawable.matita, R.drawable.mattoncini,
                    R.drawable.palla, R.drawable.pettine, R.drawable.piatto, R.drawable.puzzle, R.drawable.sapone, R.drawable.seccpalet,
                    R.drawable.tablet, R.drawable.televisione, R.drawable.zaino),
                listOf("Bambola", "Bicchiere", "Carta Igienica", "Colori", "Cellulare", "Computer", "Cucchiaio", "Dentifricio", "Foglio", "Forchetta",
                    "Libro", "Matita", "Costruzioni", "Palla", "Pettine", "Piatto", "Puzzle", "Sapone", "Secchiello Paletta", "Tablet", "TV", "Zaino")
            )
        )

        listofPECS.add(
            PECS(
                R.drawable.vestiti,
                "Vestiti",
                listOf(R.drawable.calzini, R.drawable.cappello, R.drawable.cappotto, R.drawable.gonna, R.drawable.maglietta, R.drawable.pantalone,
                    R.drawable.pigiama, R.drawable.scarpe, R.drawable.sciarpa, R.drawable.mutande),
                listOf("Calzini", "Cappello", "Cappotto", "Gonna", "Maglietta", "Pantalone", "Pigiama", "Scarpe", "Sciarpa", "Mutande")

            )
        )

        listofPECS.add(
            PECS(
                R.drawable.luoghi,
                "Luoghi",
                listOf(R.drawable.bagno, R.drawable.camera, R.drawable.casa, R.drawable.cinema, R.drawable.parco, R.drawable.spiaggia,
                    R.drawable.cucina, R.drawable.salone, R.drawable.montagna),
                listOf("Bagno", "Camera", "Casa", "Cinema", "Parco", "Spiaggia", "Cucina", "Salone", "Montagna")
            )
        )

        listofPECS.add(
            PECS(
                R.drawable.persone,
                "Persone",
                listOf(R.drawable.bambina, R.drawable.bambino, R.drawable.famiglia, R.drawable.mamma, R.drawable.padre, R.drawable.nonna, R.drawable.nonno,
                    R.drawable.fratello, R.drawable.sorella),
                listOf("Bambina", "Bambino", "Famiglia", "Mamma", "Papà", "Nonna", "Nonno", "Fratello", "Sorella")
            )
        )

        val customAdapter = CustomAdapter(listofPECS, colors)
        customAdapter.setOnItemClickListener(this)
        recyclerView.adapter = customAdapter

        val savedImages = getSavedImages()
        savedImages.forEach { imageResource ->
            addImageToBox(imageResource)
        }

        val speakButton: Button = findViewById(R.id.playaudio)
        speakButton.setOnClickListener {
            speakClickedImageNames()
        }

        val deleteButton: Button = findViewById(R.id.deleteall)
        deleteButton.setOnClickListener{
            clearStoredImages()
            imagesnames.clear()
        }

    }

    override fun OnItemClick(position: Int, item: PECS) {
        // Start a new activity (or fragment) to display grid of images
        val intent = Intent(this, GridActivity::class.java)
        val backgroundColor = colors[position % colors.size]
        intent.putIntegerArrayListExtra("imageList", ArrayList(item.imageList))
        intent.putStringArrayListExtra("textList", ArrayList(item.text))
        intent.putExtra("backgroundColor", backgroundColor)
        gridActivityLauncher.launch(intent)
    }



    private val gridActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImages = data?.getIntegerArrayListExtra("ClickedImages")
            selectedImages?.forEach { imageResource ->
                addImageToBox(imageResource)
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language for Text-to-Speech engine
            val result = tts.setLanguage(Locale.ITALIAN)// Check if the language is supported
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed!")
        }
    }

    private fun speakClickedImageNames() {
        if (imagesnames.isNotEmpty()) {
            // Convert the list of clicked image names to a single string
            val textToSpeak = imagesnames.joinToString(" ")
            // Speak the text
            tts.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "")
        } else {
            // If the list is empty, inform the user
            Toast.makeText(this, "No images clicked yet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getClickedImageNames(): List<String> {
        val sharedPref = getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        val json = sharedPref.getString("clickedImageNames", null)
        return Gson().fromJson(json, object : TypeToken<List<String>>() {}.type) ?: emptyList()
    }

    private fun getSavedImages(): List<Int> {
        val json = sharedPreferences.getString("imageList", null)
        return Gson().fromJson(json, object : TypeToken<List<Int>>() {}.type) ?: emptyList()
    }

    private fun addImageToBox(imageResource: Int) {
        val imageView = ImageView(this).apply {
            setImageResource(imageResource)
            val clickedImageNames = getClickedImageNames()
            imagesnames.addAll(clickedImageNames.filter { !imagesnames.contains(it) })
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
        }

        boxLayout.addView(imageView)
        boxLayout.requestLayout()
    }


    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
        clearStoredImages()
    }

    private fun clearStoredImages() {
        sharedPreferences.edit().clear().apply()
        boxLayout.removeAllViews()
    }

}
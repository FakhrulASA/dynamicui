package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.itemmodel.FormModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonParser

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter : ListShowAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val json = readRawJson()

//        binding.buttonView.setOnClickListener {
//            //Generate form
//            if (json.isNotEmpty()) {
//                val intent = Intent(this, GenerateFormActivity::class.java)
//                intent.putExtra("value", json)
//                startActivity(intent)
//            }
//        }

        adapter.itemActionListener={
            val gson = Gson()
            val json = gson.toJson(it)
            val intent = Intent(this, GenerateFormActivity::class.java)
            intent.putExtra("value", json)
            startActivity(intent)
        }
    }

    var formComponent: FormModel? = null

    fun readRawJson(): String {
        val text = resources.openRawResource(R.raw.sample_json)
            .bufferedReader().use { it.readText() }
        formComponent = Gson().fromJson(text, FormModel::class.java)
        val check = checkValidity(text)
        if (check) {
            adapter = ListShowAdapter(this)
            binding.rec.adapter = adapter
            adapter.submitListData(formComponent!!.data.results)
            binding.etContent.setText(text)

        } else {
            Snackbar.make(binding.content, "Invalid JSON", Snackbar.LENGTH_SHORT).show()
            return ""
        }
        return text
    }

    private fun checkValidity(string: String): Boolean {
        val gson = Gson()
        try {
            gson.fromJson(string, Object::class.java)
        } catch (e: Exception) {
            return false
        }
        return true
    }
}
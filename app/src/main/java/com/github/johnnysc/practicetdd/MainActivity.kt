package com.github.johnnysc.practicetdd

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val source = "Here is my **RED** text!\n and **this one also red** by the way."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textView)
        val parser: MarkDown.Parser = MarkDown.Parser.Base("#FF0000", "**")
        val resultItem: MarkDown.ResultItem = parser.parse(source)
        val text: CharSequence = resultItem.formattedText()
        textView.text = text
    }
}
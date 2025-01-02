package com.pam.Dictionary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pam.Dictionary.feature_dictionary.domain.model.WordInfo
import com.pam.Dictionary.ui.theme.DictionaryAppTheme

class WordInfoActivity :  ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Intent = getIntent()
        val WordItem  = Intent.getParcelableExtra<WordInfo?>("word")
        setContent {
            DictionaryAppTheme {

                WordInfoView(WordItem!!)
            }
        }
    }

    @Composable
    fun WordInfoView(WordItem: WordInfo) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start // Align items to the start
            ) {
                Text(
                    text = WordItem.word,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f)) // Flexible space between Text and Button


            }
            if(WordItem.phonetic != null){
                Text(text = WordItem.phonetic!!, fontWeight = FontWeight.Light)
            }

            else{
                Text(text = "None", fontWeight = FontWeight.Light)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = WordItem.origin)

            WordItem.meanings.forEach { meaning ->
                Text(text = meaning.partOfSpeech, fontWeight = FontWeight.Bold)
                meaning.definitions.forEachIndexed { i, definition ->
                    Text(text = "${i + 1}. ${definition.definition}")
                    Spacer(modifier = Modifier.height(8.dp))
                    definition.example?.let { example ->
                        Text(text = "Example: $example")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
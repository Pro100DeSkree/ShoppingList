package com.deskree.shoppinglist.main_activity.routes.note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deskree.shoppinglist.database.entities.NoteItem
import com.deskree.shoppinglist.utils.convertHtmlToAnnotatedString
import com.deskree.shoppinglist.utils.isHtmlString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    note: NoteItem,
    onClickDelete: (NoteItem) -> Unit,
    onClickItem: (NoteItem) -> Unit,

    ) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
        ),
        onClick = { onClickItem.invoke(note) }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val description = if (isHtmlString(note.content))
                    convertHtmlToAnnotatedString(note.content).text
                else
                    note.content
                Text(
                    text = note.title,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.padding(vertical = 2.dp))
                Text(
                    text = description,
                    maxLines = 5
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondary)
                        .height(2.dp)
                )
                Text(
                    text = note.dataTime,
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            IconButton(
                onClick = { onClickDelete(note) },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Icon button delete"
                )
            }
        }
    }
}
package com.deskree.shoppinglist.list_activity.list_screen

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deskree.shoppinglist.database.entities.ShopListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(
    item: ShopListItem,
    onClickDelete: (ShopListItem) -> Unit,
    onClickEdit: (ShopListItem) -> Unit,
    onClickCheckBox: (ShopListItem) -> Unit
) {
    val checkBox = remember { mutableStateOf(false) }
    checkBox.value = item.itemChecked

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
        ),
        onClick = {
            checkBox.value = !checkBox.value
            onClickCheckBox(item.copy(itemChecked = checkBox.value))
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 6.dp)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {
                Text(
                    text = item.name,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textDecoration = if (checkBox.value) TextDecoration.LineThrough else TextDecoration.None
                    ),
                    color = if (checkBox.value) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                        alpha = 0.7f
                    )
                    else MaterialTheme.colorScheme.onSurface
                )
                if (item.itemInfo.isNotEmpty()) {
                    Spacer(Modifier.padding(vertical = 2.dp))
                    Text(
                        text = item.itemInfo,
                        style = TextStyle(
                            textDecoration = if (checkBox.value) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = if (checkBox.value) MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.7f
                        )
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            Row {
                IconButton(
                    onClick = {
                        if (checkBox.value)
                            onClickDelete.invoke(item)
                        else
                            onClickEdit.invoke(item)
                    }
                ) {
                    if (checkBox.value)
                        Icon(
                            imageVector = Icons.Default.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = "Delete",
                        )
                    else
                        Icon(
                            imageVector = Icons.Default.Edit,
                            tint = MaterialTheme.colorScheme.secondary,
                            contentDescription = "Edit",
                        )
                }

                Checkbox(
                    checked = checkBox.value,
                    onCheckedChange = { checked ->
                        checkBox.value = checked
                        onClickCheckBox(item.copy(itemChecked = checked))
                    }
                )
            }
        }
    }
}
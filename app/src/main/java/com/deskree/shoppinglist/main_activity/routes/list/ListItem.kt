package com.deskree.shoppinglist.main_activity.routes.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deskree.shoppinglist.R
import com.deskree.shoppinglist.database.entities.ShopListNameItem


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItem(
    listItem: ShopListNameItem,
    onClickItem: (ShopListNameItem) -> Unit,
    onClickEdit: (ShopListNameItem) -> Unit,
    onClickDelete: (ShopListNameItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
        ),
        onClick = { onClickItem(listItem) }
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = listItem.name,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontSize = 20.sp)
                    )

                    Text(
                        text = listItem.time,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                // TODO: Закоментований лічильник прогресс бара
                Text(
                    text = "${listItem.totalTasks}/${listItem.tasksCompleted}",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Row {
                    IconButton(
                        onClick = { onClickEdit(listItem) },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
                            tint = MaterialTheme.colorScheme.secondary,
                            contentDescription = "Icon button edit"
                        )
                    }

                    IconButton(
                        onClick = { onClickDelete(listItem) },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = "Icon button delete"
                        )
                    }
                }
            }
            // TODO: Закоментований прогресс бар
            TaskProgressBar(listItem.tasksCompleted, listItem.totalTasks)
        }
    }
}


@Composable
fun TaskProgressBar(tasksCompleted: Int, totalTasks: Int) {
    if (totalTasks != 0) {
        val progress = tasksCompleted.toFloat() / totalTasks
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
        )
    }
}
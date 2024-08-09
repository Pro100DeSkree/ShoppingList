package com.deskree.shoppinglist.main_activity.routes.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwitchPlate(
    title: String,
    signatureOn: String,
    signatureOff: String,
    isDescription: Boolean,
    description: String,
    isChecked: MutableState<Boolean>,
    onChange: (Boolean) -> Unit
) {

    Column {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp),
            text = title,
            style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isChecked.value) signatureOn else signatureOff,
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    )
                    if (isDescription) {
                        Spacer(modifier = Modifier.padding(bottom = 2.dp))
                        Text(
                            text = description,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Thin),
                            textAlign = TextAlign.Start
                        )
                    }
                }

                Switch(
                    checked = isChecked.value, onCheckedChange = {
                        isChecked.value = it
                        onChange.invoke(it)
                    }, colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                        checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                )
            }
        }
        Spacer(modifier = Modifier.padding(2.dp))
    }
}
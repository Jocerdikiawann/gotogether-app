package com.example.livetracking.ui.component.textfield

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livetracking.R
import com.example.livetracking.ui.theme.Secondary
import com.example.livetracking.utils.from

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldSearch(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    enable: Boolean,
    readOnly: Boolean,
    placeHolder: String,
    value: String,
    context: Context
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        placeholder = {
            Text(
                text = placeHolder,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp.from(context),
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search),
                contentDescription = "ic_search",
                tint = Color.Gray,
                modifier = modifier
                    .width(40.dp.from(context))
                    .height(40.dp.from(context)),
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp.from(context)),
        shape = RoundedCornerShape(20.dp.from(context)),
        readOnly = readOnly,
        enabled = enable,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Gray,
            focusedBorderColor = Secondary
        )
    )
}
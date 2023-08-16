package com.example.bachelorthesisapp.presentation.ui.components.common

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.bachelorthesisapp.presentation.ui.theme.Ochre
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.google.android.material.color.MaterialColors

@Composable
fun <T> DropdownWithCheckboxesMenu(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    notSetLabel: String? = null,
    items: List<T>,
    selectedIndex: Int = -1,
    selectedIndexes: List<Int>,
    onItemSelected: (index: Int, item: T) -> Unit,
    onItemDeselected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (T, Boolean, Boolean, () -> Unit, () -> Unit) -> Unit = { item, selected, itemEnabled, onChecked, onUnchecked ->
        DropdownWithCheckboxesMenuItem(
            text = item.toString(),
            selected = selected,
            enabled = itemEnabled,
            onChecked = onChecked,
            onUnchecked = onUnchecked
        )
    },
    painterResource: Int
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.height(IntrinsicSize.Min)) {
        TextField(
            label = { Text(label, color = Color.DarkGray, style = Typography.caption) },
            value = items.getOrNull(selectedIndex)?.let { selectedItemToString(it) } ?: "",
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = painterResource),
                    contentDescription = ""
                )
            },
            trailingIcon = {
                Icon(Icons.Filled.ArrowDropDown, "")
            },
            onValueChange = {
                Log.d("TYPE_SELECTED", it)

            },
            readOnly = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Transparent,
                focusedIndicatorColor = Ochre,
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.Gray
            ),
        )
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable(enabled = enabled) { expanded = true },
            color = Color.Transparent,
        ) {}
    }

    if (expanded) {
        Dialog(
            onDismissRequest = { expanded = false },
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
            ) {
                val listState = rememberLazyListState()
                if (selectedIndex > -1) {
                    LaunchedEffect("ScrollToSelected") {
                        listState.scrollToItem(index = selectedIndex)
                    }
                }

                LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                    if (notSetLabel != null) {
                        item {
                            DropdownWithCheckboxesMenuItem(
                                text = notSetLabel,
                                selected = false,
                                enabled = false,
                                onChecked = { },
                                onUnchecked = { },
                            )
                        }
                    }
                    itemsIndexed(items) { index, item ->
                        val selectedItem = index in selectedIndexes
                        drawItem(
                            item,
                            selectedItem,
                            true,
                            {
                                onItemSelected(index, item)
                            },
                            {
                                onItemDeselected(index, item)
                            }
                        )

                        if (index < items.lastIndex) {
                            Divider(modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun DropdownWithCheckboxesMenuItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onChecked: () -> Unit,
    onUnchecked: () -> Unit,
) {
    val contentColor = when {
        !enabled -> MaterialTheme.colors.onSurface.copy(alpha = MaterialColors.ALPHA_DISABLED)
        selected -> MaterialTheme.colors.primary.copy(alpha = MaterialColors.ALPHA_FULL)
        else -> MaterialTheme.colors.onSurface.copy(alpha = MaterialColors.ALPHA_FULL)
    }
    var checkedState by remember { mutableStateOf(selected) }


    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                        if (checkedState)
                            onChecked()
                        else onUnchecked()
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = text,
                    style = Typography.subtitle1.copy(color = Color.DarkGray),
                )
            }

        }
    }
}
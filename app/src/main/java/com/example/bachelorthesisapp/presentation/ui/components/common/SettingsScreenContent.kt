package com.example.bachelorthesisapp.presentation.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material3.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralAccent
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Typography

@Composable
fun SettingsScreenContent(
    darkThemeChecked: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dark Theme", color = MaterialTheme.colors.primaryVariant,
                    style = Typography.body2
                )
                Spacer(modifier = Modifier.width(20.dp))
                Switch(
                    checked = darkThemeChecked,
                    onCheckedChange = {
                        onThemeChange(it)
                    },
                    colors = SwitchDefaults.colors(
                        // unchecked colors
                        uncheckedTrackColor = CoralLight,
                        uncheckedBorderColor = CoralAccent,
                        uncheckedThumbColor = CoralAccent,
                        uncheckedIconColor = CoralAccent,
                        // checked colors
                        checkedTrackColor = CoralLight,
                        checkedBorderColor = CoralLight,
                        checkedThumbColor = Coral,
                        checkedIconColor = Coral,
                    ),
                    thumbContent = {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = null,
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                )
            }

        }

        item {
            Divider()
        }

    }
}
package com.example.bachelorthesisapp.presentation.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.presentation.ui.navigation.NavigationItemBusiness
import com.example.bachelorthesisapp.presentation.ui.navigation.NavigationItemClient
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun AppBar(title: String) {
    TopAppBar(title = { Text(text = title) })
}

@Composable
fun BusinessHomeAppBar(
    title: String,
    scaffoldState: ScaffoldState
) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        modifier = Modifier.height(120.dp),
        backgroundColor = Color.White,
        elevation = 15.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp, start = 5.dp, end = 5.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.apply {
                            if (isClosed) open() else close()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    // .padding(start = 7.dp, top = 7.dp)
                    .width(50.dp)
                    .height(50.dp),
                content =
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_menu_24),
                        contentDescription = "Menu icon",
                        tint = Color.DarkGray
                    )
                }
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp)
                    .align(Alignment.Top),
                text = title,
                textAlign = TextAlign.Left,
                style = Typography.h1
            )
        }
    }
}

@Composable
fun BusinessSecondaryAppBar(
    title: String,
    navController: NavHostController,
    backgroundColor: Color = Coral,
    elevation: Dp = 15.dp
) {
    TopAppBar(
        modifier = Modifier.height(120.dp),
        backgroundColor = backgroundColor,
        elevation = elevation
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp, start = 5.dp, end = 5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = 7.dp, top = 7.dp)
                    .width(40.dp)
                    .height(40.dp),
                content =
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "Back icon",
                        tint = Color.White
                    )
                }
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp)
                    .align(Alignment.Top),
                text = title,
                textAlign = TextAlign.Left,
                style = Typography.h1,
                color = Color.White
            )
        }
    }
}

@Composable
fun BusinessProfileAppBar(title: String, navController: NavHostController) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        modifier = Modifier.height(70.dp),
        backgroundColor = Color.White,
        elevation = 15.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp, top = 10.dp, start = 5.dp, end = 5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(start = 7.dp, top = 7.dp)
                    .width(40.dp)
                    .height(40.dp),
                content =
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = "Back icon",
                        tint = Color.Black
                    )
                }
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 20.dp)
                    .align(Alignment.Top),
                text = title,
                textAlign = TextAlign.Left,
                style = Typography.h1,
                color = Color.Black
            )
        }
    }
}

@Composable
fun BottomNavigationBarBusiness(
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (NavigationItemBusiness) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val items = listOf(
        NavigationItemBusiness.Home,
        NavigationItemBusiness.Appointments,
        NavigationItemBusiness.Requests,
        NavigationItemBusiness.Feed,
    )

    BottomNavigation(
        modifier = modifier.height(70.dp),
        backgroundColor = Coral
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                selectedContentColor = CoralLight,
                unselectedContentColor = Color.White,
                alwaysShowLabel = false,
                selected = selected,
                onClick = { onItemClick(item) }
            )
        }

    }
}

@Composable
fun BottomNavigationBarClient(
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (NavigationItemClient) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val items = listOf(
        NavigationItemClient.Home,
        NavigationItemClient.Feed,
    )

    BottomNavigation(
        modifier = modifier.height(70.dp),
        backgroundColor = Coral
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                selectedContentColor = CoralLight,
                unselectedContentColor = Color.White,
                alwaysShowLabel = false,
                selected = selected,
                onClick = { onItemClick(item) }
            )
        }

    }
}
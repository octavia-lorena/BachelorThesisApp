package com.example.bachelorthesisapp.presentation.ui.screen.business

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.presentation.ui.components.AddPostFloatingButton
import com.example.bachelorthesisapp.presentation.ui.components.BottomNavigationBarBusiness
import com.example.bachelorthesisapp.presentation.ui.components.BusinessDrawerContent
import com.example.bachelorthesisapp.presentation.ui.components.BusinessHomeAppBar
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.ui.theme.Rose
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel

@Composable
fun BusinessHomeScreen(
    uid: String,
    authViewModel: AuthViewModel,
    navHostController: NavHostController,
) {
    // val userFlow = authViewModel.userFlow.collectAsState()
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = context) {
//        authViewModel.getBusinessById(uid)
//        userFlow.value?.let {
//            Log.d("USER", it.toString())
//        }
    }

    Scaffold(
        topBar = { BusinessHomeAppBar(title = "My Space", scaffoldState = scaffoldState) },
        bottomBar = {
            BottomNavigationBarBusiness(
                navController = navHostController,
                onItemClick = {
                    var route = it.route
                    val baseRoute = route.split("/")[0]
                    route = "$baseRoute/$uid"
                    navHostController.navigate(route)
                })
        },
        floatingActionButton = {
            AddPostFloatingButton(navHostController = navHostController)
        },
        scaffoldState = scaffoldState,
        drawerContent = {
            BusinessDrawerContent(
                uid = uid,
                authVM = authViewModel,
                navController = navHostController
            )
        },
        drawerGesturesEnabled = true,
        backgroundColor = Color.White
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding(), top = 10.dp)
        ) {
            BusinessHomeScreenContent()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun BusinessHomeScreenContent() {
    LazyColumn(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize(),
    ) {
        item {
            Card(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),
                backgroundColor = OffWhite
            ) {
                Text(text = "TODAY")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth(),
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .height(150.dp)
                            .fillMaxWidth(),
                        backgroundColor = OffWhite

                    ) {
                        Text(text = "UPCOMING")
                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth(),
                        backgroundColor = OffWhite

                    ) {
                        Text(text = "REQUESTS")
                    }
                }
                item {
                    Card(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth(),
                        backgroundColor = OffWhite

                    ) {
                        Text(text = "COMPLETED")
                    }
                }
            }
        }
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(63.dp)
                    .padding(top = 10.dp, bottom = 10.dp),
                backgroundColor = OffWhite

            ) {
                Text(text = "MY FEED")
            }
        }

    }
}
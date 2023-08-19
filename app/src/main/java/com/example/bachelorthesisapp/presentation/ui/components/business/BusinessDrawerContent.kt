package com.example.bachelorthesisapp.presentation.ui.components.business

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.viewmodel.AuthViewModel


@Composable
fun BusinessDrawerContent(
    authVM: AuthViewModel,
    navController: NavHostController
) {
    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Menu",
                modifier = Modifier
                    .padding(30.dp),
                style = Typography.h3,
                color = Color.DarkGray
            )
            Divider()
            Row(
                modifier = Modifier
                    .height(60.dp)
                    // .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
                    .clickable {
                        authVM.signOut()
//                        navController.navigate("login_screen"){
//                            popUpTo(navController.graph.id){
//                                inclusive = true
//                            }
//                        }
                        navController.popBackStack("login_screen", inclusive = false)
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_logout_24),
                    contentDescription = "",
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LOGOUT",
                    style = Typography.h3,
                    color = Color.DarkGray
                )
            }
            Divider()
        }
    }
}

@Composable
fun ClientDrawerContent(
    authVM: AuthViewModel,
    navController: NavHostController
) {
    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Menu",
                modifier = Modifier
                    .padding(30.dp),
                style = Typography.h3,
                color = Color.DarkGray
            )
            Divider()
            Row(
                modifier = Modifier
                    .height(60.dp)
                    // .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
                    .clickable {
                        authVM.signOut()
//                        navController.navigate("login_screen"){
//                            popUpTo(navController.graph.id){
//                                inclusive = true
//                            }
//                        }
                        navController.popBackStack("login_screen", inclusive = false)
                    }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_logout_24),
                    contentDescription = "",
                    tint = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LOGOUT",
                    style = Typography.h3,
                    color = Color.DarkGray
                )
            }
            Divider()
        }
    }
}
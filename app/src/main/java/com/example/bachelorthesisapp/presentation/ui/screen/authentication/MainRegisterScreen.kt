package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bachelorthesisapp.R
import com.example.bachelorthesisapp.presentation.ui.navigation.Routes
import com.example.bachelorthesisapp.presentation.ui.theme.NavyBlue
import com.example.bachelorthesisapp.presentation.ui.theme.Ochre
import com.example.bachelorthesisapp.presentation.ui.theme.Typography
import com.example.bachelorthesisapp.presentation.ui.theme.OffWhite
import com.example.bachelorthesisapp.presentation.ui.theme.Purple200
import com.example.bachelorthesisapp.presentation.ui.theme.Rose

@Composable
fun MainRegisterScreen(navController: NavHostController) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.main_register),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds

        )
        Scaffold(
            backgroundColor = Color.Transparent
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 80.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                Text(
                    text = "Create a new account",
                    style = Typography.body2,
                    color = Color.Black
                )
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(200.dp))
                    Text(
                        text = "You are a",
                        style = Typography.body2,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        modifier = Modifier
                            .width(220.dp)
                            .height(50.dp),
                        onClick = { navController.navigate(Routes.ClientRegisterScreen.route) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Ochre),
                        shape = RoundedCornerShape(50.dp)

                    ) {
                        Text(
                            text = "CLIENT",
                            style = Typography.button
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier
                            .width(220.dp)
                            .height(50.dp),
                        onClick = { navController.navigate(Routes.BusinessRegisterStep1Screen.route) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Ochre),
                        shape = RoundedCornerShape(50.dp)
                        //elevation =
                    ) {
                        Text(
                            text = "BUSINESS OWNER",
                            style = Typography.button
                        )
                    }

                }
            }

        }
    }

}
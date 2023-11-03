package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.Typography

@Composable
fun MainRegisterScreen(navController: NavHostController) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.login_gradient_background2),
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
                        top = 70.dp,
                        start = 20.dp,
                        end = 20.dp,
                        bottom = innerPadding.calculateBottomPadding()
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Create a new account",
                        style = Typography.body2,
                        color = Color.White
                    )
                }
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
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        modifier = Modifier
                            .width(220.dp)
                            .height(50.dp),
                        onClick = { navController.navigate(Routes.ClientRegisterScreen.route) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Coral),
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
                        colors = ButtonDefaults.buttonColors(backgroundColor = Coral),
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
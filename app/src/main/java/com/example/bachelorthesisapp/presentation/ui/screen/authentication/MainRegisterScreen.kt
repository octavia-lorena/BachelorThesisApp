package com.example.bachelorthesisapp.presentation.ui.screen.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.bachelorthesisapp.presentation.ui.theme.CoralLight
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
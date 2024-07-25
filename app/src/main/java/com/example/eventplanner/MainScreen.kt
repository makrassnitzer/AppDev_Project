package com.example.eventplanner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eventplanner.ui.theme.Purple80

@Composable
fun MainScreen(navController: NavController) {
    var modifier = Modifier
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            "Event Planner",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = Purple80
        )

        Image(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            "Monthly Overview:",
            fontSize = 22.sp,
            color = Purple80,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom) {
        OutlinedButton(
            onClick = {navController.navigate("addEvent")},
            modifier = Modifier
                .padding(bottom = 8.dp)
                .width(350.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Purple80
            ),
            border = BorderStroke(1.dp, Purple80)
        ) {
            Text("Add new Event",
                fontSize = 18.sp)
        }

        OutlinedButton(
            onClick = {navController.navigate("myEvents")},
            modifier = Modifier
                .padding(bottom = 8.dp)
                .width(350.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Purple80
            ),
            border = BorderStroke(1.dp, Purple80)
        ) {
            Text("My Events",
                fontSize = 18.sp)
        }

        OutlinedButton(
            onClick = {navController.navigate("calendar")},
            modifier = Modifier
                .padding(bottom = 8.dp)
                .width(350.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Purple80
            ),
            border = BorderStroke(1.dp, Purple80)
        ) {
            Text("View Calendar",
                fontSize = 18.sp)
        }
    }
}
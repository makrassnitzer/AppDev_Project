package com.example.eventplanner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eventplanner.ui.theme.EventPlannerTheme
import com.example.eventplanner.ui.theme.Purple80

@Composable
fun MainScreen() {
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
    }

    Column (horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom) {
        OutlinedButton(
            onClick = { /*TODO: Add Button 1 Action*/ },
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
            onClick = { /*TODO: Add Button 2 Action*/ },
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
            onClick = { /*TODO: Add Button 3 Action*/ },
            modifier = Modifier
                .padding(bottom = 8.dp)
                .width(350.dp)
                .height(50.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Purple80
            ),
            border = BorderStroke(1.dp, Purple80)
        ) {
            Text("View Calender",
                fontSize = 18.sp)
        }
    }

}
package com.example.eventplanner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    // set background img
    var modifier = Modifier
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // set header
        Text(
            "Event Planner",
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            color = Purple80
        )

        Image(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )

        // upcoming event card
        Card(
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onTertiary
            ),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Upcoming Events",
                        fontSize = 22.sp,
                        color = Purple80,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(5.dp)
                    )
                    Text(
                        text = "View All",
                        fontSize = 14.sp,
                        color = Purple80,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                LazyRow() {
                    items(4) { index ->
                        UpcomingEventsCard(
                            name = "Event $index"
                        )
                    }
                }
            }
        }

        // monthly overview card
        Card(
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onTertiary
            ),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Monthly Overview",
                        fontSize = 22.sp,
                        color = Purple80,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(5.dp)
                    )
                }
                MonthlyOverview()
            }
        }

    }
        // column for nav buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            OutlinedButton(
                onClick = { navController.navigate("addEvent") },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .width(350.dp)
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Purple80
                ),
                border = BorderStroke(1.dp, Purple80)
            ) {
                Text(
                    "Add new Event",
                    fontSize = 18.sp
                )
            }

            OutlinedButton(
                onClick = { navController.navigate("myEvents") },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .width(350.dp)
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Purple80
                ),
                border = BorderStroke(1.dp, Purple80)
            ) {
                Text(
                    "My Events",
                    fontSize = 18.sp
                )
            }

            OutlinedButton(
                onClick = { navController.navigate("calendar") },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .width(350.dp)
                    .height(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Purple80
                ),
                border = BorderStroke(1.dp, Purple80)
            ) {
                Text(
                    "View Calendar",
                    fontSize = 18.sp
                )
            }
        }
}

@Composable
private fun UpcomingEventsCard(name: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Purple80),
        modifier = Modifier
            .padding(10.dp)
            .width(165.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "name",
                    fontSize = 16.sp,
                    color = Purple80,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "datum",
                    color = Purple80,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }

            IconButton(
                onClick = { },
                modifier = Modifier.background(
                    color = Purple80,
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                Icon(Icons.Default.Info, tint = Color.White, contentDescription = null)
            }
        }
    }
}

@Composable
private fun MonthlyOverview() {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Purple80),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "number of events",
                    fontSize = 16.sp,
                    color = Purple80,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "ausgaben",
                    color = Purple80,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )

                Text(
                    text = "Orte",
                    color = Purple80,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light
                )
            }
            /*
            IconButton(
                onClick = { },
                modifier = Modifier.background(
                    color = Purple80,
                    shape = RoundedCornerShape(10.dp)
                )
            ) {
                Icon(Icons.Default., tint = Color.White, contentDescription = null)
            }

 */

        }
    }
}

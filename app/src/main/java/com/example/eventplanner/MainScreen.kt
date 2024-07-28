package com.example.eventplanner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eventplanner.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var selectedPeriod by remember {
        mutableStateOf("current month")
    }

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
            stringResource(id = R.string.app_name),
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
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Overview",
                        fontSize = 22.sp,
                        color = Purple80,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.padding(5.dp)
                    )

                    // dropdown for small overview
                    ExposedDropdownMenuBox(
                            expanded = isExpanded,
                            onExpandedChange = { isExpanded = it },
                            modifier = Modifier
                                .width(170.dp)
                                .height(45.dp)
                        ) {
                            TextField(
                                value = selectedPeriod,
                                onValueChange = {},
                                readOnly = true,
                                textStyle = TextStyle().copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Purple80
                                ),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedTrailingIconColor = Purple80,
                                    unfocusedTrailingIconColor = Purple80,
                                    focusedIndicatorColor = Purple80,
                                    unfocusedIndicatorColor = Purple80
                                ),
                                modifier = Modifier.menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "current month",
                                            color = Purple80,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    },
                                    onClick = {
                                        selectedPeriod = "current month"
                                        isExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "last month",
                                            color = Purple80,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    },
                                    onClick = {
                                        selectedPeriod = "last month"
                                        isExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = "the last 90 days",
                                            color = Purple80,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Light
                                        )
                                    },
                                    onClick = {
                                        selectedPeriod = "the last 90 days"
                                        isExpanded = false
                                    }
                                )
                        }
                    }
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
                    "View all Events",
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
        }
    }
}

package com.hoanhph29102.Assignment_Kotlin.order.order

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.hoanhph29102.Assignment_Kotlin.activity.ButtonSplash

@Composable
fun OrderScreen(navController: NavController) {
    val orderViewModel: OrderViewModel = viewModel(
        factory = OrderViewModelFactory(OrderService.getInstance())
    )

    val orders by orderViewModel.orderProducts.collectAsState()

    val user = FirebaseAuth.getInstance().currentUser
    val userId = user?.uid

    LaunchedEffect(Unit){
        userId?.let { orderViewModel.fetchOrder(userId = it) }
    }

    //Log.e("TAG", "OrderScreen: $orders", )
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(orders){order ->
            ItemOrder(order = order, navController = navController)
        }
    }
}

@Composable
fun ItemOrder(order: Order, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
        ,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column( modifier = Modifier.fillMaxSize()
        ) {
            Row(modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Text(
                    text = "Order No${order.OrderID}",
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Gray,
                )
            }
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Row(modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
                ) {
                Row {
                    Text(
                        text = "Quantity: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                    Text(
                        text = "${order.totalQuantity}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row {
                    Text(
                        text = "Total: ",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                    Text(
                        text = "${order.totalMoney}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }

            Row(modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ButtonSplash(modifier = Modifier
                    .padding(12.dp)
                    .width(100.dp)
                    .height(40.dp), text = "Detail") {
                    navController.navigate("orderDetail/${order._id}")
                }
                Text(text = "Da giao", style = MaterialTheme.typography.titleLarge)
            }

        }
    }
}




//@Preview(showBackground = true)
//@Composable
//fun PreviewOrderTest() {
//    ItemOrder()
//}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun PreviewOrder() {
//    OrderScreen()
//}
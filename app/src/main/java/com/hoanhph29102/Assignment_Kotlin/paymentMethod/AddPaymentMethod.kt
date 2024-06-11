package com.hoanhph29102.Assignment_Kotlin.paymentMethod

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hoanhph29102.Assignment_Kotlin.activity.ButtonSplash
import com.hoanhph29102.Assignment_Kotlin.activity.HeaderWithBack
import com.hoanhph29102.Assignment_Kotlin.profile.User
import com.hoanhph29102.assignment_kotlin.R
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentScreen(navController: NavController) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val defaultUserName = currentUser?.displayName ?: ""

    var cardNumberState by remember { mutableStateOf(TextFieldValue("")) }
    var cvvState by remember { mutableStateOf(TextFieldValue(""))}

    var expirationDateState by remember { mutableStateOf(TextFieldValue("")) }
    val errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()
    val viewModel: PaymentMethodViewModel = viewModel()

    Scaffold(
        topBar = {
                 HeaderWithBack(modifier = Modifier, text = "Add Payment", navController = navController)
        },
        bottomBar = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp), contentAlignment = Alignment.Center){
                ButtonSplash(modifier = Modifier
                    .width(335.dp)
                    .height(60.dp), text = "ADD NEW CARD") {
                    val newPayment = PaymentMethod(
                        idMethod = UUID.randomUUID().toString(),
                        cardNumber = cardNumberState.text,
                        cvv = cvvState.text,
                        expirationDate = expirationDateState.text
                    )
                    addPaymentMethod(newPayment, navController = navController)

                }
            }
        }
    ) {paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)){
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CardPaymentMethodDemo()
                Spacer(modifier = Modifier.height(40.dp))
                AddPaymentMethod(
                    defaultName = defaultUserName,
                    cardNumberState = cardNumberState,
                    cvvState = cvvState,
                    expiryState = expirationDateState,
                    onCardNumberChange = {cardNumberState = it},
                    onCvvChange = {cvvState = it},
                    onExpiryChange = {expirationDateState = it},
                    errorMessage = errorMessage
                )
            }
        }
    }
}

fun addPaymentMethod(paymentMethod: PaymentMethod, navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid

    if (userId != null) {
        firestore.collection("user").document(userId).get().addOnSuccessListener { document ->
            if (document.exists()) {
                val user = document.toObject(User::class.java)
                if (user != null) {
                    val updatedPayment = user.paymentMethods?.toMutableList()?.apply { add(paymentMethod) } ?: mutableListOf(paymentMethod)
                    user.paymentMethods = updatedPayment
                    firestore.collection("user").document(userId).set(user)
                        .addOnSuccessListener {
                            navController.navigate("paymentMethod")
                        }
                        .addOnFailureListener { e ->
                            Log.e("Payment add", "addAddress: $e", )
                        }
                } else {
                    // Xử lý khi không thể chuyển đổi tài liệu thành đối tượng User
                    Log.e("Payment add", "Payment: không thể chuyển đổi tài liệu thành đối tượng User", )
                }
            } else {

                // Xử lý khi tài liệu không tồn tại
                Log.e("Payment add", "Payment: tài liệu không tồn tại", )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentMethod(
    defaultName: String,
    cardNumberState: TextFieldValue,
    cvvState: TextFieldValue,
    expiryState: TextFieldValue,
    onCardNumberChange: (TextFieldValue) -> Unit,
    onCvvChange: (TextFieldValue) -> Unit,
    onExpiryChange: (TextFieldValue) -> Unit,
    errorMessage: String?
) {
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OutlinedTextField(
            value = TextFieldValue(defaultName),
            onValueChange = {},
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                containerColor = Color.LightGray
            ),
            readOnly = true
        )

        OutlinedTextField(
            value = cardNumberState,
            onValueChange = onCardNumberChange,
            modifier = Modifier
                .height(66.dp)
                .width(335.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.LightGray,
            ),
            label = {
                Text(text = "Card Number", style = MaterialTheme.typography.titleMedium)
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = cvvState,
                onValueChange = onCvvChange,
                modifier = Modifier
                    .height(66.dp)
                    .width(150.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    containerColor = Color.LightGray

                ),
                placeholder = {
                    Text(text = "CVV",style = MaterialTheme.typography.titleMedium)
                }
            )
            Spacer(modifier = Modifier.width(30.dp))
            OutlinedTextField(
                value = expiryState,
                onValueChange = onExpiryChange,
                modifier = Modifier
                    .height(66.dp)
                    .width(150.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.LightGray,
                ),
                placeholder = {
                    Text(text = "Expiry",style = MaterialTheme.typography.titleMedium)
                }
            )
        }
    }
}

@Composable
fun CardPaymentMethodDemo() {
    //var isDefault by remember { mutableStateOf(paymentMethod.isDefault) }
    Column(modifier = Modifier) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(12.dp)
            ,
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xEB1A1A1A)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(shape = RectangleShape)
                        .background(Color.LightGray)
                        .width(31.dp)
                        .height(24.dp)
                    ,
                ) {
                    Image(
                        painterResource(id = R.drawable.mastercard2),
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
//                    AsyncImage(
//                        model = R.drawable.ic_launcher_background,
//                        contentDescription = "img product",
//                        modifier = Modifier.fillMaxSize(),
//                        contentScale = ContentScale.Crop// Đảm bảo rằng hình ảnh lấp đầy Box
//                    )
                }
                Text(text = "**** **** **** 3030", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Card Holder Name", style = MaterialTheme.typography.titleSmall,color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "XXXX",style = MaterialTheme.typography.titleSmall,color = Color.White)
                    }
                    Column {
                        Text(text = "Expiry Date", style = MaterialTheme.typography.titleSmall,color = Color.White)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = "XX/XX",style = MaterialTheme.typography.titleSmall,color = Color.White)
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewAddPayment() {
//    Assignment_KotlinTheme {
//        AddPaymentMethod()
//    }
//}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewAddPaymentScreen() {
//    Assignment_KotlinTheme {
//        AddPaymentScreen()
//    }
//}
package com.hoanhph29102.Assignment_Kotlin.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.hoanhph29102.assignment_kotlin.R


@Composable
fun ProductScreen(navController: NavController) {

    val productViewModel: ProductViewModel = viewModel()
    val products by productViewModel.products.collectAsState()
    ListMenuChip()
    ListProduct(products = products, navController = navController)
}

@Composable
fun ListProduct(products: List<Product>,navController: NavController) {

    LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
        items(products){product ->
            ItemProduct(product = product, navController = navController)

        }
    },
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp)
    )
}
@Composable
fun ItemProduct(product: Product,navController: NavController) {
    //val productJson = Uri.encode(Gson().toJson(product))
    var context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                //Log.e("Home", "ItemProduct: ${product._id}",)
                navController.navigate("productDetail/${product._id}")
            },
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .width(155.dp)
                .height(200.dp),

            ) {
            AsyncImage(
                model = product.image,
                contentDescription = "img product",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Text(text = product.name, fontSize = 17.sp, color = Color.Gray)
        Text(text = "$ ${product.price}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold,)
        //Log.e("TAG", "ItemProduct: ${product._id}", )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemChipFilter(icon: Int, text: String,isSelected: Boolean, onClick: () -> Unit) {
//    var isSelectedChip by remember {
//        mutableStateOf(false)
//    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilterChip(selected = isSelected,
            onClick = onClick,
            label = {

            },
            border = FilterChipDefaults.filterChipBorder(borderWidth = 0.dp, borderColor = Color.Transparent),
            colors = FilterChipDefaults.filterChipColors(
                containerColor = Color(0x9ED8D8D8),
                selectedContainerColor = Color.Black,
                selectedLeadingIconColor = Color.White,
                selectedTrailingIconColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = icon), contentDescription = "filter stars", modifier = Modifier.size(40.dp))
            },
            modifier = Modifier
                .width(44.dp)
                .height(44.dp)
                .align(Alignment.CenterHorizontally),

            )

        Text(text = text, fontSize = 14.sp, color = if (isSelected) Color.Black else Color(
            0x6D949494
        )
        )

    }
}

@Composable
fun ListMenuChip() {
    val listMenuChip = fakeMenuChip()
    val listMenuChip1 = listOf(
        Category(icon = R.drawable.stars, text = "Popular"),
        Category(icon = R.drawable.chair, text = "Chair"),
        Category(icon = R.drawable.table, text = "Table"),
        Category(icon = R.drawable.armchair, text = "Armchair"),
        Category(icon = R.drawable.bed, text = "Bed"),
        Category(icon = R.drawable.lamp, text = "Lamp"),
    )
    var selectedChipIndex by remember { mutableStateOf(-1) }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(4.dp)
    ){
        items(listMenuChip1) {item ->
            val index = listMenuChip1.indexOf(item)
            ItemChipFilter(icon = item.icon,
                text = item.text,
                isSelected = selectedChipIndex == index,
                onClick = {
                    if (selectedChipIndex == index) {
                        selectedChipIndex = -1
                    } else {
                        selectedChipIndex = index
                    }

                }
            )
        }

    }
}

fun fakeMenuChip() : List<Category> {
    return List(10) { index ->
        Category(icon = R.drawable.icon_cart, text = "Item $index")
    }
}
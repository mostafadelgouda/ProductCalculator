package com.example.productcalculator.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.compose.ProductCalculatorTheme
import com.example.productcalculator.data.History
import com.example.productcalculator.data.Product
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(viewModel: ProductCalculatorViewModel) {
    val products by viewModel.getProducts().collectAsState(emptyList())
    val productsIds by viewModel.itemsIds.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    val addProductId: (Int) -> Unit = { id ->
        viewModel.addProductId(id)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        // Use Box composable to achieve the layout distribution
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(3f)
        ) {
            ResultScreen(productsIds, viewModel, totalPrice)
        }

        // Use Box composable to achieve the layout distribution
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(7f)
        ) {
            ProductGrid(products, addProductId)
        }

        // Use Row composable to position the buttons at the end of the screen
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            //horizontalArrangement = Arrangement.End
        ) {

            Button(
                modifier = Modifier
                    //.background(MaterialTheme.colorScheme.background)
                    .weight(1f).padding(end = 2.dp),
                onClick = { viewModel.clearProductsIds() },
                shape = MaterialTheme.shapes.small,
                //border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text(
                    text ="Clear",

                    )
            }
            Button(
                modifier = Modifier
                    .weight(1f).padding(start = 2.dp),
                onClick = {
                    viewModel.addHistoryItem(
                        History(
                            productsIds,
                            totalPrice,
                            LocalDateTime.now()
                        )
                    )
                    viewModel.clearProductsIds()
                },
                shape = MaterialTheme.shapes.small,
                //border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text("Confirm")
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResultScreen(productsIds: List<Pair<Int, Int>>, viewModel: ProductCalculatorViewModel, totalPrice: Double) {
    Column(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier

                //.padding(3.dp).padding(bottom = 0.dp)
                .fillMaxWidth()
                .border(BorderStroke(2.dp, MaterialTheme.colorScheme.primaryContainer))
                .weight(1.8f)

        ) {
            LazyHorizontalGrid(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxWidth(),
                rows = GridCells.Fixed(2),
            ) {
                items(productsIds) { item ->
                    val product =
                        viewModel.getProductWithId(item.first)
                            .collectAsState(Product("", 0.0)).value
                    MinimizedProductItem(
                        product,
                        item.second
                    ) { viewModel.removeProductWithId(item.first) }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(top = 0.dp)
                //.padding(3.dp)
                .fillMaxWidth()
                //.border(BorderStroke(2.dp, MaterialTheme.colorScheme.primary))
                .weight(0.5f)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
            ) {
                Text(
                    text = "Total Price = ",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = "$totalPrice",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayMedium
                )
            }

        }
        Divider(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                //.padding(5.dp)//.background(MaterialTheme.colorScheme.onPrimaryContainer),

        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductGrid(products: List<Product>,  addItemId: (Int) -> Unit){
    LazyVerticalGrid(
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(products.size) { index ->
            //Text(products.size.toString())

            ProductItem(products[index], addItemId)

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(item: Product, addItemId: (Int) -> Unit) {
    Card(
        onClick = { addItemId(item.id) },
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        ),
        //border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                //color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "$${item.price}",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                //color = MaterialTheme.colorScheme.onPrimary

            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinimizedProductItem(item: Product, quantity: Int, removeProductWithId: (Int) -> Unit){
    var name by rememberSaveable { mutableStateOf(item.name) }
    var price by rememberSaveable { mutableDoubleStateOf(item.price) }

    Card(

        onClick = {
            removeProductWithId(item.id)
        },
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        )

        ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
        ){
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = item.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displaySmall,
                //color = MaterialTheme.colorScheme.onPrimary
            )
            Row {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "$${item.price}  |  ",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall,
                    //color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Q : $quantity",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall,
                    //color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }



    }
}





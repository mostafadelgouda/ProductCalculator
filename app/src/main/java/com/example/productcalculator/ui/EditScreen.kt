package com.example.productcalculator.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.compose.ProductCalculatorTheme
import com.example.productcalculator.Greeting
import com.example.productcalculator.R
import com.example.productcalculator.data.Product
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditScreen(viewModel: ProductCalculatorViewModel) {
    val products by viewModel.getProducts().collectAsState(emptyList())
    val addProduct: (Product) -> Unit = { product ->
        viewModel.addProduct(product)
    }
    val deleteProduct: (Product) -> Unit = { product ->
        viewModel.deleteProduct(product)
    }
    Column(
        modifier = Modifier.fillMaxHeight().background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            onClick = { viewModel.addProduct(Product(name = "AAAA", price = 0.0)) }){
            Text("Add new Product")
        }

        LazyColumn() {
            items(products.size) { index ->
                ItemCard(products[index], addProduct, deleteProduct)

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(item: Product, addProduct: (Product) -> Unit, deleteProduct: (Product) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    var name by rememberSaveable { mutableStateOf(item.name) }
    var price by rememberSaveable { mutableStateOf(item.price.toString()) }
    Card(
        onClick = {
            isExpanded = !isExpanded
            name = mutableStateOf(item.name).value
            price = mutableStateOf(item.price.toString()).value
                  },
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.background,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.DarkGray
        )

    ) {
        if(!isExpanded) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(5f),
                ){
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        text = "$${item.price}",
                        style = MaterialTheme.typography.displaySmall)
                }
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f),
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
        else {
            Row(

                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Product Name (${item.name})") },
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        colors = colors(
                            unfocusedLabelColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )

                    )
                    //val containerColor = FilledTextFieldTokens.ContainerColor.toColor()
                    TextField(
                        value = price.toString(),
                        onValueChange = { price = it },
                        label = { Text("Product Price (${item.price})") },
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        colors = colors(
                            unfocusedLabelColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black,
                            focusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unfocusedContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                    )
                    Row(

                    ) {

                        Button(
                            modifier = Modifier.weight(1f).padding(10.dp),
                            onClick = {
                                addProduct(
                                    Product(
                                        name = name,
                                        id = item.id,
                                        price = when(price){
                                            "" -> 0.0
                                            else -> price.toDouble()
                                        }
                                    )
                                )
                                isExpanded = !isExpanded
                            },
                        ) {
                            Text("Save")
                        }
                        Button(
                            onClick = {
                                deleteProduct(
                                    Product(
                                        name = name,
                                        id = item.id,
                                        price = price.toDouble()
                                    )
                                )
                            },
                            modifier = Modifier.weight(1f).padding(10.dp)
                        ) {
                            Text("Delete")

                        }
                        Button(
                            modifier = Modifier.weight(1f).padding(10.dp),
                            onClick = {
                                name = item.name
                                price = item.price.toString()
                                isExpanded = !isExpanded
                            },
                        ) {
                            Text("Cancel")


                        }
                    }

                }


            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemCardPreview() {
    ProductCalculatorTheme {
        ItemCard(Product("name", 1.0, 123), {}, {})
    }
}
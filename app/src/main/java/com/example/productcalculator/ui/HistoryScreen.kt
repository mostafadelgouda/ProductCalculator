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
import androidx.compose.material.icons.filled.ArrowDropDown
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
import com.example.productcalculator.Greeting
import com.example.productcalculator.R
import com.example.productcalculator.data.History
import com.example.productcalculator.data.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(viewModel: ProductCalculatorViewModel) {

    val historyItems by viewModel.getHistory().collectAsState(emptyList())
    val addHistoryItem: (History) -> Unit = { historyItem ->
        viewModel.addHistoryItem(historyItem)
    }
    val deleteHistoryItem: (History) -> Unit = { historyItem ->
        viewModel.deleteHistoryItem(historyItem)
    }
    val getProductById: (Int) -> Flow<Product> = { id ->
        viewModel.getProductWithId(id)
    }
    Column(
        modifier = Modifier.fillMaxHeight().background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            onClick = {
                //viewModel.addHistoryItem(History("hshs", 1.0, 5, LocalDateTime.now()))
            }){
            Text("Clear History")
        }

        LazyColumn() {
            items(historyItems.size) { index ->
                HistoryItemCard(historyItems[index], addHistoryItem, deleteHistoryItem, getProductById)

            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryItemCard(
    item: History, addHistoryItem: (History) -> Unit,
    deleteHistoryItem: (History) -> Unit, getProductById: (Int) -> Flow<Product>
) {
    var isExpanded by remember { mutableStateOf(false) }
    Card(
        onClick = { isExpanded = !isExpanded },
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
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(5f),
            ) {
                Text(
                    text = item.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    style = MaterialTheme.typography.displaySmall
                )
                Text(
                    text = "$${item.totalPrice}",
                    style = MaterialTheme.typography.displaySmall
                )
                Icons.Default.ArrowDropDown
            }
            Icon(
                imageVector = if(!isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                modifier = Modifier
                    .size(30.dp)
                    .weight(1f),
                contentDescription = null
            )
        }
        if (isExpanded) {
            Row(

                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(

                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        for (id in item.productsId) {
                            val product: Product = getProductById(id.first).collectAsState(
                                initial = Product(
                                    "",
                                    1.0,
                                    -1
                                )
                            ).value
                            val quantity = id.second

                            Text(
                                text = "${product.name} | Quantity: $quantity | Price: ${product.price * quantity}",
                                style = MaterialTheme.typography.displaySmall
                            )

                        }
                    }

                    Row(

                    ) {

                        Button(
                            modifier = Modifier.weight(1f).padding(10.dp),
                            onClick = {
                                deleteHistoryItem(item)
                                //isExpanded = !isExpanded
                            },
                        ) {
                            Text("Delete")
                        }
                        Button(
                            modifier = Modifier.weight(1f).padding(10.dp),
                            onClick = {
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


//@Preview(showBackground = true)
//@Composable
//fun ItemCardPreview() {
//    ProductCalculatorTheme {
//        ItemCard(Product("name", 1.0, 123), {}, {})
//    }
//}
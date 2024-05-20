package com.ioappstudio.emicalculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ioappstudio.emicalculator.data.CalculateLoanInterest
import com.ioappstudio.emicalculator.ui.theme.EMICalculatorTheme
import com.ioappstudio.emicalculator.viewmodels.CalculateLoanInterestViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EMICalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    EMICalculatorApp {
                        Toast.makeText(this, "Enter all the inputs", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun EMICalculatorApp(
    viewModel: CalculateLoanInterestViewModel = viewModel(),
    onError: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        val viewModelState = viewModel.interestViewModel.asStateFlow()

        var principle by remember { mutableStateOf("") }
        var interest by remember { mutableStateOf("") }
        var tenure by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFAC21))
                .padding(30.dp)
        ) {

            ElevatedCard(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()


            ) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth()
                        .background(color = Color.White), verticalArrangement = Arrangement.Top
                ) {

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        shape = RoundedCornerShape(100),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(id = R.string.input_principle_amount)) },
                        textStyle = TextStyle(fontSize = 18.sp, baselineShift = BaselineShift.None),
                        value = principle,
                        onValueChange = { inputString ->
                            principle = inputString.trim()
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        shape = RoundedCornerShape(100),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(id = R.string.input_interest)) },
                        textStyle = TextStyle(fontSize = 18.sp, baselineShift = BaselineShift.None),
                        value = interest,
                        onValueChange = { inputString ->
                            interest = inputString.trim()
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                        shape = RoundedCornerShape(100),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        label = { Text(stringResource(id = R.string.input_tenure)) },
                        textStyle = TextStyle(fontSize = 18.sp, baselineShift = BaselineShift.None),
                        value = tenure,
                        onValueChange = { inputString ->
                            tenure = inputString.trim()
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                        onClick = {
                            if (principle.isNotEmpty() && interest.isNotEmpty() && tenure.isNotEmpty()) {
                                viewModel.calculateTheLoan(
                                    principle.toFloat(),
                                    interest.toFloat(),
                                    tenure.toFloat()
                                )
                                viewModelState.value.isShowResult = true
                            } else {
                                onError.invoke()
                            }
                        }
                    ) {
                        Text(stringResource(id = R.string.calculate_button_txt))
                    }

                    if (viewModelState.collectAsState().value.isShowResult) {
                        ShowResultValue(viewModelState)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowResultValue(viewModelState: StateFlow<CalculateLoanInterest>) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp),
        text = "Principal Amount: ${viewModelState.collectAsState().value.principal.roundToLong()}"
    )

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 10.dp),
        text = "Monthly EMI:  ${viewModelState.collectAsState().value.emi.roundToLong()}"
    )

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 6.dp),
        text = "Interest Amount:  ${viewModelState.collectAsState().value.interest.roundToLong()}"
    )

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 6.dp, bottom = 10.dp),
        text = "Total Amount:  ${viewModelState.collectAsState().value.tenure.roundToLong()}"
    )
}


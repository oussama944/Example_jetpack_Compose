package com.projet.pourboire

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.projet.pourboire.components.InputField
import com.projet.pourboire.ui.theme.PourboireTheme
import com.projet.pourboire.util.calculatePerperson
import com.projet.pourboire.util.calculateTip
import com.projet.pourboire.widgets.ButtonRound

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PourboireTheme {
                // A surface container using the 'background' color from the theme

            }
            HeadApp {

            }
        }
    }
}

@Composable
fun HeadApp(content : @Composable () -> Unit){
    Column{
        Mid()
    }


}


@Preview(showBackground = true)
@Composable
fun Top(total:Double=135.0  ) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(30.dp))),
        color = Color.Magenta
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val totalFormat = "%.2f".format(total)
            Text(text = "Total par personnes : ", style = MaterialTheme.typography.h4)
            Text(text = "$totalFormat €", style = MaterialTheme.typography.h6, fontWeight = FontWeight.Bold)
        }
    }
}







@Preview(showBackground = true)
@Composable
fun Mid() {

    val range = 1..100

    val split = rememberSaveable{
        mutableStateOf(range.first)
    }

    val tipAmountState = rememberSaveable{
        mutableStateOf(0.0)
    }
    val totalPerPerson = rememberSaveable{
        mutableStateOf(0.0)
    }
    Column(modifier = Modifier.padding(12.dp)) {
        BillForm(
            split = split,
            tipAmountState = tipAmountState,
            totalPerPerson = totalPerPerson,
            range = range
        ){

        }
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier=Modifier,
    range: IntRange = 1..100,
    split: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPerson: MutableState<Double>,
    onValueChange:(String)-> Unit ={}
) {

    val totalBillState = rememberSaveable{
        mutableStateOf("")
    }

    val sliderPositionState = rememberSaveable{
        mutableStateOf(0f)
    }

    val validState = rememberSaveable(totalBillState.value){
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController= LocalSoftwareKeyboardController.current

    val tipPercentage = (sliderPositionState.value * range.last).toInt()


    Top(totalPerPerson.value)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(shape = CircleShape.copy(all = CornerSize(30.dp))),
        color = Color.LightGray,
        shape = RoundedCornerShape(corner = CornerSize(10.dp)),
        border = BorderStroke(width = 2.dp, color = Color.DarkGray)
    ) {
        Column(
            modifier = modifier.padding(all = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            InputField(
                valueState = totalBillState,
                label = "Entrer l'addition",
                enabled = true,
                isSingleline = true,
                onAction = KeyboardActions{
                    Log.d("gtgt", "${totalBillState.value}")
                    if(!validState) return@KeyboardActions
                    if(totalBillState.value.toDoubleOrNull() != null){
                        onValueChange(totalBillState.value.trim())
                        Log.d("testeeee", "${totalBillState.value}")
                    }
                    keyboardController?.hide()
                }
            )

            if(validState){
                Row(
                modifier = modifier.padding(3.dp),
                horizontalArrangement = Arrangement.Start) {
                    Text("Split",
                    modifier=modifier.align(
                        alignment = Alignment.CenterVertically
                    ))
                    Spacer(modifier = modifier.width(180.dp))
                    Row(modifier = modifier.padding(horizontal = 3.dp), horizontalArrangement = Arrangement.End) {
                        ButtonRound(
                            imageVector = Icons.Default.ArrowBack,
                            onClick ={
                                if (split.value>1){split.value-= 1}
                                if(totalBillState.value.toDoubleOrNull() != null)
                                    totalPerPerson.value = calculatePerperson(totalBill = totalBillState.value.toDouble(), splitBy = split.value, tipPercentage = tipPercentage)

                            })
                        Text(
                            text=split.value.toString(),
                            modifier = modifier
                                .align(Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp))
                        ButtonRound(
                            imageVector = Icons.Default.ArrowForward,
                            onClick ={
                                split.value+= 1
                                if(totalBillState.value.toDoubleOrNull() != null)
                                    totalPerPerson.value = calculatePerperson(totalBill = totalBillState.value.toDouble(), splitBy = split.value, tipPercentage = tipPercentage)

                            }
                        )
                    }
                }
                Row(
                    modifier = modifier.padding(horizontal = 12.dp, vertical = 12.dp)
                ){
                    Text(text = "Text",
                        modifier=modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = modifier.width(200.dp))
                    Text(text = "$ ${tipAmountState.value}",
                        modifier = modifier.align(alignment = Alignment.CenterVertically))
                }
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(text = " $tipPercentage %")

                    Spacer(modifier = modifier.height(14.dp))

                    Slider(value = sliderPositionState.value, onValueChange = {newval->
                        sliderPositionState.value = newval
                        if(totalBillState.value.toDoubleOrNull() != null){
                            tipAmountState.value = calculateTip(totalBill = totalBillState.value.toDouble(),tipPercentage = tipPercentage)
                            totalPerPerson.value = calculatePerperson(totalBill = totalBillState.value.toDouble(), splitBy = split.value, tipPercentage = tipPercentage)
                        }
                                                                 },
                    modifier = modifier.padding(start = 16.dp,end=16.dp),steps = 5 )

                }

            }else{
                Box(){}
            }
        }
    }
}


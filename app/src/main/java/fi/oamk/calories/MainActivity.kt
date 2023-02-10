package fi.oamk.calories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import fi.oamk.calories.ui.theme.CaloriesTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaloriesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Calories()
                }
            }
        }
    }
}
@Composable
fun GenderSelect(male: Boolean, setMale: (Boolean) -> Unit) {
    Column(Modifier.selectableGroup()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = male,
                onClick = {
                    setMale(true)
                }
            )
            Text(text = "Male")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = !male,
                onClick = {
                    setMale(false)
                }
            )
            Text(text = "Female")
        }
    }
}

@Composable
fun WeightInput(weight: Int, setWeight: (String) -> Unit) {
    OutlinedTextField(
        value = weight.toString(),
        label = { Text(text = "Enter weight") },
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { setWeight(it) }
    )
}

@Composable
fun IntensityList(defaultSelected: String,setValue: (Float) -> Unit) {
    var textBoxSize by remember { mutableStateOf(Size.Zero) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    val icon =
        if (dropdownExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown
    val intensities = listOf("Light","Usual","Moderate","Hard","Very Hard")
    var selected by remember { mutableStateOf("Light") }
    OutlinedTextField(
        readOnly = true,
        value = selected,
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coords ->
                textBoxSize = coords.size.toSize()
            },
        onValueChange = {},
        label = { Text("Intensity") },
        trailingIcon = {
            Icon(
                icon,
                "loremipsum",
                modifier = Modifier.clickable {
                    dropdownExpanded = !dropdownExpanded
                },

                )
        }
    )
    DropdownMenu(
        expanded = dropdownExpanded,
        onDismissRequest = { dropdownExpanded = false },
        modifier = Modifier.width(with(LocalDensity.current) { textBoxSize.width.dp })
    ) {
        intensities.forEach(){
            DropdownMenuItem(
                onClick = {
                    selected = it
                    var intensity = when(it){
                        "Light" -> 1.3f
                        "Usual" -> 1.5f
                        "Moderate" -> 1.7f
                        "Hard" -> 2f
                        "Very Hard" -> 2.2f
                        else -> 0.0f
                    }
                    setValue(intensity)
                    dropdownExpanded = false
                }) {
                Text(it)
            }
        }
    }


}

@Composable
fun Calories() {
    var weight by remember{ mutableStateOf(0) }
    var isMale by remember{ mutableStateOf(true) }
    var intensity by remember { mutableStateOf(1.3f) }
    var result by remember{mutableStateOf(0)}
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Text("Calories",style = MaterialTheme.typography.h3)
        WeightInput(weight = weight, setWeight = {weight = it.toIntOrNull() ?: 0} )
        GenderSelect(male = isMale, setMale = {isMale = it})
        IntensityList(defaultSelected = "Light", setValue ={intensity = it} )
        Text(result.toString(),style = MaterialTheme.typography.h6)
        Button(onClick = {
            result = if (isMale) ((879 + 10.2 * weight)* intensity).roundToInt() else ((798 + 7.18 * weight)* intensity).roundToInt()
        },
        modifier = Modifier.fillMaxWidth()) {
            Text("Calculate")
        }
    }
}
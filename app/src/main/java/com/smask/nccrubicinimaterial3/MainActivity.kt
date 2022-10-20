package com.smask.nccrubicinimaterial3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smask.nccrubicinimaterial3.components.*
import com.smask.nccrubicinimaterial3.ui.theme.NccRubiciniMaterial3Theme
import com.smask.nccrubicinimaterial3.utils.myMailSender
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NccRubiciniMaterial3Theme {
                MyApp {
                    NCCRubiciniApp()
                }
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        content()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NCCRubiciniApp() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "NCC Rubicini Booking App",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
            )
        },
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(
                        vertical = 60.dp
                    )
            ) {
                MainContent()
            }
        }
    )
}

@Composable
fun MainContent() {
    val name = remember {
        mutableStateOf("")
    }

    val phoneNumber = remember {
        mutableStateOf("")
    }

    val departurePlace = remember {
        mutableStateOf("")
    }

    val arrivalPlace = remember {
        mutableStateOf("")
    }

    val date = remember {
        mutableStateOf("Date")
    }

    val time = remember {
        mutableStateOf("Time")
    }

    val email = remember {
        mutableStateOf("")
    }

    val numberOfPassengers = remember {
        mutableStateOf("")
    }

    val typeOfService = remember {
        mutableStateOf("")
    }

    val privacy = remember {
        mutableStateOf(false)
    }

    BookingForm(
        nameState = name,
        phoneNumberState = phoneNumber,
        departurePlaceState = departurePlace,
        arrivalPlaceState = arrivalPlace,
        dateState = date,
        timeState = time,
        emailState = email,
        numberOfPassengersState = numberOfPassengers,
        typeOfServiceState = typeOfService,
        privacyState = privacy
    ) { }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BookingForm(
    nameState: MutableState<String>,
    phoneNumberState: MutableState<String>,
    departurePlaceState: MutableState<String>,
    arrivalPlaceState: MutableState<String>,
    dateState: MutableState<String>,
    timeState: MutableState<String>,
    emailState: MutableState<String>,
    numberOfPassengersState: MutableState<String>,
    typeOfServiceState: MutableState<String>,
    privacyState: MutableState<Boolean>,
    function: () -> Unit
) {
    val itemsTypeOfService = listOf("Tranfer", "Disposal")
    val itemsPassengers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = nameState.value,
        onValueChange = { nameState.value = it },
        label = { Text("Name and Surname") },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(imeAction = ImeAction.Next)
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
    OutlinedTextField(
        value = phoneNumberState.value,
        onValueChange = { phoneNumberState.value = it },
        label = { Text("Phone Number") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(imeAction = ImeAction.Next)
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
    OutlinedTextField(
        value = departurePlaceState.value,
        onValueChange = { departurePlaceState.value = it },
        label = { Text("Departure") },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(imeAction = ImeAction.Next)
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
    OutlinedTextField(
        value = arrivalPlaceState.value,
        onValueChange = { arrivalPlaceState.value = it },
        label = { Text("Arrival") },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(imeAction = ImeAction.Next)
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )

    MyDatePicker(dateState)
    MyTimePicker(timeState)

    OutlinedTextField(
        value = emailState.value,
        onValueChange = { emailState.value = it },
        label = { Text("E-Mail") },
        placeholder = { Text("example@gmail.com") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(imeAction = ImeAction.Next)
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )

    MyExposedDropdownMenu(itemsPassengers, numberOfPassengersState, "Passengers")

    MyExposedDropdownMenu(itemsTypeOfService, typeOfServiceState, "Type of service")

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = privacyState.value,
            onCheckedChange = { privacyState.value = it }
        )
        Text("Accept Privacy Policy and Terms of Use")
    }

    if (privacyState.value) {
        Button(
            onClick = {
                PrepareAndSendEmail(
                    nameState.value,
                    phoneNumberState.value,
                    departurePlaceState.value,
                    arrivalPlaceState.value,
                    dateState.value,
                    timeState.value,
                    emailState.value,
                    numberOfPassengersState.value,
                    typeOfServiceState.value,
                    privacyState.value
                )
            },
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
        ) {
            Icon(
                Icons.Filled.Email,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Send Email")
        }
    }

}

fun PrepareAndSendEmail(
    name: String,
    phoneNumber: String,
    departurePlace: String,
    arrivalPlace: String,
    date: String,
    time: String,
    email: String,
    numberOfPassengers: String,
    typeOfService: String,
    privacy: Boolean,
) {
    val bodyMessage = buildString {
        append("<h1>Booking from Rubicini Android App</h1>")
        append("<table style=\"border-collapse: collapse; width: 40%;\" border=\"1\">")
        append("<tbody>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Name and Surname</td>")
        append("<td style=\"width: 70%;\">$name</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Phone Number</td>")
        append("<td style=\"width: 70%;\">$phoneNumber</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Departure</td>")
        append("<td style=\"width: 70%;\">$departurePlace</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Arrival</td>")
        append("<td style=\"width: 70%;\">$arrivalPlace</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Date</td>")
        append("<td style=\"width: 70%;\">$date</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Time</td>")
        append("<td style=\"width: 70%;\">$time</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Email</td>")
        append("<td style=\"width: 70%;\">$email</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Number of passengers</td>")
        append("<td style=\"width: 70%;\">$numberOfPassengers</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Type of service</td>")
        append("<td style=\"width: 70%;\">$typeOfService</td>")
        append("</tr>")
        append("<tr>")
        append("<td style=\"width: 30%; background: LightGray\">Privacy accepted</td>")
        append("<td style=\"width: 70%;\">$privacy</td>")
        append("</tr>")
        append("</tbody>")
        append("</table>")
    }
    myMailSender(bodyMessage)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NccRubiciniMaterial3Theme {
        MyApp {
            NCCRubiciniApp()
        }
    }
}
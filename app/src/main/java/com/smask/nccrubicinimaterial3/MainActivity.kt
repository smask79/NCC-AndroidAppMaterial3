package com.smask.nccrubicinimaterial3

import SimpleEmailSender
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smask.nccrubicinimaterial3.components.MyDatePicker
import com.smask.nccrubicinimaterial3.components.MyTimePicker
import com.smask.nccrubicinimaterial3.ui.theme.NccRubiciniMaterial3Theme
import com.smask.nccrubicinimaterial3.utils.eeemyMailSender
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
//                navigationIcon = {
//                    IconButton(onClick = { /* doSomething() */ }) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = "Localized description"
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /* doSomething() */ }) {
//                        Icon(
//                            imageVector = Icons.Filled.Favorite,
//                            contentDescription = "Localized description"
//                        )
//                    }
//                }
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

    val mobileNumber = remember {
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
        mobileNumberState = mobileNumber,
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
    mobileNumberState: MutableState<String>,
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
        value = mobileNumberState.value,
        onValueChange = { mobileNumberState.value = it },
        label = { Text("Mobile Number") },
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
    OutlinedTextField(
        value = numberOfPassengersState.value,
        onValueChange = { numberOfPassengersState.value = it },
        label = { Text("Passengers") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        keyboardActions = KeyboardActions(onDone = {
            this.defaultKeyboardAction(imeAction = ImeAction.Next)
        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
    OutlinedTextField(
        value = typeOfServiceState.value,
        onValueChange = { typeOfServiceState.value = it },
        label = { Text("Type of service") },
        singleLine = true,
        keyboardActions = KeyboardActions(onDone = {
            keyboardController?.hide()

        }),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
//            .clickable { MailFormViewModel.showDatePickerDialog(context)}
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = privacyState.value,
            onCheckedChange = { privacyState.value = it }
        )
        Text("Accept Privacy Policy and Terms of Use")
    }

    if (privacyState.value) {
        Button(
            onClick = { eeemyMailSender() },
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

fun myMailSender() {
    SimpleEmailSender()
        .setServer("smtp.gmail.com")
        .setHost(465)
        .setFrom("RubiciniApp")
        .setTo("smask80@gmail.com")
        .setUsername("servizi.rubicini@gmail.com")
        .setPassword("zuzmgnavaqalumow")
        .setSubject("Test")
        .setContent("Speriamo bene")
        .send()
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
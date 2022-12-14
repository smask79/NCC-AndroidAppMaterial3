package com.smask.nccrubicinimaterial3.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.smask.nccrubicinimaterial3.utils.MAIL_CLEANUP_STATUS
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.data.utils.getDefaultLangCode
import com.togitech.ccp.data.utils.getDefaultPhoneCode
import com.togitech.ccp.data.utils.getLibCountries
import java.util.*

// Creating a composable function to
// create two Images and a spacer between them
// Calling this function as content
// in the above function
@Composable
fun MyDatePicker(dateState: MutableState<String>) {

    // Fetching the Local Context
    val mContext = LocalContext.current

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format
    //val mDate = remember { mutableStateOf("") }

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
            dateState.value = String.format("%02d/%02d/%04d", mDayOfMonth, mMonth + 1, mYear)
            //dateState.value = "$mDayOfMonth/${mMonth + 1}/$mYear"
        }, mYear, mMonth, mDay
    )

    OutlinedCard(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = { mDatePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        )
        { Text(text = dateState.value, modifier = Modifier.fillMaxWidth()) }
    }
}

// Creating a composable function
// to create a Time Picker
// Calling this function as content
// in the above function
@Composable
fun MyTimePicker(timeState: MutableState<String>) {

    // Fetching local context
    val mContext = LocalContext.current

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string
    //val mTime = remember { mutableStateOf("") }

    // Creating a TimePicker dialod
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->
            timeState.value = String.format("%02d:%02d", mHour, mMinute)
        }, mHour, mMinute, true
    )

    OutlinedCard(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = { mTimePickerDialog.show() },
            modifier = Modifier.fillMaxWidth()
        )
        { Text(text = timeState.value, modifier = Modifier.fillMaxWidth()) }
    }
}

@Composable
fun MyDropdown(
    items: List<String>,
    typeOfServiceState: MutableState<String>,
    firstEnabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            items[selectedIndex], modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEachIndexed { index, s ->
                var enabled = true
                if (index == 0 && firstEnabled) {
                    enabled = false
                }
                DropdownMenuItem(
                    { Text(s) },
                    enabled = enabled,
                    onClick = {
                        selectedIndex = index
                        typeOfServiceState.value = items[selectedIndex]
                        expanded = false
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyExposedDropdownMenu(
    items: List<String>,
    genericState: MutableState<String>,
    myLabel: String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = genericState.value,
            onValueChange = { },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(10.dp),
            label = { Text(myLabel) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEach { selectionOption ->
                DropdownMenuItem(
                    { Text(text = selectionOption) },
                    onClick = {
                        genericState.value = selectionOption
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MyTrailingIcon() {
    IconButton(
        onClick = {
            Log.d("TEST1", "myTrailingIcon: CLICKED!!!")
        },
    ) {
        Icon(
            Icons.Default.ArrowDropDown,
            contentDescription = "",
            tint = Color.Black
        )
    }
}

@Composable
fun MyDialog(myIcon: ImageVector, myTitle: String, myText: String, mailStatus: MutableState<Int>) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
                mailStatus.value = MAIL_CLEANUP_STATUS
            },
            icon = { Icon(myIcon, contentDescription = null) },
            title = {
                Text(text = myTitle)
            },
            text = {
                Text(text = myText)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        mailStatus.value = MAIL_CLEANUP_STATUS
                    }
                ) {
                    Text("OK")
                }
            },
//            dismissButton = {
//                TextButton(
//                    onClick = {
//                        openDialog.value = false
//                    }
//                ) {
//                    Text("Dismiss")
//                }
//            }
        )
    }
}

@Composable
fun MyTogiCountryCodePicker(phoneNumberState: MutableState<String>) {
    val context = LocalContext.current
    var phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode(context)) }
    var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
    val phoneNumber = rememberSaveable { mutableStateOf("") }

    TogiCountryCodePicker(
        text = phoneNumber.value,
        onValueChange = { phoneNumber.value = it },
        defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
        pickedCountry = {
            phoneCode = it.countryPhoneCode
            defaultLang = it.countryCode
        },
        error = true
    )
    if (phoneNumber.value.isEmpty()) {
        phoneNumberState.value = ""
    } else {
        phoneNumberState.value = "$phoneCode${phoneNumber.value}"
    }
}
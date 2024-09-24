package com.example.efimsokolovaleksandarsekulovski_reminderapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.efimsokolovaleksandarsekulovski_reminderapp.ui.theme.EfimSokolovAleksandarSekulovskiReminderAppTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EfimSokolovAleksandarSekulovskiReminderAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ReminderApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ReminderApp(modifier: Modifier = Modifier) {
    var reminderMessage by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedTime by remember { mutableStateOf<Date?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    var isReminderSet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("hh:mm a", Locale.getDefault())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedDate = calendar.time
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            selectedTime = calendar.time
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = reminderMessage,
            onValueChange = { reminderMessage = it },
            label = { Text("Reminder Message") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Select Date")
            }
            Button(
                onClick = { timePickerDialog.show() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Select Time")
            }
        }

        Button(
            onClick = {
                if (reminderMessage.isNotBlank() && selectedDate != null && selectedTime != null) {
                    isReminderSet = true
                    snackbarMessage = "Reminder set successfully!"
                    showSnackbar = true
                } else {
                    snackbarMessage = "Please fill in all fields"
                    showSnackbar = true
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Set Reminder")
        }

        if (isReminderSet) {
            Text("Reminder: $reminderMessage")
            Text("Date: ${dateFormatter.format(selectedDate!!)}")
            Text("Time: ${timeFormatter.format(selectedTime!!)}")

            Button(
                onClick = {
                    reminderMessage = ""
                    selectedDate = null
                    selectedTime = null
                    isReminderSet = false
                    snackbarMessage = "Reminder cleared"
                    showSnackbar = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Clear Reminder")
            }
        }
    }

    if (showSnackbar) {
        Snackbar(
            modifier = Modifier.padding(16.dp),
            action = {
                TextButton(onClick = { showSnackbar = false }) {
                    Text("Dismiss")
                }
            }
        ) {
            Text(snackbarMessage)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderAppPreview() {
    EfimSokolovAleksandarSekulovskiReminderAppTheme {
        ReminderApp()
    }
}
package com.pemmob.nailaalifatul.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pemmob.nailaalifatul.R
import kotlinx.coroutines.launch

private val MainGreenColor = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubungiKamiScreen(navController: NavController) {
    var emailText by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var problemType by rememberSaveable { mutableStateOf("Pilih Tipe Pesan") }
    var isAgreed by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val isEmailValid = emailText.contains("@") && emailText.isNotBlank()
    val isMessageValid = messageText.length >= 10
    val isFormValid = isEmailValid && isMessageValid && isAgreed && problemType != "Pilih Tipe Pesan"

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hubungi Kami", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MainGreenColor),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.back_icon_svg),
                            contentDescription = "Back Icon",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        StatelessFormHubungiKami(
            modifier = Modifier.padding(paddingValues),
            email = emailText,
            onEmailChange = { emailText = it },
            isEmailValid = isEmailValid,
            message = messageText,
            onMessageChange = { messageText = it },
            isMessageValid = isMessageValid,
            problemType = problemType,
            onProblemTypeChange = { problemType = it },
            isAgreed = isAgreed,
            onAgreedChange = { isAgreed = it },
            imageUri = imageUri,
            onImagePicked = { imageUri = it },
            isFormValid = isFormValid,
            onSubmit = {
                scope.launch {
                    snackbarHostState.showSnackbar("Pesan Terkirim")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessFormHubungiKami(
    modifier: Modifier = Modifier,
    email: String, onEmailChange: (String) -> Unit, isEmailValid: Boolean,
    message: String, onMessageChange: (String) -> Unit, isMessageValid: Boolean,
    problemType: String, onProblemTypeChange: (String) -> Unit,
    isAgreed: Boolean, onAgreedChange: (Boolean) -> Unit,
    imageUri: Uri?, onImagePicked: (Uri?) -> Unit,
    isFormValid: Boolean, onSubmit: () -> Unit
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onImagePicked(uri) }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hubungi Kami",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email, // Fixed value from empty string
            onValueChange = onEmailChange,
            label = { Text("Email Anda") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.mail_icon_svg),
                    contentDescription = "Email"
                )
            },
            isError = email.isNotEmpty() && !isEmailValid,
            supportingText = { if (email.isNotEmpty() && !isEmailValid) Text("Format Email Salah") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        val options = listOf("Pertanyaan", "Keluhan", "Saran")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = problemType,
                onValueChange = { },
                label = { Text("Tipe Pesan") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption) },
                        onClick = {
                            onProblemTypeChange(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            label = { Text("Pesan") },
            isError = message.isNotEmpty() && !isMessageValid,
            supportingText = { if (message.isNotEmpty() && !isMessageValid) Text("Pesan minimal 10 karakter") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Upload",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Unggah Bukti (Screenshot / Foto)", color = Color.Black)
        }

        if (imageUri != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(painterResource(id = R.drawable.icon_app_jualan), contentDescription = "File")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("File terpilih: ${imageUri.lastPathSegment}")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isAgreed, onCheckedChange = onAgreedChange)
            Text("Saya menyetujui syarat & ketentuan")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            enabled = isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainGreenColor,
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledContentColor = Color.Gray
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.send_icon_svg),
                    contentDescription = "Send",
                    tint = if(isFormValid) Color.White else Color.Gray
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Text(
                    "Kirim Pesan", 
                    style = MaterialTheme.typography.labelLarge,
                    color = if(isFormValid) Color.White else Color.Gray
                )
            }
        }
    }
}
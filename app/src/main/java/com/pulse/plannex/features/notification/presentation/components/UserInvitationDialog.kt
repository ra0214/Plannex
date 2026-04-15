package com.pulse.plannex.features.notification.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.pulse.plannex.core.network.UserDto

@Composable
fun UserInvitationDialog(
    users: List<UserDto>,
    isLoading: Boolean,
    onInvite: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Invitar Usuarios",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (users.isEmpty()) {
                    Text("No se encontraron usuarios disponibles.")
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        items(users) { user ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = user.userName, style = MaterialTheme.typography.bodyLarge)
                                    user.email?.let {
                                        Text(text = it, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                                Button(onClick = { onInvite(user.id) }) {
                                    Text("Invitar")
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Cerrar")
                }
            }
        }
    }
}

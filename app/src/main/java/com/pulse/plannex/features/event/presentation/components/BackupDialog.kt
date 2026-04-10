package com.pulse.plannex.features.event.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun BackupDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Copia de seguridad") },
        text = { Text(text = "No se ha podido realizar la sincronización automática nocturna. ¿Desea realizar una copia de seguridad ahora?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Sincronizar ahora")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Más tarde")
            }
        }
    )
}

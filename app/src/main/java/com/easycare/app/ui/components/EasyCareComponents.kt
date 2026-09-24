package com.easycare.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.easycare.app.model.Medicine
import com.easycare.app.model.ContactTarget
import com.easycare.app.ui.theme.AppSpacing

@Composable
fun LargeActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth().heightIn(min = 72.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        icon?.let {
            Icon(it, contentDescription = null, modifier = Modifier.size(30.dp))
            Spacer(Modifier.width(AppSpacing.small))
        }
        Text(text, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
    }
}

@Composable
fun SecondaryActionButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().heightIn(min = 70.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
    ) { Text(text, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center) }
}

@Composable
fun EmergencyButton(onClick: () -> Unit, modifier: Modifier = Modifier, text: String = "EMERGENCY HELP") {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().heightIn(min = 92.dp).semantics {
            contentDescription = "Emergency help"
            role = Role.Button
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
        shape = RoundedCornerShape(24.dp),
    ) { Text(text, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) }
}

@Composable
fun LargeMenuCard(
    title: String,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(132.dp).semantics { this.contentDescription = contentDescription },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(AppSpacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(AppSpacing.small))
            Text(title, style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun VoiceButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Tap and Speak",
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(164.dp).semantics { contentDescription = "Tap and speak voice command" },
        shape = RoundedCornerShape(46.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(AppSpacing.small))
            Text(label, fontSize = 23.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(title: String, onBack: (() -> Unit)? = null) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack, modifier = Modifier.size(64.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back", modifier = Modifier.size(34.dp))
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
    )
}

@Composable
fun ContactCard(name: String, phone: String, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxWidth(), colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)) {
        Column(Modifier.padding(AppSpacing.large), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(name, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(AppSpacing.small))
            Text(phone, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ContactTargetSelector(
    selected: ContactTarget,
    familyName: String,
    onSelected: (ContactTarget) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth()) {
        Text("Send to", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(AppSpacing.small))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppSpacing.small),
        ) {
            FilterChip(
                selected = selected == ContactTarget.Family,
                onClick = { onSelected(ContactTarget.Family) },
                label = { Text(familyName, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center) },
                modifier = Modifier.weight(1f).heightIn(min = 64.dp),
            )
            FilterChip(
                selected = selected == ContactTarget.EmergencyContact,
                onClick = { onSelected(ContactTarget.EmergencyContact) },
                label = { Text("Emergency Contact", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center) },
                modifier = Modifier.weight(1f).heightIn(min = 64.dp),
            )
        }
    }
}

@Composable
fun MedicineCard(medicine: Medicine, isTaken: Boolean, onTaken: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(AppSpacing.large)) {
            Text(medicine.period, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(Modifier.height(4.dp))
            Text(medicine.name, style = MaterialTheme.typography.titleLarge)
            Text(medicine.time, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(AppSpacing.medium))
            LargeActionButton(
                text = if (isTaken) "DONE ✓" else "TAKEN",
                onClick = onTaken,
                icon = if (isTaken) Icons.Default.CheckCircle else null,
                enabled = !isTaken,
            )
        }
    }
}

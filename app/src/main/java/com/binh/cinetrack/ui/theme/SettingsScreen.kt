package com.binh.cinetrack.ui.theme

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.binh.cinetrack.WorkScheduler
import com.binh.cinetrack.data.preferences.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen (
    viewModel: SettingViewModel = hiltViewModel(),
    onBackClick: () -> Unit ={},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentTheme by viewModel.currentTheme.collectAsStateWithLifecycle()
    val isNotificationEnabled by viewModel.isNotificationEnabled.collectAsStateWithLifecycle()
    var permissionDeniedMessage by remember { mutableStateOf("") }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted){
            viewModel.toggleNotification(true)
            permissionDeniedMessage = ""
            WorkScheduler.scheduleDailyNotification(context, isTesting = true)
        } else {
            viewModel.toggleNotification(false)
            permissionDeniedMessage = "Quyền thông báo bị từ chối. Bạn có thể bật lại trong Cài đặt hệ thống."
            WorkScheduler.cancelDailyNotification(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Cài đặt", fontWeight = FontWeight.Bold)},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ){
            Text(
                text = "Giao diện ứng dụng",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            val themeOption = listOf(
                AppTheme.SYSTEM to "Theo hệ thống",
                AppTheme.LIGHT to "Sáng",
                AppTheme.DARK to "Tối"
            )
            themeOption.forEach { (theme, label) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable{viewModel.selectTheme(theme)}
                        .padding(vertical = 12.dp)
                ){
                    RadioButton(
                        selected = (currentTheme == theme),
                        onClick = {viewModel.selectTheme(theme)}
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Thông báo", fontWeight = FontWeight.Bold)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)){
                    Text(text ="Nhắc phim hằng ngày")
                    if (permissionDeniedMessage.isNotEmpty()){
                        Text(
                            text = permissionDeniedMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Switch(
                    checked = isNotificationEnabled,
                    onCheckedChange = {enabled ->
                        if (enabled){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) == PackageManager.PERMISSION_GRANTED
                                if (hasPermission){
                                    viewModel.toggleNotification(true)
                                    WorkScheduler.scheduleDailyNotification(context, isTesting = true)
                                }else{
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            }else{
                                viewModel.toggleNotification(true)
                                WorkScheduler.scheduleDailyNotification(context, isTesting = true)
                            }
                        }else{
                            viewModel.toggleNotification(false)
                            WorkScheduler.cancelDailyNotification(context)
                        }
                    }
                )
            }
        }
    }
}
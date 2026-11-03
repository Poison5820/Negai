package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.model.PlayerSettings
import com.example.model.StreamingQualityPreset
import com.example.torrent.P2PTrackerConfig
import com.example.torrent.TorrServerClient
import com.example.torrent.TorrentIntentHelper
import com.example.torrent.TorrentSearchService
import com.example.ui.i18n.LocalIsEnglish
import com.example.ui.i18n.tr
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TorrServerSettingsPage(
    settings: PlayerSettings,
    actions: SettingsActions
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isEnglish = LocalIsEnglish.current

    var urlInput by rememberSaveable(settings.torrServerUrl) {
        mutableStateOf(settings.torrServerUrl)
    }
    var usernameInput by rememberSaveable(settings.torrServerUsername) {
        mutableStateOf(settings.torrServerUsername)
    }
    var passwordInput by rememberSaveable(settings.torrServerPassword) {
        mutableStateOf(settings.torrServerPassword)
    }

    var isTestingServer by remember { mutableStateOf(false) }
    var serverStatusText by remember { mutableStateOf<String?>(null) }
    var serverOnline by remember { mutableStateOf<Boolean?>(null) }

    var indexerUrlInput by rememberSaveable(settings.torrentIndexerUrl) {
        mutableStateOf(settings.torrentIndexerUrl)
    }
    var indexerKeyInput by rememberSaveable(settings.torrentIndexerApiKey) {
        mutableStateOf(settings.torrentIndexerApiKey)
    }
    var isTestingSearch by remember { mutableStateOf(false) }
    var searchTestResultText by remember { mutableStateOf<String?>(null) }
    var searchTestOk by remember { mutableStateOf<Boolean?>(null) }

    val isTorrServerInstalled = remember {
        TorrentIntentHelper.isTorrServerAppInstalled(context)
    }

    // 0. Default Streaming Quality Card
    SettingsSectionCard(
        title = tr("Качество потоковой передачи", "Streaming Video Quality"),
        icon = Icons.Default.Download
    ) {
        Text(
            text = tr(
                "Стандартное качество для сетевых трансляций (HLS/DASH/HTTP) и раздач с несколькими вариантами разрешения",
                "Default quality for network streams (HLS/DASH/HTTP) and multi-resolution torrents"
            ),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StreamingQualityPreset.entries.forEach { preset ->
                FilterChip(
                    selected = settings.defaultStreamingQuality == preset,
                    onClick = { actions.onUpdateDefaultStreamingQuality(preset) },
                    label = {
                        Text(preset.localizedTitle(isEnglish))
                    },
                    modifier = Modifier.testTag("streaming_quality_preset_${preset.id}")
                )
            }
        }
    }

    // 1. TorrServer Connection Card
    SettingsSectionCard(
        title = tr("Подключение к TorrServer (MatriX)", "TorrServer (MatriX) Connection"),
        icon = Icons.Default.Cloud
    ) {
        // Quick address presets
        Text(
            text = tr("Быстрый выбор адреса сервера", "Quick Server Address Presets"),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = urlInput.contains("127.0.0.1") || urlInput.contains("localhost"),
                onClick = {
                    urlInput = "http://127.0.0.1:8090"
                    actions.onUpdateTorrServerUrl(urlInput)
                },
                label = { Text(tr("На этом устройстве (127.0.0.1:8090)", "On This Device (127.0.0.1:8090)")) },
                modifier = Modifier.testTag("torrserver_preset_local")
            )
            FilterChip(
                selected = urlInput.startsWith("http://192.168."),
                onClick = {
                    if (!urlInput.startsWith("http://192.168.")) {
                        urlInput = "http://192.168.1.100:8090"
                        actions.onUpdateTorrServerUrl(urlInput)
                    }
                },
                label = { Text(tr("Домашний ПК / ТВ-бокс (192.168.x.x)", "Home PC / TV Box (192.168.x.x)")) },
                modifier = Modifier.testTag("torrserver_preset_lan")
            )
        }

        OutlinedTextField(
            value = urlInput,
            onValueChange = { urlInput = it },
            label = { Text(tr("URL сервера TorrServer", "TorrServer URL")) },
            placeholder = { Text("http://127.0.0.1:8090") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("settings_torrserver_url_input")
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = usernameInput,
                onValueChange = { usernameInput = it },
                label = { Text(tr("Логин (если включена защита)", "Username (optional)")) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("settings_torrserver_user_input")
            )
            OutlinedTextField(
                value = passwordInput,
                onValueChange = { passwordInput = it },
                label = { Text(tr("Пароль", "Password")) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("settings_torrserver_pass_input")
            )
        }

        if (serverStatusText != null) {
            Surface(
                color = if (serverOnline == true) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                },
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(
                    1.dp,
                    if (serverOnline == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (serverOnline == true) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.ErrorOutline
                        },
                        contentDescription = null,
                        tint = if (serverOnline == true) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = serverStatusText.orEmpty(),
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    val normalized = TorrServerClient.normalizeBaseUrl(urlInput)
                    urlInput = normalized
                    actions.onUpdateTorrServerUrl(normalized)
                    actions.onUpdateTorrServerAuth(usernameInput.trim(), passwordInput)
                    scope.launch {
                        isTestingServer = true
                        val res = TorrServerClient.ping(
                            rawBaseUrl = normalized,
                            username = usernameInput.trim(),
                            password = passwordInput
                        )
                        isTestingServer = false
                        res.fold(
                            onSuccess = { version ->
                                serverOnline = true
                                serverStatusText = if (isEnglish) {
                                    "Connected! TorrServer $version is online ($normalized)"
                                } else {
                                    "Подключено! TorrServer $version на связи ($normalized)"
                                }
                            },
                            onFailure = { err ->
                                serverOnline = false
                                serverStatusText = if (isEnglish) {
                                    "Saved, but server did not respond: ${err.message ?: "check that TorrServer is running"}"
                                } else {
                                    "Сохранено, но сервер не ответил: ${err.message ?: "проверьте запуск TorrServer"}"
                                }
                            }
                        )
                    }
                },
                enabled = !isTestingServer,
                modifier = Modifier
                    .weight(1f)
                    .testTag("settings_torrserver_save_and_test_button")
            ) {
                if (isTestingServer) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(tr("Сохранить и проверить", "Save & Test"))
            }

            if (isTorrServerInstalled) {
                FilledTonalButton(
                    onClick = { TorrentIntentHelper.launchTorrServerApp(context) },
                    modifier = Modifier.testTag("settings_launch_torrserver_app_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(tr("Открыть TorrServer", "Open TorrServer"))
                }
            } else {
                OutlinedButton(
                    onClick = {
                        TorrentIntentHelper.openUrlInBrowser(
                            context,
                            "https://github.com/YouROK/TorrServer/releases"
                        )
                    },
                    modifier = Modifier.testTag("settings_download_torrserver_button")
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(tr("Скачать APK", "Download APK"))
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        SettingToggleRow(
            title = tr(
                "Мгновенный запуск одиночных фильмов",
                "Instant Playback for Single-File Movies"
            ),
            checked = settings.torrServerAutoPlaySingle,
            onCheckedChange = actions.onUpdateTorrServerAutoPlaySingle,
            testTag = "torrserver_autoplay_single_switch"
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        SettingToggleRow(
            title = tr("Сохранять открытые раздачи в списке TorrServer", "Save Opened Torrents to TorrServer List"),
            checked = settings.torrServerSaveToDb,
            onCheckedChange = actions.onUpdateTorrServerSaveToDb,
            testTag = "torrserver_save_db_switch"
        )
    }

    var p2pModel by remember { mutableStateOf(P2PTrackerConfig.loadConfig(context)) }
    var showP2pConfigDialog by remember { mutableStateOf(false) }
    var remoteConfigUrl by remember { mutableStateOf("https://raw.githubusercontent.com/Poison5820/Negai/main/app/src/main/assets/p2p_trackers.json") }
    var isLoadingRemote by remember { mutableStateOf(false) }
    var p2pDialogMessage by remember { mutableStateOf<String?>(null) }

    // 2. P2P Indexers & Decentralized Nodes (Play Store Compliant)
    SettingsSectionCard(
        title = tr("P2P-индексаторы и децентрализованные узлы", "P2P Indexers & Decentralized Nodes"),
        icon = Icons.Default.Search
    ) {
        // P2P Configuration Status Pill
        Surface(
            color = if (p2pModel.isConfigured) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colorScheme.error.copy(alpha = 0.10f)
            },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(
                1.dp,
                if (p2pModel.isConfigured) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                } else {
                    MaterialTheme.colorScheme.error.copy(alpha = 0.30f)
                }
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = if (p2pModel.isConfigured) {
                        tr(
                            "Конфигурация P2P активна: ${p2pModel.totalNodes} узлов",
                            "P2P Configuration active: ${p2pModel.totalNodes} nodes"
                        )
                    } else {
                        tr(
                            "Конфигурация P2P не загружена (требуется для поиска)",
                            "P2P Configuration not loaded (required for search)"
                        )
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (p2pModel.isConfigured) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Text(
                    text = if (p2pModel.isConfigured) {
                        p2pModel.sourceDescription
                    } else {
                        tr(
                            "Для соблюдения правил Google Play список публичных P2P-узлов не встроен в приложение и загружается по вашему запросу.",
                            "In compliance with Google Play policies, public P2P node mirrors are not bundled in the binary and are loaded on-demand by user request."
                        )
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { showP2pConfigDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .testTag("p2p_config_button")
            ) {
                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(tr("Загрузить JSON узлов", "Load Node JSON"))
            }

            if (p2pModel.isConfigured) {
                OutlinedButton(
                    onClick = {
                        P2PTrackerConfig.resetConfig(context)
                        p2pModel = P2PTrackerConfig.loadConfig(context)
                        TorrentSearchService.updateMirrorsFromConfig(p2pModel)
                    },
                    modifier = Modifier.testTag("p2p_reset_config_button")
                ) {
                    Text(tr("Сбросить", "Reset"))
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        SettingToggleRow(
            title = tr("Прямой поиск по пиринговым узлам и агрегаторам", "Direct Search on P2P Nodes & Aggregators"),
            checked = settings.enableDirectRutorSearch,
            onCheckedChange = actions.onUpdateEnableDirectRutorSearch,
            testTag = "enable_direct_rutor_switch"
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        SettingToggleRow(
            title = tr("Прямой поиск по аниме-узлам (AniLibria / Nyaa)", "Direct Anime P2P Node Search (AniLibria / Nyaa)"),
            checked = settings.enableAnimeTrackerSearch,
            onCheckedChange = actions.onUpdateEnableAnimeTrackerSearch,
            testTag = "enable_anime_tracker_switch"
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        SettingToggleRow(
            title = tr("Безопасное разрешение доменов узлов через DNS-over-HTTPS (DoH)", "Secure Domain Resolution via DNS-over-HTTPS (DoH)"),
            checked = settings.enableTrackerDoh,
            onCheckedChange = actions.onUpdateEnableTrackerDoh,
            testTag = "enable_tracker_doh_switch"
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Text(
            text = tr("Пользовательский P2P / Torznab / JacRed индексатор", "Custom P2P / Torznab / JacRed Indexer"),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )

        OutlinedTextField(
            value = indexerUrlInput,
            onValueChange = { indexerUrlInput = it },
            label = { Text("URL Torznab / JacRed / API") },
            placeholder = { Text("http://192.168.1.100:9117") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("settings_indexer_url_input")
        )

        OutlinedTextField(
            value = indexerKeyInput,
            onValueChange = { indexerKeyInput = it },
            label = { Text(tr("API Key (если требуется)", "API Key (if required)")) },
            placeholder = { Text(tr("Ключ API индексатора", "Indexer API key")) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("settings_indexer_key_input")
        )

        if (searchTestResultText != null) {
            Surface(
                color = if (searchTestOk == true) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                } else {
                    MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = searchTestResultText.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    actions.onUpdateTorrentIndexerConfig(
                        indexerUrlInput.trim(),
                        indexerKeyInput.trim()
                    )
                    searchTestOk = true
                    searchTestResultText = if (isEnglish) {
                        "Indexer settings saved"
                    } else {
                        "Настройки индексатора сохранены"
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("settings_save_indexer_button")
            ) {
                Text(tr("Сохранить индексатор", "Save Indexer"))
            }

            FilledTonalButton(
                onClick = {
                    actions.onUpdateTorrentIndexerConfig(
                        indexerUrlInput.trim(),
                        indexerKeyInput.trim()
                    )
                    scope.launch {
                        isTestingSearch = true
                        val respResult = TorrentSearchService.searchAll(
                            rawQuery = "1080p",
                            indexerUrl = indexerUrlInput.trim(),
                            indexerApiKey = indexerKeyInput.trim(),
                            enableDirectRutor = settings.enableDirectRutorSearch,
                            enableAnimeNyaa = settings.enableAnimeTrackerSearch,
                            enableDoh = settings.enableTrackerDoh
                        )
                        isTestingSearch = false
                        respResult.fold(
                            onSuccess = { resp ->
                                if (resp.results.isNotEmpty()) {
                                    searchTestOk = true
                                    val srcSummary = resp.activeSources.joinToString(", ")
                                    searchTestResultText = if (isEnglish) {
                                        "Search works! Found ${resp.results.size} streams ($srcSummary)"
                                    } else {
                                        "Поиск работает! Найдено ${resp.results.size} P2P-потоков ($srcSummary)"
                                    }
                                } else {
                                    searchTestOk = false
                                    searchTestResultText = resp.warningMessage
                                        ?: if (isEnglish) "Sources returned 0 results" else "Источники вернули 0 результатов"
                                }
                            },
                            onFailure = { err ->
                                searchTestOk = false
                                searchTestResultText = err.message
                                    ?: if (isEnglish) "Network error" else "Сетевая ошибка"
                            }
                        )
                    }
                },
                enabled = !isTestingSearch,
                modifier = Modifier
                    .weight(1f)
                    .testTag("settings_test_search_button")
            ) {
                if (isTestingSearch) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Dns, contentDescription = null, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(tr("Тест поиска", "Test Search"))
            }
        }
    }

    if (showP2pConfigDialog) {
        val isPureOled = MaterialTheme.colorScheme.background.red < 0.01f &&
            MaterialTheme.colorScheme.background.green < 0.01f &&
            MaterialTheme.colorScheme.background.blue < 0.01f

        AlertDialog(
            onDismissRequest = { showP2pConfigDialog = false },
            containerColor = if (isPureOled) androidx.compose.ui.graphics.Color(0xFF050505) else MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = tr("Конфигурация P2P-узлов", "P2P Node Configuration"),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = tr(
                            "Вы можете загрузить стандартную конфигурацию публичных узлов или указать собственный URL внешнего JSON (GitHub RAW, личный сервер).",
                            "You can load standard public node configuration or specify a custom external JSON URL (GitHub RAW, personal server)."
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = remoteConfigUrl,
                        onValueChange = { remoteConfigUrl = it },
                        label = { Text("URL JSON-файла") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (p2pDialogMessage != null) {
                        Text(
                            text = p2pDialogMessage.orEmpty(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                val outcome = P2PTrackerConfig.saveConfig(context, P2PTrackerConfig.SAMPLE_INDEXER_JSON)
                                outcome.fold(
                                    onSuccess = { model ->
                                        p2pModel = model
                                        TorrentSearchService.updateMirrorsFromConfig(model)
                                        p2pDialogMessage = if (isEnglish) "Loaded ${model.totalNodes} nodes" else "Загружено ${model.totalNodes} узлов"
                                    },
                                    onFailure = { p2pDialogMessage = it.message }
                                )
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(tr("Стандартный список", "Standard List"), style = MaterialTheme.typography.labelSmall)
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    isLoadingRemote = true
                                    val res = P2PTrackerConfig.fetchRemoteConfig(context, remoteConfigUrl, settings.enableTrackerDoh)
                                    isLoadingRemote = false
                                    res.fold(
                                        onSuccess = { model ->
                                            p2pModel = model
                                            TorrentSearchService.updateMirrorsFromConfig(model)
                                            p2pDialogMessage = if (isEnglish) "Downloaded ${model.totalNodes} nodes" else "Скачано ${model.totalNodes} узлов"
                                        },
                                        onFailure = { p2pDialogMessage = it.message }
                                    )
                                }
                            },
                            enabled = !isLoadingRemote,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (isLoadingRemote) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                            } else {
                                Text(tr("Загрузить с URL", "Download URL"), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showP2pConfigDialog = false }) {
                    Text(tr("Готово", "Done"), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

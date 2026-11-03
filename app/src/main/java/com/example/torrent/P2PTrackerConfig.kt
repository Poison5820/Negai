package com.example.torrent

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Clean P2P Indexer / Tracker configuration management compliant with Google Play Store guidelines.
 * No hardcoded commercial tracker mirrors are embedded into binary APK code.
 * The configuration is loaded on-demand by the user via custom URL or JSON import.
 */
data class P2PTrackerModel(
    val jacredMirrors: List<String> = emptyList(),
    val rutorMirrors: List<String> = emptyList(),
    val nyaaMirrors: List<String> = emptyList(),
    val anilibriaMirrors: List<String> = emptyList(),
    val apibayMirrors: List<String> = emptyList(),
    val sourceDescription: String = ""
) {
    val totalNodes: Int
        get() = jacredMirrors.size + rutorMirrors.size + nyaaMirrors.size + anilibriaMirrors.size + apibayMirrors.size

    val isConfigured: Boolean
        get() = totalNodes > 0
}

object P2PTrackerConfig {

    private const val CONFIG_FILE_NAME = "p2p_trackers.json"
    private var cachedModel: P2PTrackerModel? = null

    /**
     * Standard sample configuration used strictly when the user explicitly triggers
     * "Загрузить конфигурацию узлов" with confirmation and terms acceptance.
     */
    val SAMPLE_INDEXER_JSON = """
    {
      "version": 1,
      "description": "Пользовательские P2P-индексаторы (JacRed, RuTracker, Rutor, Nyaa, AniLibria)",
      "jacred_mirrors": [
        "https://jacred.xyz",
        "https://jacred.pro",
        "https://jac-red.ru",
        "https://jr.maxvol.pro",
        "https://jacred.viewbox.dev"
      ],
      "rutor_mirrors": [
        "https://rutor.info",
        "https://rutor.is",
        "https://6tor.org"
      ],
      "nyaa_mirrors": [
        "https://nyaa.si",
        "https://nyaa.iss.one",
        "https://nyaa.land"
      ],
      "anilibria_mirrors": [
        "https://anilibria.top",
        "https://api.anilibria.tv"
      ],
      "apibay_mirrors": [
        "https://apibay.org"
      ]
    }
    """.trimIndent()

    @Synchronized
    fun loadConfig(context: Context): P2PTrackerModel {
        cachedModel?.let { return it }

        val file = File(context.filesDir, CONFIG_FILE_NAME)
        if (!file.exists()) {
            val empty = P2PTrackerModel()
            cachedModel = empty
            return empty
        }

        return try {
            val content = file.readText()
            val parsed = parseJson(content)
            cachedModel = parsed
            parsed
        } catch (_: Exception) {
            val empty = P2PTrackerModel()
            cachedModel = empty
            empty
        }
    }

    @Synchronized
    fun saveConfig(context: Context, jsonText: String): Result<P2PTrackerModel> {
        return try {
            val parsed = parseJson(jsonText)
            val file = File(context.filesDir, CONFIG_FILE_NAME)
            file.writeText(jsonText)
            cachedModel = parsed
            Result.success(parsed)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Synchronized
    fun resetConfig(context: Context) {
        val file = File(context.filesDir, CONFIG_FILE_NAME)
        if (file.exists()) {
            file.delete()
        }
        cachedModel = P2PTrackerModel()
    }

    suspend fun fetchRemoteConfig(
        context: Context,
        rawUrl: String,
        enableDoh: Boolean
    ): Result<P2PTrackerModel> = withContext(Dispatchers.IO) {
        val cleanUrl = rawUrl.trim()
        if (cleanUrl.isEmpty() || (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://"))) {
            return@withContext Result.failure(IllegalArgumentException("Некорректный URL (требуется http:// или https://)"))
        }

        try {
            val client = ResilientTrackerNetwork.client(enableDoh)
            val request = Request.Builder()
                .url(cleanUrl)
                .header("User-Agent", "Negai-P2P-Client/3.4.6")
                .header("Accept", "application/json, text/plain")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext Result.failure(IllegalStateException("HTTP ${response.code}: ${response.message}"))
                }
                val bodyText = response.body?.string().orEmpty()
                saveConfig(context, bodyText)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseJson(jsonStr: String): P2PTrackerModel {
        val obj = JSONObject(jsonStr)
        fun parseStringList(key: String): List<String> {
            val arr = obj.optJSONArray(key) ?: return emptyList()
            val list = mutableListOf<String>()
            for (i in 0 until arr.length()) {
                val str = arr.optString(i).trim()
                if (str.isNotEmpty() && (str.startsWith("http://") || str.startsWith("https://"))) {
                    list.add(str.removeSuffix("/"))
                }
            }
            return list.distinct()
        }

        return P2PTrackerModel(
            jacredMirrors = parseStringList("jacred_mirrors"),
            rutorMirrors = parseStringList("rutor_mirrors"),
            nyaaMirrors = parseStringList("nyaa_mirrors"),
            anilibriaMirrors = parseStringList("anilibria_mirrors"),
            apibayMirrors = parseStringList("apibay_mirrors"),
            sourceDescription = obj.optString("description", "Пользовательский файл конфигурации")
        )
    }

    fun getJacredMirrors(context: Context): List<String> = loadConfig(context).jacredMirrors
    fun getRutorMirrors(context: Context): List<String> = loadConfig(context).rutorMirrors
    fun getNyaaMirrors(context: Context): List<String> = loadConfig(context).nyaaMirrors
    fun getAnilibriaMirrors(context: Context): List<String> = loadConfig(context).anilibriaMirrors
    fun getApibayMirrors(context: Context): List<String> = loadConfig(context).apibayMirrors
}

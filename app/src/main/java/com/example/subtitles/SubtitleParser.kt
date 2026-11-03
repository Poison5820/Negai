package com.example.subtitles

import com.example.model.SubtitleFormat
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction
import java.util.Locale

object SubtitleParser {

    private val SRT_TIMING_REGEX = Regex(
        """(\d{1,2}):(\d{2}):(\d{2})[,.](\d{1,3})\s*-->\s*(\d{1,2}):(\d{2}):(\d{2})[,.](\d{1,3})"""
    )
    private val ASS_OVERRIDE_BLOCK_REGEX = Regex("""\{([^}]*)\}""")
    private val ASS_ALIGNMENT_REGEX = Regex("""\\an([1-9])""")
    private val ASS_FONT_NAME_REGEX = Regex("""\\fn([^\\}]+)""")
    private val ASS_FONT_SIZE_REGEX = Regex("""\\fs(\d+(?:\.\d+)?)""")
    private val ASS_PRIMARY_COLOR_REGEX = Regex("""\\(?:1?c)&?[Hh]?([0-9A-Fa-f]{6,8})&?""")
    private val ASS_OUTLINE_COLOR_REGEX = Regex("""\\3c&?[Hh]?([0-9A-Fa-f]{6,8})&?""")
    private val HTML_FONT_COLOR_REGEX = Regex("""(?i)<font[^>]*color\s*=\s*["']?#?([0-9a-fA-F]{6})["']?[^>]*>""")
    private val HTML_FONT_FACE_REGEX = Regex("""(?i)<font[^>]*face\s*=\s*["']([^"']+)["'][^>]*>""")
    private val HTML_TAG_REGEX = Regex("""<[^>]+>""")

    private data class AssStyleDef(
        val name: String,
        val fontName: String?,
        val fontSize: Float?,
        val alignment: SubtitleAlignment,
        val bold: Boolean,
        val italic: Boolean,
        val primaryColorArgb: Long?,
        val outlineColorArgb: Long?
    )

    /**
     * Decodes raw subtitle file bytes supporting:
     * - UTF-8, UTF-16LE, UTF-16BE, UTF-32LE, UTF-32BE BOMs
     * - BOM-less UTF-16LE / UTF-16BE detection
     * - Strict UTF-8 validation
     * - Statistical Cyrillic & international charset scoring (windows-1251, KOI8-R, ISO-8859-5, IBM866, Windows-1252)
     */
    fun decodeSubtitleBytes(bytes: ByteArray): String {
        if (bytes.isEmpty()) return ""
        // 1. Check explicit BOMs
        if (bytes.size >= 4) {
            if (bytes[0] == 0xFF.toByte() && bytes[1] == 0xFE.toByte() &&
                bytes[2] == 0x00.toByte() && bytes[3] == 0x00.toByte()
            ) {
                val utf32Le = runCatching { Charset.forName("UTF-32LE") }.getOrNull()
                if (utf32Le != null) return String(bytes, 4, bytes.size - 4, utf32Le)
            }
            if (bytes[0] == 0x00.toByte() && bytes[1] == 0x00.toByte() &&
                bytes[2] == 0xFE.toByte() && bytes[3] == 0xFF.toByte()
            ) {
                val utf32Be = runCatching { Charset.forName("UTF-32BE") }.getOrNull()
                if (utf32Be != null) return String(bytes, 4, bytes.size - 4, utf32Be)
            }
        }
        if (bytes.size >= 3 &&
            bytes[0] == 0xEF.toByte() &&
            bytes[1] == 0xBB.toByte() &&
            bytes[2] == 0xBF.toByte()
        ) {
            return String(bytes, 3, bytes.size - 3, Charsets.UTF_8)
        }
        if (bytes.size >= 2 && bytes[0] == 0xFF.toByte() && bytes[1] == 0xFE.toByte()) {
            return String(bytes, 2, bytes.size - 2, Charsets.UTF_16LE)
        }
        if (bytes.size >= 2 && bytes[0] == 0xFE.toByte() && bytes[1] == 0xFF.toByte()) {
            return String(bytes, 2, bytes.size - 2, Charsets.UTF_16BE)
        }

        // 2. Check BOM-less UTF-16LE / UTF-16BE via null-byte parity in first 512 bytes
        val sampleLen = minOf(bytes.size, 512)
        if (sampleLen >= 16) {
            var evenZeros = 0
            var oddZeros = 0
            val pairs = sampleLen / 2
            for (idx in 0 until pairs) {
                if (bytes[idx * 2] == 0.toByte()) evenZeros++
                if (bytes[idx * 2 + 1] == 0.toByte()) oddZeros++
            }
            if (oddZeros > pairs / 3 && evenZeros < pairs / 10) {
                return String(bytes, Charsets.UTF_16LE)
            }
            if (evenZeros > pairs / 3 && oddZeros < pairs / 10) {
                return String(bytes, Charsets.UTF_16BE)
            }
        }

        // 3. Try strict UTF-8
        try {
            val decoder = Charsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
            return decoder.decode(ByteBuffer.wrap(bytes)).toString()
        } catch (_: Exception) {
            // Not valid UTF-8; fall through to statistical charset detection
        }

        // 4. Score candidate single-byte / legacy encodings (Windows-1251, KOI8-R, ISO-8859-5, IBM866, Windows-1252)
        val candidateCharsets = listOf(
            "windows-1251",
            "KOI8-R",
            "ISO-8859-5",
            "IBM866",
            "windows-1252",
            "ISO-8859-1"
        )
        var bestText: String? = null
        var bestScore = Int.MIN_VALUE

        val previewBytes = if (bytes.size > 8192) bytes.copyOfRange(0, 8192) else bytes
        for (charsetName in candidateCharsets) {
            val cs = runCatching { Charset.forName(charsetName) }.getOrNull() ?: continue
            val preview = String(previewBytes, cs)
            val score = scoreDecodedSubtitleText(preview, charsetName)
            if (score > bestScore) {
                bestScore = score
                bestText = if (previewBytes.size == bytes.size) preview else String(bytes, cs)
            }
        }

        return bestText ?: String(bytes, Charsets.UTF_8)
    }

    private fun scoreDecodedSubtitleText(text: String, charsetName: String): Int {
        var score = 0
        val commonRussianLower = "аеиоуыэюябвгджзйклмнпрстфхцчшщъь"
        val commonRussianUpper = "АЕИОУЫЭЮЯБВГДЖЗЙКЛМНПРСТФХЦЧШЩЪЬ"
        var lowerCount = 0
        var upperCount = 0
        for (ch in text) {
            when {
                ch in commonRussianLower -> {
                    lowerCount++
                    score += 4
                }
                ch in commonRussianUpper -> {
                    upperCount++
                    score += 2
                }
                ch == '\uFFFD' -> score -= 25
                ch.code in 0x0080..0x009F -> score -= 12
                // Box drawing / math symbols often appear when Win-1251 is misread as IBM866/KOI8-R
                ch.code in 0x2500..0x257F -> score -= 10
            }
        }
        // Natural Russian text has significantly more lowercase Cyrillic than uppercase
        if (lowerCount > upperCount * 2 && lowerCount > 8) {
            score += lowerCount * 2
        } else if (upperCount > lowerCount * 2 && upperCount > 12) {
            // Inverted case symptom of KOI8-R vs Windows-1251 mismatch
            score -= (upperCount - lowerCount) * 3
        }
        if (charsetName.equals("windows-1251", ignoreCase = true)) {
            score += 5 // Slight tie-breaker preference for standard Windows-1251
        }
        return score
    }

    fun parse(
        rawContent: String,
        formatHint: SubtitleFormat? = null,
        title: String = "Subtitles"
    ): ParsedSubtitleDocument {
        val normalized = rawContent.replace("\r\n", "\n").replace('\r', '\n').trimStart('\uFEFF')
        val detectedFormat = when {
            normalized.contains("[Events]", ignoreCase = true) ||
                normalized.contains("[Script Info]", ignoreCase = true) ||
                normalized.contains("[V4+ Styles]", ignoreCase = true) ||
                normalized.contains("[V4 Styles]", ignoreCase = true) ||
                formatHint == SubtitleFormat.ASS -> SubtitleFormat.ASS
            else -> SubtitleFormat.SRT
        }

        val cues = when (detectedFormat) {
            SubtitleFormat.ASS -> parseAss(normalized)
            SubtitleFormat.SRT, SubtitleFormat.VTT -> parseSrtOrVtt(normalized)
        }

        return ParsedSubtitleDocument(
            title = title,
            cues = cues.sortedBy { it.startMs }
        )
    }

    fun parseSrtOrVtt(content: String): List<SubtitleCue> {
        val result = mutableListOf<SubtitleCue>()
        val lines = content.split('\n')
        var i = 0
        while (i < lines.size) {
            val line = lines[i].trim()
            if (line.isEmpty() || line.equals("WEBVTT", ignoreCase = true)) {
                i++
                continue
            }

            var timingMatch = SRT_TIMING_REGEX.find(line)
            if (timingMatch == null && i + 1 < lines.size) {
                val nextLine = lines[i + 1].trim()
                val nextMatch = SRT_TIMING_REGEX.find(nextLine)
                if (nextMatch != null) {
                    timingMatch = nextMatch
                    i++
                }
            }

            if (timingMatch != null) {
                val startMs = parseSrtTime(
                    timingMatch.groupValues[1],
                    timingMatch.groupValues[2],
                    timingMatch.groupValues[3],
                    timingMatch.groupValues[4]
                )
                val endMs = parseSrtTime(
                    timingMatch.groupValues[5],
                    timingMatch.groupValues[6],
                    timingMatch.groupValues[7],
                    timingMatch.groupValues[8]
                )
                i++
                val textLines = mutableListOf<String>()
                while (i < lines.size && lines[i].trim().isNotEmpty()) {
                    textLines.add(lines[i].trim())
                    i++
                }

                val rawBlock = textLines.joinToString("\n")
                if (rawBlock.isNotBlank() && endMs > startMs) {
                    val isItalic = rawBlock.contains("<i>", ignoreCase = true) || rawBlock.contains("{\\i1}")
                    val isBold = rawBlock.contains("<b>", ignoreCase = true) || rawBlock.contains("{\\b1}")
                    val alignCode = ASS_ALIGNMENT_REGEX.find(rawBlock)?.groupValues?.get(1)?.toIntOrNull()
                    val alignment = if (alignCode != null) {
                        SubtitleAlignment.fromAssNumpad(alignCode)
                    } else {
                        SubtitleAlignment.BOTTOM_CENTER
                    }
                    val htmlColorArgb = HTML_FONT_COLOR_REGEX.find(rawBlock)?.groupValues?.getOrNull(1)
                        ?.toLongOrNull(16)?.let { 0xFF000000L or it }
                    val assColorArgb = ASS_PRIMARY_COLOR_REGEX.find(rawBlock)?.groupValues?.getOrNull(1)
                        ?.let { parseAssColor("&H$it") }
                    val fontFace = HTML_FONT_FACE_REGEX.find(rawBlock)?.groupValues?.getOrNull(1)?.trim()
                        ?: ASS_FONT_NAME_REGEX.find(rawBlock)?.groupValues?.getOrNull(1)?.trim()

                    val cleaned = rawBlock
                        .replace(ASS_OVERRIDE_BLOCK_REGEX, "")
                        .replace(HTML_TAG_REGEX, "")
                        .replace("&nbsp;", " ")
                        .replace("&amp;", "&")
                        .replace("&lt;", "<")
                        .replace("&gt;", ">")
                        .trim()

                    if (cleaned.isNotEmpty()) {
                        val cueColor = assColorArgb ?: htmlColorArgb
                        result.add(
                            SubtitleCue(
                                startMs = startMs,
                                endMs = endMs,
                                text = cleaned,
                                alignment = alignment,
                                isItalic = isItalic,
                                isBold = isBold,
                                assStyleColorArgb = cueColor,
                                fontFamilyName = fontFace,
                                hasExplicitInlineColor = cueColor != null
                            )
                        )
                    }
                }
            } else {
                i++
            }
        }
        return result
    }

    fun parseAss(content: String): List<SubtitleCue> {
        val cues = mutableListOf<SubtitleCue>()
        val styles = mutableMapOf<String, AssStyleDef>()
        val embeddedFontNames = mutableSetOf<String>()

        var currentSection = ""
        var styleColumns: List<String> = emptyList()
        var eventColumns: List<String> = emptyList()
        var playResY = 0f

        for (rawLine in content.lineSequence()) {
            val line = rawLine.trim()
            if (line.isEmpty() || line.startsWith(";")) continue

            if (line.startsWith("[") && line.endsWith("]")) {
                currentSection = line.substring(1, line.length - 1).lowercase(Locale.ROOT)
                continue
            }

            when (currentSection) {
                "script info" -> {
                    if (line.startsWith("PlayResY:", ignoreCase = true)) {
                        playResY = line.substringAfter(':').trim().toFloatOrNull() ?: 0f
                    }
                }

                "fonts" -> {
                    if (line.startsWith("fontname:", ignoreCase = true)) {
                        val fn = line.substringAfter(':').trim()
                        if (fn.isNotEmpty()) embeddedFontNames.add(fn)
                    }
                }

                "v4+ styles", "v4 styles" -> {
                    if (line.startsWith("Format:", ignoreCase = true)) {
                        styleColumns = line.substringAfter(':')
                            .split(',')
                            .map { it.trim().lowercase(Locale.ROOT) }
                    } else if (line.startsWith("Style:", ignoreCase = true) && styleColumns.isNotEmpty()) {
                        val values = line.substringAfter(':').split(',', limit = styleColumns.size)
                            .map { it.trim() }
                        val map = styleColumns.zip(values).toMap()
                        val name = map["name"]?.trim()?.trimStart('*') ?: continue
                        val fontName = map["fontname"]?.trim()?.trimStart('@')?.takeIf { it.isNotEmpty() }
                        val fontSize = map["fontsize"]?.toFloatOrNull()
                        val alignRaw = map["alignment"]?.toIntOrNull() ?: 2
                        val alignment = if (currentSection == "v4 styles") {
                            when (alignRaw) {
                                1 -> SubtitleAlignment.BOTTOM_LEFT
                                2 -> SubtitleAlignment.BOTTOM_CENTER
                                3 -> SubtitleAlignment.BOTTOM_RIGHT
                                5 -> SubtitleAlignment.TOP_LEFT
                                6 -> SubtitleAlignment.TOP_CENTER
                                7 -> SubtitleAlignment.TOP_RIGHT
                                9 -> SubtitleAlignment.MIDDLE_LEFT
                                10 -> SubtitleAlignment.MIDDLE_CENTER
                                11 -> SubtitleAlignment.MIDDLE_RIGHT
                                else -> SubtitleAlignment.BOTTOM_CENTER
                            }
                        } else {
                            SubtitleAlignment.fromAssNumpad(alignRaw)
                        }
                        val bold = map["bold"]?.let { it == "-1" || it == "1" || (it.toIntOrNull() ?: 0) >= 600 } ?: false
                        val italic = map["italic"]?.let { it == "-1" || it == "1" } ?: false
                        val primaryColor = map["primarycolour"]?.let { parseAssColor(it) }
                        val outlineColor = map["outlinecolour"]?.let { parseAssColor(it) }
                        styles[name.lowercase(Locale.ROOT)] = AssStyleDef(
                            name = name,
                            fontName = fontName,
                            fontSize = fontSize,
                            alignment = alignment,
                            bold = bold,
                            italic = italic,
                            primaryColorArgb = primaryColor,
                            outlineColorArgb = outlineColor
                        )
                    }
                }

                "events" -> {
                    if (line.startsWith("Format:", ignoreCase = true)) {
                        eventColumns = line.substringAfter(':')
                            .split(',')
                            .map { it.trim().lowercase(Locale.ROOT) }
                    } else if (line.startsWith("Dialogue:", ignoreCase = true)) {
                        val cols = eventColumns.ifEmpty {
                            listOf(
                                "layer", "start", "end", "style", "name",
                                "marginl", "marginr", "marginv", "effect", "text"
                            )
                        }
                        val payload = line.substringAfter(':')
                        val parts = payload.split(',', limit = cols.size)
                        if (parts.size < cols.size) continue

                        val startIdx = cols.indexOf("start")
                        val endIdx = cols.indexOf("end")
                        val styleIdx = cols.indexOf("style")
                        val textIdx = cols.indexOf("text")
                        if (startIdx < 0 || endIdx < 0 || textIdx < 0) continue

                        val startMs = parseAssTime(parts[startIdx].trim()) ?: continue
                        val endMs = parseAssTime(parts[endIdx].trim()) ?: continue
                        if (endMs <= startMs) continue

                        val styleName = if (styleIdx >= 0) {
                            parts[styleIdx].trim().trimStart('*').lowercase(Locale.ROOT)
                        } else {
                            "default"
                        }
                        val styleDef = styles[styleName] ?: styles["default"]
                        val rawText = parts[textIdx]

                        // Skip pure ASS vector polygon drawings ({\p1}...{\p0})
                        if (rawText.contains("\\p1") || rawText.contains("\\p2") || rawText.contains("\\p4")) {
                            continue
                        }

                        val overrideAlign = ASS_ALIGNMENT_REGEX.find(rawText)
                            ?.groupValues?.get(1)?.toIntOrNull()
                            ?.let { SubtitleAlignment.fromAssNumpad(it) }

                        val inlineFontName = ASS_FONT_NAME_REGEX.find(rawText)
                            ?.groupValues?.getOrNull(1)?.trim()?.trimStart('@')?.takeIf { it.isNotEmpty() }
                        val effectiveFontName = inlineFontName ?: styleDef?.fontName

                        val inlineFontSize = ASS_FONT_SIZE_REGEX.find(rawText)
                            ?.groupValues?.getOrNull(1)?.toFloatOrNull()
                        val effectiveAssFontSize = inlineFontSize ?: styleDef?.fontSize
                        val referenceFontSize = styles["default"]?.fontSize
                            ?: if (playResY > 0f) (playResY * 0.055f) else 48f
                        val fontSizeScale = if (effectiveAssFontSize != null && referenceFontSize > 0f) {
                            (effectiveAssFontSize / referenceFontSize).coerceIn(0.72f, 1.45f)
                        } else {
                            1.0f
                        }

                        val inlinePrimaryColor = ASS_PRIMARY_COLOR_REGEX.find(rawText)
                            ?.groupValues?.getOrNull(1)?.let { parseAssColor("&H$it") }
                        val inlineOutlineColor = ASS_OUTLINE_COLOR_REGEX.find(rawText)
                            ?.groupValues?.getOrNull(1)?.let { parseAssColor("&H$it") }

                        val isItalic = rawText.contains("\\i1") || (styleDef?.italic == true && !rawText.contains("\\i0"))
                        val isBold = rawText.contains("\\b1") ||
                            rawText.contains("\\b700") ||
                            rawText.contains("\\b800") ||
                            (styleDef?.bold == true && !rawText.contains("\\b0"))

                        val cleanText = rawText
                            .replace(ASS_OVERRIDE_BLOCK_REGEX, "")
                            .replace("\\N", "\n")
                            .replace("\\n", "\n")
                            .replace("\\h", "\u00A0")
                            .trim()

                        if (cleanText.isNotEmpty()) {
                            cues.add(
                                SubtitleCue(
                                    startMs = startMs,
                                    endMs = endMs,
                                    text = cleanText,
                                    alignment = overrideAlign ?: styleDef?.alignment ?: SubtitleAlignment.BOTTOM_CENTER,
                                    isItalic = isItalic,
                                    isBold = isBold,
                                    assStyleColorArgb = inlinePrimaryColor ?: styleDef?.primaryColorArgb,
                                    assOutlineColorArgb = inlineOutlineColor ?: styleDef?.outlineColorArgb,
                                    fontFamilyName = effectiveFontName,
                                    fontSizeScale = fontSizeScale,
                                    hasExplicitInlineColor = inlinePrimaryColor != null
                                )
                            )
                        }
                    }
                }
            }
        }
        return cues
    }

    private fun parseSrtTime(h: String, m: String, s: String, msRaw: String): Long {
        val hours = h.toLongOrNull() ?: 0L
        val mins = m.toLongOrNull() ?: 0L
        val secs = s.toLongOrNull() ?: 0L
        val millis = msRaw.padEnd(3, '0').take(3).toLongOrNull() ?: 0L
        return (((hours * 60L + mins) * 60L) + secs) * 1000L + millis
    }

    private fun parseAssTime(value: String): Long? {
        // Format: H:MM:SS.cc (centiseconds) or H:MM:SS.mmm
        val match = Regex("""^(\d+):(\d{2}):(\d{2})[.:](\d{1,3})$""").find(value) ?: return null
        val hours = match.groupValues[1].toLongOrNull() ?: return null
        val mins = match.groupValues[2].toLongOrNull() ?: return null
        val secs = match.groupValues[3].toLongOrNull() ?: return null
        val fracStr = match.groupValues[4]
        val millis = when (fracStr.length) {
            1 -> (fracStr.toLongOrNull() ?: 0L) * 100L
            2 -> (fracStr.toLongOrNull() ?: 0L) * 10L
            else -> fracStr.take(3).toLongOrNull() ?: 0L
        }
        return (((hours * 60L + mins) * 60L) + secs) * 1000L + millis
    }

    private fun parseAssColor(raw: String): Long? {
        // ASS format: &HAABBGGRR or &HBBGGRR (where AA=00 is opaque, FF is transparent)
        val hex = raw.trim().removePrefix("&H").removePrefix("&h").removeSuffix("&")
        val parsed = hex.toLongOrNull(16) ?: return null
        val b: Long
        val g: Long
        val r: Long
        val alpha: Long
        if (hex.length >= 8) {
            val assAlpha = (parsed shr 24) and 0xFFL
            alpha = (255L - assAlpha).coerceIn(40L, 255L)
            b = (parsed shr 16) and 0xFFL
            g = (parsed shr 8) and 0xFFL
            r = parsed and 0xFFL
        } else {
            alpha = 0xFFL
            b = (parsed shr 16) and 0xFFL
            g = (parsed shr 8) and 0xFFL
            r = parsed and 0xFFL
        }
        return (alpha shl 24) or (r shl 16) or (g shl 8) or b
    }
}

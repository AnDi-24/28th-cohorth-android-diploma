package ru.practicum.android.diploma.util.parser

private const val ABOUT_COMPANY = "О компании"
private const val FILTER_LINE_LENGTH = 30
private const val MAX_SECTION_TITLE_LENGTH = 50

private val FILTER_PHRASES = listOf(
    "о нас",
    ABOUT_COMPANY,
    "компания:",
    "компания",
    "от компании",
    "информация о компании",
    "наша компания"
)

object VacancyDescriptionParser {

    fun parseDescription(description: String): List<DescriptionSection> {
        val lines = preprocessDescription(description)
        val (sections, otherLines) = parseLinesIntoSections(lines)

        return buildFinalSections(sections, otherLines)
    }

    private fun preprocessDescription(description: String): List<String> {
        return description.split("\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }
    }

    private fun parseLinesIntoSections(lines: List<String>): Pair<List<DescriptionSection>, MutableList<String>> {
        val sections = mutableListOf<DescriptionSection>()
        val otherLines = mutableListOf<String>()
        var currentTitle: String? = null
        val currentItems = mutableListOf<String>()

        for (line in lines) {
            val sectionTitle = detectSectionTitle(line)

            when {
                sectionTitle != null -> {
                    addCurrentSection(sections, currentTitle, currentItems)
                    currentTitle = sectionTitle
                }

                currentTitle != null && line.length > 2 -> {
                    addToCurrentSection(currentItems, line)
                }

                line.length > 2 -> {
                    addToOtherLines(otherLines, line)
                }
            }
        }

        addCurrentSection(sections, currentTitle, currentItems)

        return Pair(sections, otherLines)
    }

    private fun detectSectionTitle(line: String): String? {
        val lowerLine = line.lowercase()

        return when {
            lowerLine.contains("обязанности") && line.length < MAX_SECTION_TITLE_LENGTH -> "Обязанности"
            lowerLine.contains("требования") && line.length < MAX_SECTION_TITLE_LENGTH -> "Требования"
            lowerLine.contains("условия") && line.length < MAX_SECTION_TITLE_LENGTH -> "Условия"
            else -> null
        }
    }

    private fun addToCurrentSection(currentItems: MutableList<String>, line: String) {
        if (shouldFilterLine(line)) return

        val cleanedText = cleanText(line)
        if (cleanedText.isNotBlank()) {
            currentItems.add(cleanedText)
        }
    }

    private fun addToOtherLines(otherLines: MutableList<String>, line: String) {
        if (shouldFilterLine(line)) return

        val cleanedText = cleanText(line)
        if (cleanedText.isNotBlank()) {
            otherLines.add(cleanedText)
        }
    }

    private fun shouldFilterLine(line: String): Boolean {
        val lowerLine = line.lowercase()
        return FILTER_PHRASES.any { phrase ->
            (lowerLine == phrase || lowerLine == "$phrase:") && line.length < FILTER_LINE_LENGTH
        }
    }

    private fun cleanText(text: String): String {
        var cleaned = text.replace(Regex("^[•\\-\\*\\d+\\.\\s]+"), "").trim()

        FILTER_PHRASES.forEach { phrase ->
            val regex = Regex("\\b${Regex.escape(phrase)}\\b[\\s:]*", RegexOption.IGNORE_CASE)
            cleaned = cleaned.replace(regex, "").trim()
        }

        return cleaned
    }

    private fun addCurrentSection(
        sections: MutableList<DescriptionSection>,
        currentTitle: String?,
        currentItems: MutableList<String>
    ) {
        if (currentTitle != null && currentItems.isNotEmpty()) {
            sections.add(DescriptionSection(currentTitle, currentItems.toList()))
            currentItems.clear()
        }
    }

    private fun buildFinalSections(
        sections: List<DescriptionSection>,
        otherLines: MutableList<String>
    ): List<DescriptionSection> {
        val finalSections = sections.toMutableList()

        if (otherLines.isNotEmpty()) {
            val companyText = otherLines.joinToString(" ")
            finalSections.add(DescriptionSection(ABOUT_COMPANY, listOf(companyText)))
        }

        addMissingStandardSections(finalSections)

        return finalSections
    }

    private fun addMissingStandardSections(sections: MutableList<DescriptionSection>) {
        val requiredTitles = listOf("Обязанности", "Требования", "Условия")
        val existingStandardTitles = sections.filterNot { it.title == ABOUT_COMPANY }.map { it.title }

        requiredTitles.forEach { requiredTitle ->
            if (!existingStandardTitles.contains(requiredTitle)) {
                sections.add(DescriptionSection(requiredTitle, listOf("Информация не указана")))
            }
        }
    }
}

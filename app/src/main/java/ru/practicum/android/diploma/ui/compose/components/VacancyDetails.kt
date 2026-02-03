package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.domain.network.models.Contacts
import ru.practicum.android.diploma.domain.network.models.Salary
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel

private const val FIFTY = 50
private const val THIRTY = 30
private const val ABOUT_COMPANY = "О компании"

@Composable
fun VacancyDetails(
    vacancy: VacancyDetailsModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        val formattedVacancyName = formatVacancyName(
            vacancyName = vacancy.name,
            companyName = vacancy.employer?.name
        )
        item {
            // 1. Заголовок с отформатированным названием вакансии
            Text(
                text = formattedVacancyName,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
            // 2. Зарплатная вилка
            vacancy.salary?.let { salary ->
                Text(
                    text = formatSalaryForDetails(salary),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 3. Блок с логотипом компании, названием и городом
        item {
            CompanyInfoCard(vacancy)
        }

        // 4. Требуемый опыт, График работы, Тип занятости
        item {
            val experienceText = vacancy.experience?.name ?: "Не указано"
            val scheduleText = vacancy.schedule?.name ?: "График не указан"
            val employmentText = vacancy.employment?.name ?: "Занятость не указана"

            RequiredExperienceSection(
                experience = experienceText,
                schedule = scheduleText,
                employment = employmentText
            )
        }

        // 5. Описание вакансии
        item {
            vacancy.description?.let { description ->
                DescriptionSection(description)
            }
        }

        // 6. Навыки
        item {
            if (!vacancy.skills.isNullOrEmpty()) {
                SkillsSection(vacancy.skills)
            }
        }

        // 7. Контакты
        item {
            vacancy.contacts?.let { contacts ->
                ContactsSection(contacts)
            }
        }
    }
}

// Вспомогательные функции и компоненты

// Серая карточка с лого, названием компании и городом
@Composable
private fun CompanyInfoCard(vacancy: VacancyDetailsModel) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                EmployerLogoGlide(
                    logoUrl = vacancy.employer?.logo,
                    modifier = Modifier.size(48.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = vacancy.employer?.name ?: "Компания не указана",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = vacancy.area?.name ?: "Город не указан",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun RequiredExperienceSection(
    experience: String,
    schedule: String,
    employment: String
) {
    Column {
        Text(
            text = "Требуемый опыт",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = experience,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "$schedule, $employment",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun ContactsSection(contacts: Contacts) {
    val viewModel: VacancyDetailsViewModel = koinViewModel()
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Контакты",
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            contacts.name?.let { name ->
                ContactItem(
                    text = name
                )
            }

            contacts.email?.let { email ->
                Box(
                    modifier = Modifier.clickable(onClick = { viewModel.emailTo(context, email) })
                ) {
                    ContactItem(
                        text = email
                    )
                }
            }

            contacts.phones?.forEach { phone ->
                Box(
                    modifier = Modifier.clickable(onClick = { viewModel.callTo(context, phone.formatted) })
                ) {
                    ContactItem(
                        text = phone.formatted ?: ""
                    )
                }

            }
        }
    }
}

@Composable
private fun ContactItem(
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SkillsSection(skills: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Ключевые навыки",
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            skills.forEach { skill ->
                BulletListItem(text = skill)
            }
        }
    }
}

@Composable
private fun DescriptionSection(description: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Описание вакансии",
            style = MaterialTheme.typography.titleMedium
        )

        // Парсим описание на секции
        val sections = parseDescription(description)

        // Если секций нет, показываем информацию об отсутствии данных
        if (sections.isEmpty()) {
            Text(
                text = "Информация не указана",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // Отображаем все секции
            sections.forEach { section ->
                if (section.title == ABOUT_COMPANY) {
                    // Для секции "О компании" отображаем как абзацы
                    CompanyInfoSubsection(
                        title = section.title,
                        paragraphs = section.items
                    )
                } else {
                    // Для стандартных секций отображаем как маркированный список
                    DescriptionSubsection(
                        title = section.title,
                        items = section.items
                    )
                }
            }

            // Добавляем заглушки для отсутствующих стандартных секций
            val requiredTitles = listOf("Обязанности", "Требования", "Условия")
            val existingStandardTitles = sections.filterNot { it.title == ABOUT_COMPANY }.map { it.title }

            requiredTitles.forEach { requiredTitle ->
                if (!existingStandardTitles.contains(requiredTitle)) {
                    DescriptionSubsection(
                        title = requiredTitle,
                        items = listOf("Информация не указана")
                    )
                }
            }
        }
    }
}

@Composable
private fun CompanyInfoSubsection(
    title: String,
    paragraphs: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            paragraphs.forEach { paragraph ->
                Text(
                    text = paragraph,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun DescriptionSubsection(
    title: String,
    items: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.forEach { item ->
                BulletListItem(text = item)
            }
        }
    }
}

@Composable
private fun BulletListItem(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(3.dp)
                .offset(y = 8.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape
                )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun formatSalaryForDetails(salary: Salary): String {
    return buildString {
        salary.from?.let { from ->
            salary.to?.let { to ->
                append("от $from до $to")
            } ?: append("от $from")
        } ?: salary.to?.let { to ->
            append("до $to")
        } ?: return "Зарплата не указана"

        salary.currency?.let { currency ->
            append(" ${getCurrencySymbol(currency)}")
        }
    }
}

private fun formatVacancyName(vacancyName: String, companyName: String?): String {
    if (companyName.isNullOrBlank()) return vacancyName

    var result = vacancyName
    val lowerVacancyName = vacancyName.lowercase()
    val lowerCompanyName = companyName.lowercase()

    when {
        lowerVacancyName.endsWith(" в $lowerCompanyName") -> {
            result = vacancyName.removeSuffix(" в $companyName").trim()
        }

        lowerVacancyName.endsWith(lowerCompanyName) -> {
            result = vacancyName.removeSuffix(companyName).trim()
        }
    }

    return result
}

private fun getCurrencySymbol(currency: String): String {
    return when (currency) {
        "RUR", "RUB" -> "₽"
        "USD" -> "$"
        "EUR" -> "€"
        else -> currency
    }
}

private fun parseDescription(description: String): List<DescriptionSection> {
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
        lowerLine.contains("обязанности") && line.length < FIFTY -> "Обязанности"
        lowerLine.contains("требования") && line.length < FIFTY -> "Требования"
        lowerLine.contains("условия") && line.length < FIFTY -> "Условия"
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
        (lowerLine == phrase || lowerLine == "$phrase:") && line.length < THIRTY
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

    return finalSections
}

private val FILTER_PHRASES = listOf(
    "о нас",
    ABOUT_COMPANY,
    "компания:",
    "компания",
    "от компании",
    "информация о компании",
    "наша компания"
)

// Модель для секций описания
data class DescriptionSection(
    val title: String,
    val items: List<String>
)

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
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.domain.network.models.Contacts
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.ui.theme.BulletSize
import ru.practicum.android.diploma.ui.theme.LogoSize
import ru.practicum.android.diploma.ui.theme.Spacing12
import ru.practicum.android.diploma.ui.theme.Spacing16
import ru.practicum.android.diploma.ui.theme.Spacing24
import ru.practicum.android.diploma.ui.theme.Spacing4
import ru.practicum.android.diploma.ui.theme.Spacing8
import ru.practicum.android.diploma.util.formatters.formatSalary
import ru.practicum.android.diploma.util.formatters.formatVacancyNameForDetails
import ru.practicum.android.diploma.util.parser.DescriptionSection
import ru.practicum.android.diploma.util.parser.VacancyDescriptionParser

@Composable
fun VacancyDetails(
    vacancy: VacancyDetailsModel,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Spacing16),
        verticalArrangement = Arrangement.spacedBy(Spacing24)
    ) {
        val formattedVacancyName = formatVacancyNameForDetails(
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
                    text = formatSalary(salary),
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
                .padding(Spacing16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing16)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                EmployerLogoGlide(
                    logoUrl = vacancy.employer?.logo,
                    modifier = Modifier.size(LogoSize)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing4)
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
            modifier = Modifier.padding(vertical = Spacing8)
        )
    }
}

@Composable
private fun ContactsSection(contacts: Contacts) {
    val viewModel: VacancyDetailsViewModel = koinViewModel()
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing16)
    ) {
        Text(
            text = "Контакты",
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing12)
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
        horizontalArrangement = Arrangement.spacedBy(Spacing12)
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
        verticalArrangement = Arrangement.spacedBy(Spacing16)
    ) {
        Text(
            text = "Ключевые навыки",
            style = MaterialTheme.typography.titleMedium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing8),
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
        verticalArrangement = Arrangement.spacedBy(Spacing16)
    ) {
        Text(
            text = "Описание вакансии",
            style = MaterialTheme.typography.titleMedium
        )

        val sections = VacancyDescriptionParser.parseDescription(description)

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
                renderDescriptionSubsection(section)
            }
        }
    }
}

@Composable
private fun renderDescriptionSubsection(section: DescriptionSection) {
    if (section.title == "О компании") {
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

@Composable
private fun CompanyInfoSubsection(
    title: String,
    paragraphs: List<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing8)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing12)
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
        verticalArrangement = Arrangement.spacedBy(Spacing8)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing4)
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
        horizontalArrangement = Arrangement.spacedBy(Spacing8)
    ) {
        Box(
            modifier = Modifier
                .size(BulletSize)
                .offset(y = Spacing8)
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

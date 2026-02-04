package ru.practicum.android.diploma.util.formatters

fun formatVacancyNameForDetails(vacancyName: String, companyName: String?): String {
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

fun formatVacancyNameForListItem(vacancyName: String, companyName: String?, area: String?): String {
    val nameWithoutCompany = formatVacancyNameForDetails(vacancyName, companyName)

    return if (!area.isNullOrBlank()) {
        "$nameWithoutCompany, $area"
    } else {
        nameWithoutCompany
    }
}

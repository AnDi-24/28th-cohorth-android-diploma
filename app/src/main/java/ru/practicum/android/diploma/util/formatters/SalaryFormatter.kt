package ru.practicum.android.diploma.util.formatters

import ru.practicum.android.diploma.domain.network.models.Salary

fun formatSalary(salary: Salary?): String {
    return buildString {
        salary?.from?.let { from ->
            salary.to?.let { to ->
                append("от $from до $to")
            } ?: append("от $from")
        } ?: salary?.to?.let { to ->
            append("до $to")
        } ?: return "Зарплата не указана"

        salary?.currency?.let { currency ->
            append(" ${getCurrencySymbol(currency)}")
        }
    }
}

fun getCurrencySymbol(currency: String): String {
    return when (currency.uppercase()) {
        "RUR", "RUB" -> "₽" // Российский рубль
        "BYR", "BYN" -> "Br" // Белорусский рубль
        "USD" -> "$" // Доллар
        "EUR" -> "€" // Евро
        "KZT" -> "₸" // Казахстанский тенге
        "UAH" -> "₴" // Украинская гривна
        "AZN" -> "₼" // Азербайджанский манат
        "UZS" -> "сўм" // Узбекский сум
        "GEL" -> "₾" // Грузинский лари
        "KGS", "KGT" -> "с" // Киргизский сом
        else -> currency
    }
}

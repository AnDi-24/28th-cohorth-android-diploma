package ru.practicum.android.diploma.retrofit_tests

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BASE_APP_URL
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.api.FindJobApi
import ru.practicum.android.diploma.data.network.models.industries.IndustriesResponse
import ru.practicum.android.diploma.util.ResponseState

class IndustriesIntegrationTest {

    @Test
    fun `getIndustries() should return industries with valid structure`() = runBlocking {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_APP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(FindJobApi::class.java)
        val client = RetrofitNetworkClient(api)

        val response = client.getIndustries()

        assertEquals(ResponseState.SUCCESS, response.resultCode)

        val industriesResponse = response as IndustriesResponse
        val industries = industriesResponse.industries!!

        assertTrue("Список не должен быть пустым", industries.isNotEmpty())
        assertTrue("Должно быть >3 отраслей", industries.size > 3)

        industries.forEach { industry ->
            assertFalse("ID не должен быть пустым", industry.id.isBlank())
            assertFalse("NAME не должно быть пустым", industry.name.isBlank())
        }
    }
}

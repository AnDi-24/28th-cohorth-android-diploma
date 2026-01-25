package ru.practicum.android.diploma.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VacancyDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun addVacancyToFavorite(vacancy: VacancyEntity)

    @Query("DELETE FROM favorite_vacancy WHERE vacancyId = :id")
    suspend fun deleteVacancyFromFavorite(id: Long)

    @Query("SELECT * FROM favorite_vacancy")
    fun getAllFavoriteVacancy(): Flow<List<VacancyEntity>>

}

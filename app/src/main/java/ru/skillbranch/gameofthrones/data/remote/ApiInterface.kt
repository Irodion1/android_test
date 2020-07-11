package ru.skillbranch.gameofthrones.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

interface ApiInterface {
    @GET("houses?pageSize=50")
    suspend fun houses(@Query("page") page: Int = 1): List<HouseRes>

    @GET("characters/{id}")
    suspend fun character(@Path("id") id: String): List<CharacterRes>

    @GET("houses")
    suspend fun housesByName(@Query("name") name: String): List<HouseRes>

}
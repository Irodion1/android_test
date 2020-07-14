package ru.skillbranch.gameofthrones.repositories

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import kotlinx.coroutines.*
import ru.skillbranch.gameofthrones.AppConfig
import ru.skillbranch.gameofthrones.data.local.DbManager
import ru.skillbranch.gameofthrones.data.local.DbManager.db
import ru.skillbranch.gameofthrones.data.local.dao.CharacterDao
import ru.skillbranch.gameofthrones.data.local.dao.HouseDao
import ru.skillbranch.gameofthrones.data.local.entities.Character
import ru.skillbranch.gameofthrones.data.local.entities.CharacterFull
import ru.skillbranch.gameofthrones.data.local.entities.CharacterItem
import ru.skillbranch.gameofthrones.data.local.entities.House
import ru.skillbranch.gameofthrones.data.remote.ApiInterface
import ru.skillbranch.gameofthrones.data.remote.ApiService
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

object RootRepository {
    private val api: ApiInterface = ApiService.api
    private val houseDao: HouseDao = DbManager.db.houseDao()
    private val characterDao: CharacterDao = DbManager.db.characterDao()

    private val errHandler = CoroutineExceptionHandler { _, exception ->
        println("Exception on coroutine $exception")
        exception.printStackTrace()
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO + errHandler)

    /**
     * Получение данных о всех домах из сети
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getAllHouses(result: (houses: List<HouseRes>) -> Unit) {
        scope.launch {
            val res = mutableListOf<HouseRes>()
            var flag = true
            var i = 1
            while (flag) {
                api.houses(i).also {
                    res.addAll(it)
                    if (it.size == 0) {
                        flag = false
                    }
                }
                i++
            }
            result(res)
        }
    }

    /**
     * Получение данных о требуемых домах по их полным именам из сети
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о домах
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouses(vararg houseNames: String, result: (houses: List<HouseRes>) -> Unit) {
        scope.launch {
            result(
                houseNames.fold(mutableListOf<HouseRes>()) { acc, title ->
                    acc.also {
                        it.add(api.housesByName(title).first())
                    }
                }
            )
        }
    }

    /**
     * Получение данных о требуемых домах по их полным именам и персонажах в каждом из домов из сети
     * @param houseNames - массив полных названий домов (смотри AppConfig)
     * @param result - колбек содержащий в себе список данных о доме и персонажей в нем (Дом - Список Персонажей в нем)
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun getNeedHouseWithCharacters(
        vararg houseNames: String,
        result: (houses: List<Pair<HouseRes, List<CharacterRes>>>) -> Unit
    ) {
        scope.launch { result(needHouseWithCharacters(*houseNames)) }
    }

    /**
     * Запись данных о домах в DB
     * @param houses - Список персонажей (модель HouseRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertHouses(houses: List<HouseRes>, complete: () -> Unit) {
        val list = houses.map { it.toHouse() }
        scope.launch {
            houseDao.insert(list)
            complete()
        }
    }

    /**
     * Запись данных о пересонажах в DB
     * @param Characters - Список персонажей (модель CharacterRes - модель ответа из сети)
     * необходимо произвести трансформацию данных
     * @param complete - колбек о завершении вставки записей db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun insertCharacters(Characters: List<CharacterRes>, complete: () -> Unit) {
        val list = Characters.map { it.toCharacter(it.houseId) }
        scope.launch {
            characterDao.insert(list)
            complete()
        }
    }

    /**
     * При вызове данного метода необходимо выполнить удаление всех записей в db
     * @param complete - колбек о завершении очистки db
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun dropDb(complete: () -> Unit) {
        scope.launch {
            db.clearAllTables()
            complete()
        }
    }

    /**
     * Поиск всех персонажей по имени дома, должен вернуть список краткой информации о персонажах
     * дома - смотри модель CharacterItem
     * @param name - краткое имя дома (его первычный ключ)
     * @param result - колбек содержащий в себе список краткой информации о персонажах дома
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharactersByHouseName(name: String, result: (characters: List<CharacterItem>) -> Unit) {
        scope.launch {
            result(characterDao.findCharacterList(name))
        }
    }

    /**
     * Поиск персонажа по его идентификатору, должен вернуть полную информацию о персонаже
     * и его родственных отношения - смотри модель CharacterFull
     * @param id - идентификатор персонажа
     * @param result - колбек содержащий в себе полную информацию о персонаже
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun findCharacterFullById(id: String, result: (character: CharacterFull) -> Unit) {
        scope.launch {
            result(characterDao.findCharacterFull(id))
        }
    }

    /**
     * Метод возвращет true если в базе нет ни одной записи, иначе false
     * @param result - колбек о завершении очистки db
     */
    fun isNeedUpdate(result: (isNeed: Boolean) -> Unit) {
        scope.launch {
            result(houseDao.recordsCount() == 0)
        }
    }

    suspend fun isNeedUpdate() = houseDao.recordsCount() == 0

    fun findCharacter(id: String) = characterDao.findCharacter(id)

    fun findCharacters(title: String): LiveData<List<CharacterItem>> =
        characterDao.findCharacters(title)

    suspend fun sync() {
        val pairs = needHouseWithCharacters(*AppConfig.NEED_HOUSES)
        val initial = mutableListOf<House>() to mutableListOf<Character>()

        val lists = pairs.fold(initial) { acc, (houseRes, characterList) ->
            val house = houseRes.toHouse()
            val characters = characterList.map { it.toCharacter(it.houseId) }
            acc.also { (hs, ch) ->
                hs.add(house)
                ch.addAll(characters)
            }
        }
        houseDao.upsert(lists.first)
        characterDao.upsert(lists.second)
    }

    suspend fun needHouseWithCharacters(vararg houseNames: String): List<Pair<HouseRes, List<CharacterRes>>> {
        val result = mutableListOf<Pair<HouseRes, List<CharacterRes>>>()
        val houses = getNeedHouses(*houseNames)
        scope.launch {
            houses.forEach { house ->
                val characters = mutableListOf<CharacterRes>()
                result.add(house to characters)
                house.members.forEach { name ->
                    launch {
                        api.character(name)
                            .apply { houseId = house.shortName }
                            .also { characters.add(it) }
                    }
                }
            }
        }.join()
        return result
    }

    suspend fun getNeedHouses(vararg houseNames: String): List<HouseRes> {
        val result = mutableListOf<HouseRes>()
        scope.launch {
            houseNames.forEach { name ->
                launch {
                    api.housesByName(name)
                        .also { result.add(it[0]) }
                }
            }
        }.join()
        return result
    }

}
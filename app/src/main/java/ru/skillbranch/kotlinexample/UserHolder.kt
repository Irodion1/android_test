package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val map = mutableMapOf<String, User>()


    fun importUsers(list: List<String>): List<User> = mutableListOf<User>()
        .apply {
            list.forEach {
                this.add(User.makeUserCsv(it.trim()).also { user ->
                    map[user.login] = user
                })
            }
        }

    fun requestAccessCode(login: String) {
        val phone = login.normalizedPhoneNumber()
        if (!phone.isValidePhoneNumber()) {
            throw java.lang.IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        }
        map[phone]?.changeAcessCode()
    }

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User = User.makeUser(fullName, email = email, password = password)
        .also { user ->
            if (map[user.login] != null) throw IllegalArgumentException("A user with this email already exists")
            map[user.login] = user
        }

    fun registerUserByPhone(fullName: String, rawPhone: String): User {
        val phone = rawPhone.normalizedPhoneNumber()
        if (!phone.isValidePhoneNumber()) {
            throw java.lang.IllegalArgumentException("Enter a valid phone number starting with a + and containing 11 digits")
        }
        return User.makeUser(fullName, phone = phone)
            .also { user ->
                if (map[user.login] != null) throw IllegalArgumentException("A user with this phone already exists")
                map[user.login] = user
            }
    }

    fun loginUser(login: String, password: String): String? {
        return when {
            login.normalizedPhoneNumber().isValidePhoneNumber() -> {
                map[login.normalizedPhoneNumber()]?.let {
                    if (it.checkPassword(password)) return it.userInfo
                    else null
                }
            }
            else -> {
                map[login.trim()]?.let {
                    if (it.checkPassword(password)) it.userInfo
                    else null
                }
            }
        }
    }


    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }

    private fun String.normalizedPhoneNumber(): String {
        val re = Regex("[^0-9+]")
        return re.replace(this, "")
    }

    private fun String.isValidePhoneNumber(): Boolean {
        val re = Regex("\\+\\d{11}\$")
        return re.matches(this)
    }
}
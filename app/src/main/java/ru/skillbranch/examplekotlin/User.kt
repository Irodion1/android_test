package ru.skillbranch.examplekotlin

import androidx.annotation.VisibleForTesting
import ru.skillbranch.examplekotlin.User.Factory.fullNameToPair
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom

class User private constructor(
    val firstName: String,
    val lastName: String?,
    email: String? = null,
    rawPhone: String? = null,
    meta: Map<String, Any>? = null
) {
    val userInfo: String

    private val fullName: String
        get() = listOfNotNull(firstName, lastName)
            .joinToString(" ")
            .capitalize()

    private val initials: String
        get() = listOfNotNull(firstName, lastName)
            .map { it.first().toUpperCase() }
            .joinToString(" ")

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var phone: String? = null
        set(value) {
            field = value?.replace("""[^+\d]""".toRegex(), "")
        }

    private var _login: String? = null

    var login: String
        set(value) {
            _login = value.toLowerCase()
        }
        get() = _login!!

    private var salt: String? = null

    private lateinit var passwordHash: String

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    var accessCode: String? = null

    constructor(
        firstName: String,
        lastName: String?,
        email: String,
        password: String
    ) : this(firstName, lastName, email = email, meta = mapOf("auth" to "password")) {
        println("Secondary email constructor")
        passwordHash = encrypt(password)
    }

    constructor(
        firstName: String,
        lastName: String?,
        rawPhone: String
    ) : this(firstName, lastName, rawPhone = rawPhone, meta = mapOf("auth" to "sms")) {
        println("Secondary phone constructor")
        changeAcessCodeInner(rawPhone)
    }

    constructor(
        firstName: String,
        lastName: String?,
        email: String?,
        password: String?,
        rawPhone: String?
    ) : this(
        firstName,
        lastName,
        email = email,
        rawPhone = rawPhone,
        meta = mapOf("src" to "csv")
    ) {
        println("Secondary csv constructor")
        passwordHash = password ?: passwordHash
        if (rawPhone != null) changeAcessCodeInner(rawPhone)
    }


    init {
        println("First init, primary constructor called")
        check(firstName.isNotBlank()) { "FirstName must not be blank" }
        check(!email.isNullOrBlank() || !rawPhone.isNullOrBlank()) { "Email or phone must not be blank" }

        phone = rawPhone
        login = email ?: phone!!

        userInfo = """
            firstName: $firstName
            lastName: $lastName
            login: $login
            fullName: $fullName
            initials: $initials
            email: $email
            phone: $phone
            meta: $meta
        """.trimIndent()

    }

    private fun changeAcessCodeInner(rawPhone: String) {
        val code = generateAcessCode()
        passwordHash = encrypt(code)
        println("Phone passwordHash is $passwordHash")
        accessCode = code
        sendAcessCodeToUser(rawPhone, code)
    }

    fun changeAcessCode() {
        phone?.let { changeAcessCodeInner(it) }
    }

    private fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(toByteArray())
        val hexString = BigInteger(1, digest).toString(16)
        return hexString.padStart(32, '0')
    }

    private fun sendAcessCodeToUser(phone: String, code: String) {
        println(".... sending $code on $phone")
    }

    private fun generateAcessCode(): String {
        val possible = "ABCDEFGHIabcdefghi0123456789"

        return StringBuilder().apply {
            repeat(6) {
                (possible.indices).random().also { index ->
                    append(possible[index])
                }
            }
        }.toString()
    }

    fun checkPassword(pass: String) = (encrypt(pass) == passwordHash).also {
        println("Checking password, result: $it")
    }

    fun changePassword(oldPass: String, newPass: String) {
        if (checkPassword(oldPass)) {
            passwordHash = encrypt(newPass)
            if (!accessCode.isNullOrEmpty()) accessCode = newPass
            println("$oldPass changed to $newPass")
        } else throw java.lang.IllegalArgumentException("wrong password")
    }

    private fun encrypt(input: String): String {
        if (salt.isNullOrEmpty()) {
            salt = ByteArray(16).also { SecureRandom().nextBytes(it) }.toString()
        }
        println("Salt in encrypt: $salt")
        return salt.plus(input).md5()
    }

    companion object Factory {

        private val USER_FULLNAME_IDX = 0
        private val USER_EMAIL_IDX = 1
        private val USER_PASSHASH_IDX = 2
        private val USER_PHONE_IDX = 3

        fun makeUser(
            fullName: String,
            email: String? = null,
            password: String? = null,
            phone: String? = null
        ): User {
            val (firstName, lastName) = fullName.fullNameToPair()
            return when {
                !phone.isNullOrBlank() -> User(firstName, lastName, phone)
                !email.isNullOrBlank() && !password.isNullOrBlank() -> User(
                    firstName,
                    lastName,
                    email,
                    password
                )
                else -> throw IllegalArgumentException("Email or phone must not be blank")
            }
        }

        fun makeUserCsv(input: String): User? {
            if (input.isBlank()) {
                return null
            }
            val tokens = mutableListOf<String?>().apply {
                input.split(";").forEach {
                    if (it == "") this.add(null)
                    else this.add(it)
                }
            }

            val (salt, pass) = input.split(";")[USER_PASSHASH_IDX].split(":")
            val (firstName, lastName) = input.split(";")[USER_FULLNAME_IDX].fullNameToPair()
            return User(
                firstName,
                lastName,
                tokens[USER_EMAIL_IDX],
                pass,
                tokens[USER_PHONE_IDX]
            )
        }

        private fun String.fullNameToPair(): Pair<String, String?> =
            this.split(" ")
                .filter { it.isNotBlank() }
                .run {
                    when (size) {
                        1 -> first() to null
                        2 -> first() to last()
                        else -> throw IllegalArgumentException(
                            "Fullname must contain only firstname and last name, current split result: " +
                                    "${this@fullNameToPair}"
                        )
                    }
                }
    }


}



package com.easyhi.manage.data.local

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.easyhi.manage.serialize.AuthTokenP
import com.easyhi.manage.serialize.Settings
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<Settings> {
    override val defaultValue: Settings = Settings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Settings {
        try {
            return Settings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: Settings,
        output: OutputStream
    ) = t.writeTo(output)
}

val Context.settingsDataStore: DataStore<Settings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsSerializer
)

object AccessTokenSerializer : Serializer<AuthTokenP> {
    override val defaultValue: AuthTokenP
        get() = AuthTokenP.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthTokenP {
        try {
            return AuthTokenP.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AuthTokenP, output: OutputStream) = t.writeTo(output)

}

val Context.tokenDataStore: DataStore<AuthTokenP> by dataStore(
    fileName = "auth_token.pb",
    serializer = AccessTokenSerializer
)
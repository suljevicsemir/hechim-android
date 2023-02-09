package com.hechim.models.repo

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.hechim.dev.AppSettings
import java.io.InputStream
import java.io.OutputStream

object AppSettingsSerializer: Serializer<AppSettings>{

    override suspend fun readFrom(input: InputStream): AppSettings {
        try {
            return AppSettings.parseFrom(input)
        }catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot write to proto", exception)
        }
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        t.writeTo(output)
    }

    override val defaultValue: AppSettings
        get() = AppSettings.getDefaultInstance().toBuilder()
            .setDarkTheme(false)
            .setFinishedOnBoarding(false)
            .build()


}
package com.example.mobilehealthcare.network

import com.example.mobilehealthcare.domain.WorkTime
import com.example.mobilehealthcare.service.AuthService
import com.example.mobilehealthcare.service.DoctorService
import com.example.mobilehealthcare.service.HospitalService
import com.example.mobilehealthcare.service.PatientService
import com.example.mobilehealthcare.service.RecipeService
import com.example.mobilehealthcare.service.SelectedDoctorService
import com.example.mobilehealthcare.service.TerminService
import com.example.mobilehealthcare.service.UserService
import com.example.mobilehealthcare.service.WorkTimeService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiClient {

    private const val BASE_URL = "http://192.168.1.3:8080"

    @Provides
    @Singleton
    @Named("AuthOkHttp")
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Named("PublicOkHttp")
    fun providePublicOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    @Named("AuthRetrofit")
    fun provideRetrofit(@Named("AuthOkHttp") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                    classDiscriminator = "type"
                    serializersModule = appSerializersModule
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    @Provides
    @Singleton
    @Named("PublicRetrofit")
    fun providePublicRetrofit(@Named("PublicOkHttp") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                Json {
                    prettyPrint = true
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                    classDiscriminator = "type"
                    serializersModule = appSerializersModule
                }.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    // Javne rute
    @Provides
    @Singleton
    fun provideAuthService(@Named("PublicRetrofit") retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideHospitalService(@Named("PublicRetrofit") retrofit: Retrofit): HospitalService =
        retrofit.create(HospitalService::class.java)

    // Zaštićene rute
    @Provides
    @Singleton
    fun provideUserService(@Named("AuthRetrofit") retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideDoctorService(@Named("AuthRetrofit") retrofit: Retrofit): DoctorService =
        retrofit.create(DoctorService::class.java)

    @Provides
    @Singleton
    fun provideWorkTimeService(@Named("AuthRetrofit") retrofit: Retrofit): WorkTimeService =
        retrofit.create(WorkTimeService::class.java)
    @Provides
    @Singleton
    fun providePatientService(@Named("AuthRetrofit")retrofit: Retrofit): PatientService=
        retrofit.create(PatientService::class.java)
    @Provides
    @Singleton
    fun provideTerminService(@Named("AuthRetrofit")retrofit: Retrofit): TerminService=
        retrofit.create(TerminService::class.java)
    @Provides
    @Singleton
    fun provideRecipeService(@Named("AuthRetrofit")retrofit: Retrofit): RecipeService=
        retrofit.create(RecipeService::class.java)
    @Provides
    @Singleton
    fun provideSelectedDoctorService(@Named("AuthRetrofit")retrofit: Retrofit): SelectedDoctorService=
        retrofit.create(SelectedDoctorService::class.java)
}


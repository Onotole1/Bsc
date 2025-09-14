
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response as OkHttpResponse
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject
import javax.inject.Singleton


// Retrofit API interface
interface ApiService {
    @FormUrlEncoded
    @POST("api/v1/cabinet/auth/login")
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): LoginResponse?

    @GET("api/v1/cabinet/user")
    suspend fun getUser(
        @Header("Authorization") apiKey: String
    ): UserResponse?

    @GET("/api/v1/cabinet/user/subscriptions/other")
    suspend fun getSubscriptions(
        @Header("Authorization") apiKey: String
    ) : SubscriptionsResponse?

    @POST("/api/v1/cabinet/user/services/credit")
    suspend fun creditActivation(
        @Header("Authorization") apiKey: String
    ) : CreditResponse?

    @Multipart
    @POST("/api/v1/cabinet/auth/phone")
    suspend fun phone(
        @Part("phone") phone: RequestBody
    )

    @POST("/api/v1/cabinet/auth/phone/otp")
    suspend fun smsPicker(
        @Field("otp") otp: String
    ): LoginResponse?

}

// Interceptor to add User-Agent header
class UserAgentInterceptor(private val userAgent: String) : Interceptor {
    override fun intercept(chain: okhttp3.Interceptor.Chain): OkHttpResponse {
        val originalRequest: Request = chain.request()
        val requestWithUserAgent = originalRequest.newBuilder()
            .header("User-Agent", userAgent)
            .build()
        return chain.proceed(requestWithUserAgent)
    }
}

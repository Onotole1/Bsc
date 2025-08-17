package ru.bsvyazi.bsconnect.api

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.Call
import retrofit2.Response
import com.google.gson.annotations.SerializedName
import java.io.IOException

// Data classes to parse the JSON responses

data class LoginResponse(
    val success: Boolean,
    val code: Int,
    val data: TokenData?,
    val message: String
)

data class TokenData(
    val token: String
)

data class UserResponse(
    val success: Boolean,
    val code: Int,
    val data: UserData?,
    val message: String
)

data class UserData(
    val user: String,
    val crypt_method: String,
    val state: String,
    val uid: String,
    val gid: String,
    val deposit: String,
    val credit: String,
    val fio: String,
    val phone: String,
    val address: String,
    val prim: String,
    val add_date: String,
    val blocked: String,
    val activated: String,
    val expired: String,
    val total_time: String,
    val total_traffic: String,
    val total_money: String,
    val last_connection: String,
    val framed_ip: String,
    val framed_mask: String,
    val callback_number: String,
    val local_ip: String,
    val local_mac: String,
    val sectorid: String,
    val create_mail: String,
    val user_installed: String,
    val speed_rate: String,
    val speed_burst: String,
    val gidd: String,
    val link_to_ip_mac: String,
    val email: String,
    val passportserie: String,
    val passportpropiska: String,
    val passportvoenkomat: String,
    val passportgdevidan: String,
    val inn: String,
    val real_ip: String,
    val real_price: String,
    val real_ipfree: String,
    val dogovor: String,
    val credit_procent: String,
    val rating: String,
    val mob_tel: String,
    val sms_tel: String,
    val date_birth: String,
    val date_birth_do: String,
    val languarddisable: String,
    val credit_unlimited: String,
    val dontshowspeed: String,
    val numdogovor: String,
    val app: String,
    val switchport: String,
    val houseid: String,
    val housingid: String,
    val houseblockid: String,
    val porch: String,
    val floor: String,
    val swid: String,
    val use_router: String,
    val router_model: String,
    val router_login: String,
    val router_pass: String,
    val router_ssid: String,
    val router_wep_pass: String,
    val router_we_saled: String,
    val router_use_dual: String,
    val router_add_date: String,
    val router_serial: String,
    val router_port: String,
    val credit_stop: String,
    val date_abonka: String,
    val mac_reg: String,
    val fixed_cost: String,
    val deletedtable: String,
    val otkluchentable: String,
    val frezetable: String,
    val useruid: String,
    val lane: String,
    val house: String,
    val settlementname: String,
    val packet: String,
    val fixed: String,
    val tarif_fixed_cost: String,
    val fixed_cost2: String,
    val us_uid: String,
    val ext_legal_person: String,
    val dogovor_date: String,
    val invoice_print_on: String,
    val ext_show_company_name: String,
    val ext_show_company_name_text: String,
    val show_company_name: String,
    val show_company_name_text: String,
    val dealer_id: String,
    val local_mask: String,
    val tarif: String,
    val shapers_day_night_active: String,
    val UE: String,
    val show: Show,
    val use_wqiwiru: String,
    val use_robokassa: String,
    val use_liqpay: String,
    val liqpay_kommiss_on: String,
    val liqpay_kommiss_percent: String,
    val use_onpay: String,
    val use_privat24: String,
    val use_pscb: String,
    val use_paymaster: String,
    val use_stripe: String,
    val use_paypal: String,
    val use_paykeeper: String,
    val use_ukrpays: String,
    val use_yandex: String,
    val use_portmone: String,
    val use_uniteller: String,
    val use_ipay: String,
    val use_fondy: String,
    val use_sberbankrumrch: String,
    val use_simplepay: String,
    val use_yandexmoney: String,
    val use_cloudpayments: String,
    val use_alfabankru: String,
    val use_isbank: String,
    val use_paysoft: String,
    val use_ckassa: String,
    val use_tinkoff: String,
    val tinkoff_kommiss_percent: String,
    val use_easypay: String,
    val use_paycell: String,
    val use_masterpass: String,
    val use_paymo: String,
    val use_payme: String,
    val use_click: String,
    val barcode_on: String,
    val qrcode_on: String,
    val paysera_on: String,
    val qiwi_on: String,
    val reeves_on: String,
    val iviru_on: String,
    val rentsoft_on: String,
    val rentsoft_secret: String,
    val rentsoft_ag_name: String,
    val rentsoft_dev_domain_suffix: String,
    val use_privat_v2: String,
    val payment_methods: PaymentMethods,
    val do_turbo: String,
    val turbo_buy_cena: String,
    val do_real_ip: String,
    val real_ip_buy_cena: String,
    val real_ip_disable_cena: String,
    val do_perevod_na_tarif: String,
    val tarif_perevod_vniz: String,
    val tarif_perevod_vverh: String,
    val current_rating: String,
    val freeze_do_ever: String,
    val start_credit_date: String,
    val stop_credit_date: String,
    val start_credit_procent_date: String,
    val stop_credit_procent_date: String,
    val do_credit_vremen_global: String,
    val do_credit_procent_vremen_global: String,
    val do_credit_swing_date_use: String,
    val do_credit_vremen: String,
    val do_credit_vremen_use: String,
    val do_credit_procent_vremen_use: String,
    val do_credit_vremen_start_date: String,
    val do_credit_vremen_stop_date: String,
    val do_credit_procent_vremen_start_date: String,
    val do_credit_procent_vremen_stop_date: String,
    val use_cards: String,
    val use_perevod: String,
    val use_change_pass: String,
    val use_change_data: String,
    val do_fixed_credit: String,
    val do_credit_swing_date: String,
    val do_credit_swing_date_days: String,
    val credit_active_cena: String,
    val credit_procent_active_cena: String,
    val do_credit_vozvrat_aktiv_cena: String,
    val turbo_active_cena: String,
    val turbo_time: String,
    val turbo_speed_in: String,
    val turbo_speed_out: String,
    val do_print_dogovor: String,
    val zapret_uhoda_s_tarifa: String,
    val razresh_minus: String,
    val use_drweb: String,
    val check_freeze: String,
    val freeze_date_info: List<Any>,
    val unfreeze_earlier_disallow: String,
    val kabinet_do_freeze_balanse_plus: String,
    val cena_freeze: String,
    val count_free_freeze: String,
    val count_free_freeze_use: String,
    val freeze_fixed_month: String,
    val kabinet_do_freeze: String,
    val cena_unfreeze: String,
    val cena_sutok_freeze: String,
    val freeze_do_return_abonolata: String,
    val min_sutok_freeze: String,
    val dont_show_speed_tarif: String,
    val dont_display_local_ip: String,
    val dont_display_framed_ip: String,
    val do_perevod_fixed: String,
    val perevod_summa: String,
    val real_ip_link_with_packet_type: String,
    val fixed_tarif: String,
    val current_day: String,
    val abonplata_user_dev: String,
    val abonplata_subscriptions: String,
    val abonplata: String,
    val abonplata_real_now: String,
    val abonplata_installments: String,
    val erec_payment: String,
    val itog_summa: String,
    val do_fixed_credit_summa: String,
    val days_left: String,
    val date_itog: String,
    val address_old: String,
    val fee: Fee
)

data class Show(
    val payments_tile: String,
    val speed_out: String,
    val speed_in: String,
    val index_daysleft: String,
    val index_enddate: String,
    val fee: String,
    val discount: String
)

data class PaymentMethods(
    val alfabankru: PaymentMethodStatus,
    val ckassa: PaymentMethodStatus,
    val click: PaymentMethodStatus,
    val cloudpayments: PaymentMethodStatus,
    val easypay: PaymentMethodStatus,
    val fondy: PaymentMethodStatus,
    val freedompay: PaymentMethodStatus,
    val ipay: PaymentMethodStatus,
    val isbank: PaymentMethodStatus,
    val liqpay: LiqpayStatus,
    val onpay: PaymentMethodStatus,
    val paycell: PaymentMethodStatus,
    val paykeeper: PaymentMethodStatus,
    val paymaster: PaymentMethodStatus,
    val payme: PaymentMethodStatus,
    val paymo: PaymentMethodStatus,
    val paypal: PaymentMethodStatus,
    val paysera: PaymentMethodStatus,
    val paysoft: PaymentMethodStatus,
    val platon: PaymentMethodStatus,
    val portmone: PaymentMethodStatus,
    val privatv2: PaymentMethodStatus,
    val pscb: PaymentMethodStatus,
    val qiwi: PaymentMethodStatus,
    val reeves: PaymentMethodStatus,
    val robokassa: PaymentMethodStatus,
    val sberbankrumrch: PaymentMethodStatus,
    val simplepay: PaymentMethodStatus,
    val stripe: PaymentMethodStatus,
    val tinkoff: TinkoffStatus,
    val ukrpays: PaymentMethodStatus,
    val uniteller: PaymentMethodStatus,
    val wqiwiru: PaymentMethodStatus,
    val yandex: PaymentMethodStatus,
    val yandexmoney: PaymentMethodStatus
)

data class PaymentMethodStatus(
    val enabled: Int
)

data class LiqpayStatus(
    val enabled: Int,
    val commission_enabled: Int,
    val commission_percent: String
)

data class TinkoffStatus(
    val enabled: Int,
    val commission_enabled: Int,
    val commission_percent: String
)

data class Fee(
    val packet: PacketPrice,
    val subscriptions: SubscriptionsFee,
    val devices: DevicesFee,
    val realip: String,
    val installments: String,
    val total: String,
    val total_with_discount: String
)

data class PacketPrice(
    val price: String,
    val discount: Discount,
    val price_with_discount: String
)

data class Discount(
    val value: String,
    val sign: String
)

data class SubscriptionsFee(
    val total: String,
    val total_with_discount: String,
    val discount: Discount,
    val detailed: List<SubscriptionDetail>
)

data class SubscriptionDetail(
    val id: String,
    val name: String,
    val price: String,
    val discount: String,
    val price_with_discount: String
)

data class DevicesFee(
    val total: String,
    val total_with_discount: String,
    val discount: Discount,
    val detailed: List<Any>
)

// Retrofit API interface

interface ApiService {
    @FormUrlEncoded
    @POST("api/v1/cabinet/auth/login")
    fun login(
        @Field("login") login: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("api/v1/cabinet/user")
    fun getUser(
        @Header("Authorization") apiKey: String
    ): Call<UserResponse>

}

// Main client class to encapsulate login and fetch user logic

class BsvyaziClient(
    private val userAgent: String = "YourAppUserAgent"
) {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.bsvyazi.ru/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    /**
     * Attempt to login with the given credentials.
     * @return token string if success, or null if failure.
     */
    fun login(login: String, password: String): String? {
        val call = api.login(login, password)
        val response: Response<LoginResponse>
        try {
            response = call.execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    return body.data.token.trim()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Fetch user data with the given API token.
     * @return UserData object if success, or null if error.
     */
    fun getUserData(apiKey: String): UserData? {
        val call = api.getUser(apiKey)
        val response: Response<UserResponse>
        try {
            response = call.execute()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success && body.data != null) {
                    return body.data
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
package ru.bsvyazi.bsconnect.Repository

data class UserData(
    var login: String = "",
    var fio: String = "",
    var tarif: String = "",
    var amount: String = "",
    var credit: String = "",
    var address: String = "",
    var feeName: String = "",
    var feePrice: String = "0",
    var status: String = "",
    var internetPrice: String = "-",
    var endDate: String = ""
)
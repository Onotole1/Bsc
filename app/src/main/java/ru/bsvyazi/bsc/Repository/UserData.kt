package ru.bsvyazi.bsc.Repository

data class UserData (
    var fio: String = "",
    var tarif: String = "",
    var amount: String = "",
    var address: String = "",
    var feeName: String = "",
    var feePrice: String = "0",
    var status: String = "",
    var internetPrice: String = "-",
    var endDate: String = ""
)
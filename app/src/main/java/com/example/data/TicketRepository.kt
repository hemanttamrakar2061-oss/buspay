package com.example.first.data

class TicketRepository {

    fun validateTicket(qr: String): Boolean {
        return qr.isNotEmpty()
    }
}
package com.example.jpa.onetomany

import javax.persistence.*

@Entity
data class Traveler(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    val name: String
) {
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    lateinit var reservation: Reservation
}

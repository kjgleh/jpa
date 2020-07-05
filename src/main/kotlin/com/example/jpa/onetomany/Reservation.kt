package com.example.jpa.onetomany

import javax.persistence.*

@Entity
data class Reservation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    val customerName: String,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "reservation")
    val travelers: List<Traveler>
)
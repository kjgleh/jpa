package com.example.jpa.onetomany

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
internal class ReservationRepositoryTest {

    @Autowired
    private lateinit var reservationRepository: ReservationRepository

    @Test
    fun save_correctly() {
        // Arrange
        val reservation = Reservation(
            customerName = UUID.randomUUID().toString()
        )

        // Act
        val sut = reservationRepository
        val actual = sut.save(reservation)

        // Assert
        assertThat(actual).isEqualTo(reservation)
    }
}
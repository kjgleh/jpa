package com.example.jpa.onetomany

import com.appmattus.kotlinfixture.decorator.recursion.NullRecursionStrategy
import com.appmattus.kotlinfixture.decorator.recursion.recursionStrategy
import com.appmattus.kotlinfixture.kotlinFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.RepeatedTest
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

    private val fixture = kotlinFixture {
        recursionStrategy(NullRecursionStrategy)
    }

    @Test
    fun save_correctly() {
        // Arrange
        val travelers = listOf(
            Traveler(name = UUID.randomUUID().toString()),
            Traveler(name = UUID.randomUUID().toString()),
            Traveler(name = UUID.randomUUID().toString())
        )
        val reservation = Reservation(
            customerName = UUID.randomUUID().toString(),
            travelers = travelers
        )
        travelers.map {
            it.reservation = reservation
        }

        // Act
        val sut = reservationRepository
        val actual = sut.save(reservation)

        // Assert
        assertThat(actual).isEqualTo(reservation)
    }

    @Test
    fun test_save_with_kotlinFixture() {
        // Arrange
        val reservation = fixture<Reservation>().copy(
            travelers = listOf(
                Traveler(name = UUID.randomUUID().toString()),
                Traveler(name = UUID.randomUUID().toString()),
                Traveler(name = UUID.randomUUID().toString())
            )
        )

        reservation.travelers.forEach {
            it.reservation = reservation
        }

        // Act
        val sut = reservationRepository
        val actual = sut.save(reservation)

        // Assert
        assertThat(actual).isEqualToIgnoringGivenFields(reservation, "id", "travelers")
        assertThat(actual.travelers)
            .usingElementComparatorIgnoringFields("id", "reservation")
            .isEqualTo(reservation.travelers)
    }

    /**
     * 아래 테스트는 에러가 발생한다
     */
    @RepeatedTest(10)
    fun test_save_with_kotlinFixture_error() {
        // Arrange
        val reservation = fixture<Reservation>().copy(
            travelers = travelers()
        )

        reservation.travelers.forEach {
            it.reservation = reservation
        }

        // Act
        val sut = reservationRepository
        val actual = sut.save(reservation)

        // Assert
        assertThat(actual).isEqualToIgnoringGivenFields(reservation, "id", "travelers")
        assertThat(actual.travelers)
            .usingElementComparatorIgnoringFields("id", "reservation")
            .isEqualTo(reservation.travelers)
    }

    private fun travelers(): List<Traveler> {
        val travelers = mutableListOf<Traveler>()
        repeat(2) {
            travelers.add(fixture())
        }
        return travelers
    }
}
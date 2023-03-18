package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> getBookingsByBookerIdAndStatusOrderByStartDateDesc(long bookerId, String status);
    List<Booking> getBookingsByBookerIdOrderByStartDateDesc(long bookerId);

}
package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;

    @Override
    public Booking addBooking(Booking booking) {
        if (!isValidBooking(booking)) throw new NoSuchElementException("item to add is not valid");
        return repository.save(booking);
    }

    private boolean isValidBooking(Booking booking) {
        return true;
    }
}

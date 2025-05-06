package com.e2rent.rent_service.service;

import com.e2rent.rent_service.dto.CreateRentalRequestDto;
import com.e2rent.rent_service.dto.RentalResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Сервіс для роботи з орендними запитами.
 */
public interface    IRentalService {

    /**
     * Створює новий запит на оренду.
     *
     * @param request   DTO з інформацією для створення запиту
     * @param authToken токен автентифікації користувача, що створює запит
     */
    void createRental(CreateRentalRequestDto request, String authToken);

    /**
     * Отримує запит на оренду за його ідентифікатором.
     *
     * @param rentalId  ідентифікатор оренди
     * @param authToken токен автентифікації користувача
     * @return DTO з інформацією про оренду
     */
    RentalResponseDto getRentalById(Long rentalId, String authToken);

    /**
     * Отримує список запитів на оренду, створених поточним користувачем.
     *
     * @param authToken токен автентифікації користувача
     * @param pageable  параметри сторінкування
     * @return сторінка з орендами, створеними користувачем
     */
    Page<RentalResponseDto> getMyOutgoingRentals(String authToken, Pageable pageable);

    /**
     * Отримує список запитів на обладнання, яке належить поточному користувачу.
     *
     * @param authToken токен автентифікації користувача
     * @param pageable  параметри сторінкування
     * @return сторінка з орендами, що стосуються обладнання користувача
     */
    Page<RentalResponseDto> getMyIncomingRentals(String authToken, Pageable pageable);

    /**
     * Підтверджує запит на оренду (доступно лише власнику обладнання).
     *
     * @param rentalId  ідентифікатор оренди
     * @param authToken токен автентифікації власника
     */
    void approveRental(Long rentalId, String authToken);

    /**
     * Відхиляє запит на оренду з повідомленням (доступно лише власнику обладнання).
     *
     * @param rentalId         ідентифікатор оренди
     * @param rejectionMessage повідомлення, що пояснює причину відмови
     * @param authToken        токен автентифікації власника
     */
    void rejectRental(Long rentalId, String rejectionMessage, String authToken);

    /**
     * Скасовує запит на оренду, якщо він ще не був підтверджений.
     *
     * @param rentalId  ідентифікатор оренди
     * @param authToken токен автентифікації користувача (тільки орендар може скасувати)
     */
    void cancelRental(Long rentalId, String authToken);

    /**
     * Генерує PDF-документ, що містить деталі оренди.
     * Доступ дозволено лише орендарю або власнику обладнання.
     *
     * @param rentalId  ідентифікатор оренди
     * @param authToken токен автентифікації користувача
     * @return масив байтів, що представляє PDF-файл
     */
    byte[] generateRentalPdf(Long rentalId, String authToken);
}

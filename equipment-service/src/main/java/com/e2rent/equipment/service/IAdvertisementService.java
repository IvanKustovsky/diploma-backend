package com.e2rent.equipment.service;

import com.e2rent.equipment.dto.AdvertisementDto;
import com.e2rent.equipment.dto.AdvertisementFilterDto;
import com.e2rent.equipment.entity.Equipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdvertisementService {

    // ---------------------------- CREATE ----------------------------

    /**
     * Створити нове оголошення на основі обладнання.
     *
     * @param equipment об'єкт обладнання, для якого створюється оголошення
     */
    void createAdvertisement(Equipment equipment);


    // ---------------------------- MODERATION ----------------------------

    /**
     * Схвалити оголошення (робить його видимим для користувачів).
     *
     * @param advertisementId ID оголошення
     * @param adminMessage    повідомлення адміністратора (може містити коментар чи інструкції)
     */
    void approveAdvertisement(Long advertisementId, String adminMessage);

    /**
     * Відхилити оголошення (не буде показане користувачам).
     *
     * @param advertisementId ID оголошення
     * @param adminMessage    повідомлення з поясненням причини відхилення
     */
    void rejectAdvertisement(Long advertisementId, String adminMessage);


    // ---------------------------- UPDATE ----------------------------

    /**
     * Позначити оголошення як оновлене (наприклад, після редагування обладнання).
     *
     * @param equipmentId ID обладнання, яке було оновлено
     */
    void markAsUpdated(Long equipmentId);


    // ---------------------------- READ ----------------------------

    /**
     * Отримати сторінку всіх оголошень, які очікують на модерацію.
     *
     * @param pageable об'єкт пагінації
     * @return сторінка DTO оголошень зі статусом PENDING
     */
    Page<AdvertisementDto> getAllPending(Pageable pageable);

    /**
     * Отримати сторінку всіх схвалених оголошень, створених конкретним користувачем.
     *
     * @param userId   ID користувача
     * @param pageable об'єкт пагінації
     * @return сторінка DTO оголошень, створених користувачем і схвалених модератором
     */
    Page<AdvertisementDto> getAllApprovedByUserId(Long userId, Pageable pageable);

    /**
     * Отримати сторінку всіх оголошень автентифікованого користувача.
     *
     * @param authToken   Токен користувача
     * @param pageable об'єкт пагінації
     * @return сторінка DTO оголошень, створених користувачем і схвалених модератором
     */
    Page<AdvertisementDto> getMyAdvertisements(String authToken, Pageable pageable);

    /**
     * Отримати сторінку схвалених оголошень за заданими фільтрами, де статус оголошення — APPROVED,
     * а статус обладнання — AVAILABLE.
     *
     * @param authToken   Токен користувача
     * @param filter   об'єкт із параметрами фільтрації
     * @param pageable об'єкт пагінації
     * @return сторінка DTO оголошень, що відповідають критеріям
     */
    Page<AdvertisementDto> searchFilteredAdvertisements(String authToken,
                                                        AdvertisementFilterDto filter,
                                                        Pageable pageable);
}


package com.e2rent.equipment.service;

import com.e2rent.equipment.dto.EquipmentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IEquipmentService {

    // ---------------------------- CREATE ----------------------------

    /**
     * Реєстрація нового обладнання з головним зображенням.
     *
     * @param equipmentDto       DTO з даними про обладнання
     * @param file               файл зображення
     * @param authorizationToken токен авторизації користувача
     */
    void registerEquipment(EquipmentDto equipmentDto, MultipartFile file, String authorizationToken);


    // ---------------------------- READ ----------------------------

    /**
     * Отримати обладнання за його ідентифікатором.
     *
     * @param equipmentId ID обладнання
     * @return DTO з повною інформацією про обладнання
     */
    EquipmentDto fetchEquipment(Long equipmentId);

    /**
     * Отримати зображення за ID.
     *
     * @param imageId ID зображення
     * @return масив байтів зображення
     */
    byte[] downloadImage(Long imageId);

    /**
     * Отримати ID власника обладнання за його ID.
     *
     * @param equipmentId ID обладнання
     * @return ID користувача-власника
     */
    Long getOwnerIdByEquipmentId(Long equipmentId);


    // ---------------------------- UPDATE ----------------------------

    /**
     * Оновити інформацію про обладнання.
     *
     * @param equipmentId        ID обладнання
     * @param equipmentDto       DTO з новими даними
     * @param authorizationToken токен авторизації користувача
     */
    void updateEquipment(Long equipmentId, EquipmentDto equipmentDto, String authorizationToken);

    /**
     * Завантажити головне зображення для обладнання.
     *
     * @param equipmentId        ID обладнання
     * @param file               файл з головним зображенням
     * @param authorizationToken токен авторизації користувача
     */
    void uploadMainImage(Long equipmentId, MultipartFile file, String authorizationToken);

    /**
     * Завантажити додаткові зображення для обладнання.
     *
     * @param equipmentId        ID обладнання
     * @param files              список файлів зображень
     * @param authorizationToken токен авторизації користувача
     */
    void uploadImages(Long equipmentId, List<MultipartFile> files, String authorizationToken);


    // ---------------------------- DELETE ----------------------------

    /**
     * Видалити обладнання за ID.
     *
     * @param equipmentId ID обладнання
     */
    void deleteEquipment(Long equipmentId); // TODO: реалізація


    // ---------------------------- STATUS MANAGEMENT ----------------------------

    /**
     * Деактивувати обладнання (робить його недоступним для оренди).
     *
     * @param equipmentId        ID обладнання
     * @param authorizationToken токен авторизації користувача
     */
    void deactivateEquipmentById(Long equipmentId, String authorizationToken);

    /**
     * Активувати обладнання (робить його доступним для оренди).
     *
     * @param equipmentId        ID обладнання
     * @param authorizationToken токен авторизації користувача
     */
    void activateEquipmentById(Long equipmentId, String authorizationToken);
}

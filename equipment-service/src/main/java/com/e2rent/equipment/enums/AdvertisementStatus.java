package com.e2rent.equipment.enums;

public enum AdvertisementStatus {
    CREATED,        // Створено користувачем, чекає на перевірку
    UPDATED,        // Оновлено користувачем — знову чекає на перевірку
    APPROVED,       // Затверджено модератором/адміном — доступне для оренди
    REJECTED,       // Відхилено модератором, може бути оновлене
}

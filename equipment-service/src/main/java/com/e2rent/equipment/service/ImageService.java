package com.e2rent.equipment.service;

import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.DataFormatException;

public interface ImageService {

    Image uploadImage(MultipartFile imageFile, Equipment equipment);

    byte[] downloadImage(Long id);

    void deleteImage(Long id);
}

package com.example.equipment.service;

import com.example.equipment.entity.Equipment;
import com.example.equipment.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.DataFormatException;

public interface ImageService {

    Image uploadImage(MultipartFile imageFile, Equipment equipment) throws IOException;

    byte[] downloadImage(Long id) throws IOException, DataFormatException;
}

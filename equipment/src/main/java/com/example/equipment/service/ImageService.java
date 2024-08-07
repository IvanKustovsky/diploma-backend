package com.example.equipment.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.DataFormatException;

public interface ImageService {

    boolean uploadImage(MultipartFile imageFile) throws IOException;

    byte[] downloadImage(Long id) throws IOException, DataFormatException;
}

package com.example.equipment.service.impl;

import com.example.equipment.entity.Image;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.repository.ImageRepository;
import com.example.equipment.service.ImageService;
import com.example.equipment.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public boolean uploadImage(MultipartFile imageFile) throws IOException {
        var imageToSave = Image.builder()
                .name(imageFile.getOriginalFilename())
                .type(imageFile.getContentType())
                .imageData(ImageUtils.compressImage(imageFile.getBytes()))
                .build();
        imageRepository.save(imageToSave);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadImage(Long id) throws IOException, DataFormatException {
        Image dbImage = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", String.valueOf(id)));
        return ImageUtils.decompressImage(dbImage.getImageData());
    }
}

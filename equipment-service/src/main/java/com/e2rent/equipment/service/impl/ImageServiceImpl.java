package com.e2rent.equipment.service.impl;

import com.e2rent.equipment.entity.Equipment;
import com.e2rent.equipment.entity.Image;
import com.e2rent.equipment.exception.ImageUploadException;
import com.e2rent.equipment.exception.ResourceNotFoundException;
import com.e2rent.equipment.repository.ImageRepository;
import com.e2rent.equipment.service.ImageService;
import com.e2rent.equipment.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.DataFormatException;

@Slf4j // TODO: Add logging
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE);

    @Override
    @Transactional
    public Image uploadImage(MultipartFile imageFile, Equipment equipment) {
        String contentType = imageFile.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new ImageUploadException("Unsupported image type: " + contentType);
        }

        try {
            var imageToSave = Image.builder()
                    .name(imageFile.getOriginalFilename())
                    .type(contentType)
                    .imageData(ImageUtils.compressImage(imageFile.getBytes()))
                    .equipment(equipment)
                    .build();
            return imageRepository.save(imageToSave);
        } catch (IOException e) {
            throw new ImageUploadException("Failed to process image file", e);
        }
    }

    @Override
    @Transactional
    public byte[] downloadImage(Long id) throws IOException, DataFormatException {
        Image dbImage = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "id", String.valueOf(id)));
        return ImageUtils.decompressImage(dbImage.getImageData());
    }

    @Override
    @Transactional
    public void deleteImage(Long id) {
        imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "ID", String.valueOf(id)));

        imageRepository.deleteById(id);
    }
}

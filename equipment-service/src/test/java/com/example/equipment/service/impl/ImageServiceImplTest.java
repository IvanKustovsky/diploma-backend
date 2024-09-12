package com.example.equipment.service.impl;

import com.example.equipment.entity.Equipment;
import com.example.equipment.entity.Image;
import com.example.equipment.exception.ImageUploadException;
import com.example.equipment.exception.ResourceNotFoundException;
import com.example.equipment.repository.ImageRepository;
import com.example.equipment.util.ImageUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Image Service Test Class")
class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageServiceImpl;

    @Test
    void uploadImageSuccess() throws IOException {
        // given
        MultipartFile imageFile = mock(MultipartFile.class);
        Equipment equipment = mock(Equipment.class);
        Image image = mock(Image.class);

        when(imageFile.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
        when(imageFile.getBytes()).thenReturn("image_data".getBytes());
        when(imageFile.getOriginalFilename()).thenReturn("test.jpg");

        when(imageRepository.save(any(Image.class))).thenReturn(image);

        // when
        Image savedImage = imageServiceImpl.uploadImage(imageFile, equipment);

        // then
        assertNotNull(savedImage);
        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    void uploadImageUnsupportedType() {
        // given
        MultipartFile imageFile = mock(MultipartFile.class);
        Equipment equipment = mock(Equipment.class);

        when(imageFile.getContentType()).thenReturn("application/pdf");

        // when, then
        assertThatThrownBy(() -> imageServiceImpl.uploadImage(imageFile, equipment))
                .isInstanceOf(ImageUploadException.class)
                .hasMessageContaining("Unsupported image type");

        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    void uploadImageIOException() throws IOException {
        // given
        MultipartFile imageFile = mock(MultipartFile.class);
        Equipment equipment = mock(Equipment.class);

        when(imageFile.getContentType()).thenReturn(MediaType.IMAGE_JPEG_VALUE);
        when(imageFile.getBytes()).thenThrow(new IOException("Test IOException"));

        // when, then
        assertThatThrownBy(() -> imageServiceImpl.uploadImage(imageFile, equipment))
                .isInstanceOf(ImageUploadException.class)
                .hasMessageContaining("Failed to process image file");

        verify(imageRepository, never()).save(any(Image.class));
    }

    @Test
    void downloadImageSuccess() throws IOException, DataFormatException {
        // given
        Long imageId = 1L;
        Image dbImage = mock(Image.class);

        byte[] compressedData = ImageUtils.compressImage("test_image_data".getBytes());

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(dbImage));
        when(dbImage.getImageData()).thenReturn(compressedData);

        // when
        byte[] imageData = imageServiceImpl.downloadImage(imageId);

        // then
        assertNotNull(imageData);
        verify(imageRepository, times(1)).findById(imageId);
    }

    @Test
    void downloadImageNotFound() {
        // given
        Long imageId = 1L;

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> imageServiceImpl.downloadImage(imageId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Image", "id", imageId));

        verify(imageRepository, times(1)).findById(imageId);
    }

    @Test
    void deleteImageSuccess() {
        // given
        Long imageId = 1L;
        Image dbImage = mock(Image.class);

        when(imageRepository.findById(imageId)).thenReturn(Optional.of(dbImage));

        // when
        imageServiceImpl.deleteImage(imageId);

        // then
        verify(imageRepository, times(1)).findById(imageId);
        verify(imageRepository, times(1)).deleteById(imageId);
    }

    @Test
    void deleteImageNotFound() {
        // given
        Long imageId = 1L;

        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> imageServiceImpl.deleteImage(imageId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.format("%s not found with the given input data %s: '%s'",
                        "Image", "ID", imageId));

        verify(imageRepository, times(1)).findById(imageId);
        verify(imageRepository, never()).deleteById(imageId);
    }
}
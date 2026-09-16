package com.example.instajava.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {

    @NotNull(message = "Изображение обязательно для загрузки")
    private MultipartFile image;

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    private String caption;
}
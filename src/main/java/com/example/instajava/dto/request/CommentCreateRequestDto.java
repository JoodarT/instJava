package com.example.instajava.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateRequestDto {

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(max = 2000, message = "Комментарий не должен превышать 2000 символов")
    private String text;
}
package com.example.TCC.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NicknameRequestDto {
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 설정해야 합니다.")
    private String nickname;
}

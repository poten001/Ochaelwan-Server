package com.example.TCC.controller;

import com.example.TCC.dto.response.MemberResponseDto;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> showAll() {
        List<MemberResponseDto> dtos = memberService.members();
        if (dtos == null) {
            throw new NotFoundException("멤버가 없습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(dtos);
    }
}

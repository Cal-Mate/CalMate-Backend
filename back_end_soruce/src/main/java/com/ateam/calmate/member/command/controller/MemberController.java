package com.ateam.calmate.member.command.controller;

import com.ateam.calmate.common.ResponseMessage;
import com.ateam.calmate.member.command.dto.*;
import com.ateam.calmate.member.command.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/member")
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MemberController(MemberService memberService
    , BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberService = memberService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @GetMapping("/member-info/{id}")
    public ResponseMemberDTO findMember(@PathVariable Long id) {
        ResponseMemberDTO member = memberService.findMember(id);
        return member;
    }

    @PostMapping("/member")
    public ResponseEntity<ResponseMessage> addMember(@RequestBody RequestMemberDTO member) {

        try {
            // BCrypt 암호화
            member.setPw(bCryptPasswordEncoder.encode(member.getPw()));

            ResponseSignUpDTO memberSignUpDTO = memberService.signUp(member);
            Map<String, Object> responseMap = new HashMap<>();
            ResponseMessage responseMessage = new ResponseMessage();

            HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

            if (memberSignUpDTO.isBanMember()) {
                responseMessage.setMessage(member.getName() + " 회원님은 가입이 제한된 회원 입니다.");
                responseMap.put("memberInfo", memberSignUpDTO.getBlackListDTO());
            } else if (memberSignUpDTO.isDuplicateEmail()) {
                responseMessage.setMessage(member.getEmail() + " 이미 존재하는 이메일입니다. 이메일 주소를 확인해 주세요");
            } else if (memberSignUpDTO.isExistingMember()) {
                responseMessage.setMessage(member.getName() + " 님은 이미 존재하는 회원 정보입니다.");
            } else {
                httpStatus = HttpStatus.OK;
                responseMap.put("회원 정보", memberSignUpDTO.getMemberDTO());
                responseMessage.setMessage("회원가입이 완료 되었습니다.");
            }

            if (!responseMap.isEmpty())
                responseMessage.setResult(responseMap);

            responseMessage.setHttpStatus(httpStatus.value());
            return ResponseEntity.status(httpStatus)
                    .body(responseMessage);
        } catch (IllegalArgumentException e) {
            ResponseMessage responseMessage =
                    new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "필수 입력 정보가 누락 됐습니다.", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseMessage);
        }
    }

    @DeleteMapping("/member")
    public ResponseEntity<ResponseMessage> deleteMember(@RequestBody RequestQuitMemberDTO member) {
        member.setMemPwd(bCryptPasswordEncoder.encode(member.getMemPwd()));

        ResponseQuitDTO responseQuitDTO = memberService.memberQuit(member);
        ResponseMessage responseMessage = new ResponseMessage();

        if (responseQuitDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (responseQuitDTO.isInvalidPwd()) {
            responseMessage.setMessage("암호가 틀렸습니다.");
        } else {
            responseMessage.setMessage("회원 탈퇴가 완료 됐습니다.");
        }

        responseMessage.setHttpStatus(HttpStatus.OK.value());

        return ResponseEntity.ok()
                .body(responseMessage);
    }





    @PostMapping("/Profile/{id}")
    public ResponseEntity<ResponseMessage> updateProfileImage(
            @RequestParam MultipartFile singleFile,
            @PathVariable Long id,
            HttpServletRequest request) {
        ResponseProfileImageDTO success = memberService.updateProfileImage(singleFile, id,request);


        Map<String, Object> responseMap = new HashMap<>();
        ResponseMessage responseMessage = new ResponseMessage();

        if (success.isSuccessUpload() == true) {
            responseMessage.setMessage("프로필 변경 완료");
            responseMap.put("responseData", success);
            responseMessage.setHttpStatus(HttpStatus.OK.value());
        } else {
            responseMessage.setMessage("프로필 변경 실패");
            responseMessage.setHttpStatus(HttpStatus.EXPECTATION_FAILED.value());
        }

        if (!responseMap.isEmpty())
            responseMessage.setResult(responseMap);


        return ResponseEntity.ok()
                .body(responseMessage);
    }

    @PostMapping("/password-info")
    public ResponseEntity<ResponseMessage> checkPassword(@RequestBody CheckPasswordDTO checkPasswordDTO) {
        boolean ableToPassword = memberService.checkPassowrd(checkPasswordDTO);

        ResponseMessage responseMessage = new ResponseMessage();
        HttpStatus httpStatus = ableToPassword ? HttpStatus.OK :HttpStatus.BAD_REQUEST;

        if(ableToPassword)
        {
            responseMessage.setHttpStatus(httpStatus.value());
            responseMessage.setMessage("password 일치");
        }
        else {
            responseMessage.setHttpStatus(httpStatus.value());
            responseMessage.setMessage("password 불 일치");
        }

        return ResponseEntity
                .status(httpStatus)
                .body(responseMessage);
    }

    @PostMapping("/member-password")
    public ResponseEntity<ResponseMessage> modifyPassword(@RequestBody ModifyPasswordDTO modifyPasswordDTO) {

        boolean ableToPassword = memberService.modifyPassword(modifyPasswordDTO);

        ResponseMessage responseMessage = new ResponseMessage();
        HttpStatus httpStatus = ableToPassword ? HttpStatus.OK :HttpStatus.BAD_REQUEST;

        if(ableToPassword)
        {
            responseMessage.setHttpStatus(httpStatus.value());
            responseMessage.setMessage("password 변경 완료");
        }
        else {
            responseMessage.setHttpStatus(httpStatus.value());
            responseMessage.setMessage("password 변경 실패");
        }

        return ResponseEntity
                .status(httpStatus)
                .body(responseMessage);
    }




}

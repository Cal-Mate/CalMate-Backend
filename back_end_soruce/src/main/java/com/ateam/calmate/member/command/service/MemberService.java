package com.ateam.calmate.member.command.service;

import com.ateam.calmate.member.command.dto.*;
import com.ateam.calmate.member.command.entity.Member;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    ResponseMemberDTO findMember(Long id);


    ResponseSignUpDTO signUp(RequestMemberDTO member);

    ResponseQuitDTO memberQuit(RequestQuitMemberDTO member);

    UserDetails loadUserByUsername(String email);

    void updateInvlidPassword(RequsetloginHisotry user);

    void updateCompleteLogin(RequsetloginHisotry loginHistory);


    boolean updateStatus(Long id, ReportMemberUpdateDTO dto);


    ResponseProfileImageDTO updateProfileImage(MultipartFile singleFile, Long id, HttpServletRequest request);

    boolean checkPassowrd(CheckPasswordDTO checkPasswordDTO);

    boolean modifyPassword(ModifyPasswordDTO modifyPasswordDTO);

    void calculatePoint(Long member_id, int id);

    void modifyOfData(RequestModifyDTO modifiedData);
}

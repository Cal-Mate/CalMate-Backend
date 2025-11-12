package com.ateam.calmate.member.command.service;


import com.ateam.calmate.member.command.dto.*;
import com.ateam.calmate.member.command.entity.*;
import com.ateam.calmate.member.command.entity.compositeKeys.MemberAuthorityPK;
import com.ateam.calmate.member.command.repository.*;
import com.ateam.calmate.member.query.dto.BlackListDTO;
import com.ateam.calmate.member.command.dto.RequestLoginwithAuthoritiesDTO;
import com.ateam.calmate.member.query.service.MemberQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberQueryService memberQueryService;
    private final ModelMapper modelMapper;
    private final LoginFailureRecordRepository loginFailureRecordRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthorityRepository authorityRepository;
    private final ProfileImageService profileImageService;
    private final ProfileImageRepository profileImageRepository;
    private final MemberAuthorityRepository memberAuthorityRepository;
    private final BaseOfPointRepository baseOfPointRepository;
    private final PointRepository pointRepository;
    private final PasswordEncoder passwordEncoder;  //평문과 암호화 된 다이제스트를 비교하기 위한 도구

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository
            , MemberQueryService memberQueryService
            , ModelMapper modelMapper
            , BCryptPasswordEncoder bCryptPasswordEncoder
            , LoginFailureRecordRepository loginFailureRecordRepository
            , LoginHistoryRepository loginHistoryRepository
            , AuthorityRepository authorityRepository
            , ProfileImageService profileImageService
            , ProfileImageRepository profileImageRepository
            , PasswordEncoder passwordEncoder
            , MemberAuthorityRepository memberAuthorityRepository
            , BaseOfPointRepository baseOfPointRepository
            , PointRepository pointRepository) {
        this.memberRepository = memberRepository;
        this.memberQueryService = memberQueryService;
        this.modelMapper = modelMapper;
        this.loginFailureRecordRepository = loginFailureRecordRepository;
        this.loginHistoryRepository = loginHistoryRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authorityRepository = authorityRepository;
        this.profileImageService = profileImageService;
        this.profileImageRepository = profileImageRepository;
        this.passwordEncoder = passwordEncoder;
        this.memberAuthorityRepository = memberAuthorityRepository;
        this.baseOfPointRepository = baseOfPointRepository;
        this.pointRepository = pointRepository;
    }

    @Override
    public ResponseMemberDTO findMember(Long id) {

        Member member = memberRepository.findById(id).orElse(null);
        ResponseMemberDTO responseMemberDTO = member != null ?
                modelMapper.map(member, ResponseMemberDTO.class) : null;


        return responseMemberDTO;
    }

    //회원 가입
    @Override
    @Transactional
    public ResponseSignUpDTO signUp(RequestMemberDTO member) {
        return checkBeforeSignUp(member);
    }

    //회원 탈퇴
    @Override
    @Transactional
    public ResponseQuitDTO memberQuit(RequestQuitMemberDTO memberDTO) {
        ResponseQuitDTO responseQuitDTO = new ResponseQuitDTO();
        Member member = memberRepository.findById(memberDTO.getId()).orElse(null);

        if (member == null) { //테스트중에는 값을 잘못 입력해서 null 이 나올수 있어서 처리 함.
            return null;
        }

        //암호가 틀렸을 때
        if (bCryptPasswordEncoder.matches(member.getPw(), memberDTO.getMemPwd())) {
            responseQuitDTO.setInvalidPwd(true);
            return responseQuitDTO;
        }

        member.setQuitDate(LocalDateTime.now());
        member.setId(2L);
        memberRepository.save(member);

        return responseQuitDTO;
    }

    //spring security에서 회원정보 조회에 사용
    @Override
    public UserDetails loadUserByUsername(String email) {
        com.ateam.calmate.member.query.dto.RequestLoginwithAuthoritiesDTO loginDTO =
                memberQueryService.findMemberWithAuthoriesByEmail(email);

        if (loginDTO == null)
            return null;

        //계정 권한
        List<GrantedAuthority> grantedAuthorities
                = loginDTO.getAuthorities().stream()
                .map(x -> new SimpleGrantedAuthority(x.getAuthName()))
                .collect(Collectors.toList());

        UserImpl userImpl =
                new UserImpl(
                        loginDTO.getEmail(),
                        loginDTO.getPwd(),
                        grantedAuthorities
                );
        userImpl.setDetails(modelMapper.map(loginDTO, RequestLoginwithAuthoritiesDTO.class));

        return userImpl;
    }

    //로그인 실패시 실패이력 insert 및 회원테이블 실패 횟 수 업데이트
    @Override
    @Transactional
    public void updateInvlidPassword(RequsetloginHisotry requestLoginHisotry) {
        Member member = memberRepository.findById(requestLoginHisotry.getCumId()).orElse(null);
        if (member == null)
            return;

        LoginFailureRecord loginFailureRecord = new LoginFailureRecord(
                null
                , requestLoginHisotry.getDateTime()
                , requestLoginHisotry.getClientIp()
                , requestLoginHisotry.getReason()
                , member.getId()
        );

        int failCount = member.getLoginFailureCount() == null
                ? 1 : member.getLoginFailureCount() + 1;

        //연속 오 입력이 6이 됐다는건 15분 지나고 또 틀렸기 때문 .
        // 1로 바꾸지 않으면 바로 접속 제한 걸려서 1부터 다시 카운트 시작
        failCount = failCount >= 6 ? 1 : failCount;

        member.setLoginFailureCount(failCount);


        if (failCount >= 5) {
            LocalDateTime loginLockUnitlby = LocalDateTime.now().plusMinutes(15);
            member.setLoginLockUntil(loginLockUnitlby); // 15분간 접속 불가
        }
        loginFailureRecordRepository.save(loginFailureRecord);
        memberRepository.save(member);
    }

    //정상 로그인 hitory 기록
    @Override
    @Transactional
    public void updateCompleteLogin(RequsetloginHisotry loginHistory) {
        LoginHistory loginRecord = new LoginHistory(
                null,
                loginHistory.getDateTime(),
                loginHistory.getClientIp(),
                "",
                loginHistory.getCumId()
        );

        Member member = memberRepository.findById(loginHistory.getCumId()).orElse(null);
        member.setLoginFailureCount(0);
//        member.setLastLogin(loginRecord.getDate());
        memberRepository.save(member);
        loginHistoryRepository.save(loginRecord);
    }

    @Override
    public boolean updateStatus(Long id, ReportMemberUpdateDTO dto) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isEmpty()) return false;

        Member member = optionalMember.get();

        if (dto.getMemStsId() != null) {
            member.setId(dto.getMemStsId());
        }
        if (dto.getBanCnt() != null) {
            member.setBanCnt(dto.getBanCnt());
        }

        memberRepository.save(member);
        return true;
    }

    @Override
    @Transactional
    public ResponseProfileImageDTO updateProfileImage(MultipartFile singleFile, Long id, HttpServletRequest request) {
        ResponseProfileImageDTO responseProfileImageDTO = null;
        try {
            responseProfileImageDTO = profileImageService.updateProfileImage(singleFile, id, request);
            UploadFile findProfile = profileImageRepository.findByMemberId(id);

            //파일이 없었을때
            if (findProfile == null) {
                findProfile = modelMapper.map(responseProfileImageDTO, UploadFile.class);
            } else { // 기존 파일이있어서 덮어 쓰기
                findProfile.setReFileName(responseProfileImageDTO.getReFileName());
                findProfile.setOriginalFileName(responseProfileImageDTO.getOriginalFileName());
            }
            profileImageRepository.save(findProfile);


        } catch (IOException e) {
            if (responseProfileImageDTO == null) {
                responseProfileImageDTO = new ResponseProfileImageDTO();
                responseProfileImageDTO.setSuccessUpload(false);
                responseProfileImageDTO.setExceptionMessage("프로필 변경 실패");
                log.info(e.getMessage());
            }
        }

        return responseProfileImageDTO;
    }


    @Override
    public boolean checkPassowrd(CheckPasswordDTO checkPasswordDTO) {
        /* 설명. DB에 있는 해당 회원의 정보 */
        Member member = memberRepository.findById(checkPasswordDTO.getId()).orElse(null);
        if (member == null || !passwordEncoder.matches(checkPasswordDTO.getPassword(), member.getPw())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean modifyPassword(ModifyPasswordDTO modifyPasswordDTO) {
        Member member = memberRepository.findById(modifyPasswordDTO.getId()).orElse(null);

        if (member == null || !passwordEncoder.matches(modifyPasswordDTO.getOldPassword(), member.getPw())) {
            return false;
        }
        // BCrypt 암호화
        modifyPasswordDTO.setNewPassword(bCryptPasswordEncoder.encode(modifyPasswordDTO.getNewPassword()));

        member.setPw(modifyPasswordDTO.getNewPassword());
        memberRepository.save(member);

        return true;
    }



    // 회원 가입 제한 사항 확인, 이메일 중복, 이미 가입한 회원, 블랙리스트 등등
    private ResponseSignUpDTO checkBeforeSignUp(RequestMemberDTO memberDTO) throws IllegalArgumentException {

        ResponseSignUpDTO responseSignUpDTO = new ResponseSignUpDTO();
        // 중복된 email 혹은 이미 가입된 회원이 있는지조회(이미 가입된 회원 조건은 이름 && 생일이 같은 사람이있는지로 판단)
        List<Member> findMembers
                = memberRepository.findByEmailOrNameAndBirth(memberDTO.getEmail(), memberDTO.getName(), memberDTO.getBirth());

        Member findMember = null;
        if (findMembers.size() > 0) findMember = findMembers.get(0);
//                = memberRepository.findByEmailOrNameAndBirth(memberDTO.getEmail(), memberDTO.getName(), memberDTO.getBirth());

        List<AuthorityList> authorityList
                = authorityRepository.findAll().stream()
                .sorted(Comparator.comparing(AuthorityList::getId))
                .toList();

        if (findMember != null) {
            //중복 이메일이 있는지 확인
            if (findMember.getEmail().equals(memberDTO.getEmail())) {
                responseSignUpDTO.setDuplicateEmail(true);
                return responseSignUpDTO;
            } else if (findMember.getName().equals(memberDTO.getName())) {
                responseSignUpDTO.setExistingMember(true);
                return responseSignUpDTO;
            }

            if (findMember != null) {
                BlackListDTO blackListDTO = memberQueryService.findBlakListByMemberId(findMember.getId());

                // null이 아니면 블랙리스트라 가입 거절
                if (blackListDTO != null) {
                    responseSignUpDTO.setBanMember(true);
                    responseSignUpDTO.setBlackListDTO(blackListDTO);
                    return responseSignUpDTO;
                }

            }

        } else {
            try {

                Member member = modelMapper.map(memberDTO, Member.class);
                member.setStatus(1L); //회원 상태 정상 상태로 초기 셋팅
                member.setLevel(1L); //회원 상태 정상 상태로 초기 셋팅
                member.setCreatedAt(LocalDateTime.now());
                member = memberRepository.save(member);

                calculatePoint(member, 2);

                responseSignUpDTO.setMemberDTO(modelMapper.map(member, ResponseMemberDTO.class));

                //기본 Member 권한 부여
                memberAuthorityRepository.save(new MemberAuthority(new MemberAuthorityPK(member.getId(), authorityList.get(1).getId())));
            } catch (Exception e) {
                throw new IllegalArgumentException("Member not Exception");
            }
        }

        return responseSignUpDTO;
    }


    @Override
    public void calculatePoint(Long member_id, int id) {
        Member member = memberRepository.findById(member_id).orElse(null);

        calculatePoint(member, id);
    }

    // 포인트 연산
    private void calculatePoint(Member member, int id) {

        BaseOfPoint baseOfPoint = baseOfPointRepository.findById(id).orElse(null);

        if (baseOfPoint != null) {
            int point = baseOfPoint.getPoint();
            //로그인시 포인트
            if (baseOfPoint.getId() == 1) {
                LocalDate today = LocalDate.now();
                LocalDateTime startOfDay = today.atStartOfDay();           // 2025-11-10 00:00:00
                LocalDateTime endOfDay   = today.plusDays(1).atStartOfDay(); // 2025-11-11 00:00:00
                List<LoginHistory> loginHistories =
                        loginHistoryRepository.findLoginHistory(startOfDay,endOfDay, member.getId());

                //당일 첫 로그인시만 포인트 흭득
                if(loginHistories.size() == 0) {
                    // 포인트 흭득 기록
                    Point pointHistroy = new Point();
                    pointHistroy.setPoint(point);
                    pointHistroy.setMemberId(member.getId());
                    pointHistroy.setDistinction(Point.Distinction.EARN);

                    pointRepository.save(pointHistroy);
                    member.setPoint(member.getPoint() + point);
                    memberRepository.save(member);
                }
            } else if (baseOfPoint.getId() == 2) { // 회원가입시 포인트
                member.setPoint(point);

                // 포인트 흭득 기록
                Point pointHistroy = new Point();
                pointHistroy.setPoint(point);
                pointHistroy.setMemberId(member.getId());
                pointHistroy.setDistinction(Point.Distinction.EARN);

                pointRepository.save(pointHistroy);
                memberRepository.save(member);
            }

        }

    }


}
package com.Javapractice.member_test.service;

import com.Javapractice.member_test.dto.MemberDTO;
import com.Javapractice.member_test.entity.MemberEntity;
import com.Javapractice.member_test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void save(MemberDTO memberDTO) { // Controller에서 받아온 DTO를 가지고 Repository의 save 메소드 호출
        // 조건. entity 객체로 넘겨줘야함
        // 1. dto -> entity 변환
        // 2. repository의 save 메소드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);    // JPARepository의 save함수 -> insert 쿼리를 만들어줬다.
    }    // spring이 관리해주는 객체

    public MemberDTO login(MemberDTO memberDTO) {
        // 1. 입력한 이메일로 DB조회
        // 2. 그 이메일에 해당되는 비밀번호가 입력 비밀번호와 일치하는지 판단
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        // 데이터베이스에 이메일이 있는지 확인하는 작업을 Optional 객체로
        if (byMemberEmail.isPresent()) {
            // 조회 결과가 있다(해당 이메일을 가진 회원 정보가 있다)
            MemberEntity memberEntity = byMemberEmail.get();
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 좌항 : 데이터베이스에 있는 비밀번호, 우항 : 입력한 비밀번호
                // 비밀번호 일치
                // entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                // 비밀번호 불일치(로그인실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();   // Repository에서 가져오기 때문에 Entity객체로 가져온다
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberEntity memberEntity: memberEntityList) { // Entity객체를 DTO 객체로 넘겨준다.
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if(optionalMemberEntity.isPresent()){
            // 조회한 데이터가 존재한다면
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }
        else{
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if(optionalMemberEntity.isPresent()){
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        }
        else{
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
        // ID도 같이 넘어온 상황이기 때문에 DTO값을 Entity로 바꿀 때 ID도 같이 가도록 설정
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    public String emailCheck(String memberEmail) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberEmail);
        if (byMemberEmail.isPresent()) {
            // 조회결과가 있다 -> 사용할 수 없다.
            return null;
        } else {
            // 조회결과가 없다 -> 사용할 수 있다.
            return "ok";
        }
    }
}

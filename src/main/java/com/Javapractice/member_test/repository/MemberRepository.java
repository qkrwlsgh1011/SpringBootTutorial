package com.Javapractice.member_test.repository;

import com.Javapractice.member_test.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    // 이메일로 정보 조회 interface이기 때문에 추상으로
    Optional<MemberEntity> findByMemberEmail(String memberEmail);

}

package ansan.Domain.Entity.Member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    //엔티티 검색 findby필드명
    Optional<MemberEntity> findBymid(String mid);







    
}

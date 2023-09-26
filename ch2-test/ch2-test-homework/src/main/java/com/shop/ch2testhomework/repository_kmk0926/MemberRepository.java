package com.shop.ch2testhomework.repository_kmk0926;


import com.shop.ch2testhomework.entity_kmk0926.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// 레거시 ,
// 동네 1 ~ 4, 비유,
// DAO (Repository) -> JPA -> ORM 기술 사용중, -> Hibernate 구현체 이용해서.
// 인터페이스에 있는 기본 쿼리 메소드를 이용해서, save, count, delete 기본 기능.
// 단위 테스트에 사용할 테스트 디비 H2 디비 사용함.
// 임시로 사용하기 위해서, 메모리 위에 작업을 한다.
// 기본설정 파일과, 테스트를 위한 설정 파일을 2개 분리해서 작업 중.

//public interface ItemRepository extends JpaRepository<Item, Long> {
//    public interface MemberRepository extends JpaRepository<Member, Long>,
//        QuerydslPredicateExecutor<Member> {
public interface MemberRepository extends JpaRepository<Member, Long> {
//
//    여러개의 쿼리 메소드 중에서, 조회 부분 보고,
//    조회하는 옵션을 하나씩 볼 예정
//    findByUserNm.
    List<Member> findByUserNm(String userNm);
    List<Member> findByUserDescription(String userDescription);

    List<Member> findByUserNmOrUserDescription(String userNm, String userDescription);


// 통계, 조회 조건이 복잡하면, 쿼리 메소드로만 작업이 조금 어려움.
    // JPQL 문법을 이용해서, 표준 SQL을 사용함.
    // 문법이 너무 복잡하면, nativeSQL 속성을 사용해서, 그대로 사용.
    // 단점. 인텔리 제이라서, 문자열 안의 문법을 체크를 해주는데 원래, 안해줌.
    // 이 단점, 해결도하고, 자바 컴파일러의 문법 체크의 도움도 받기 위해서
    // queryDSl 를 사용을 함.
    @Query("select m from Member m where m.userDescription like " +
            "%:userDescription% order by m.regTime desc")
    List<Member> findByItemDetail(@Param("userDescription") String userDescription);

//    @Query(value="select * from item i where i.item_detail like " +
//            "%:itemDetail% order by i.price desc", nativeQuery = true)
//    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
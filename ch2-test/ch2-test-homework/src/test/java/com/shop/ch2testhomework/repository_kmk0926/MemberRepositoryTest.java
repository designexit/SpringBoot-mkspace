package com.shop.ch2testhomework.repository_kmk0926;

import com.shop.ch2testhomework.constant_kmk0926.UserRole;
import com.shop.ch2testhomework.entity_kmk0926.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
// 테스트를 하기위한 , 설정 파일을 분리했고, 로드.
    // 설정 파일이 주석이 되면, 기본 설정 파일이 로드가 된다.
    // 기본 설정 파일은 mysql 로 등록이 되어 있다.
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("멤버 저장 테스트")
    public void createItemTest() {
        Member member = new Member();
        member.setUserNm("이상용");
        member.setUserDescription("실습 풀이중");
        member.setUserEmail("lsy@naver.com");
        member.setUserRole(UserRole.ADMIN);
        member.setRegTime(LocalDateTime.now());

        //itemRepository 이용해서, 자바 -> 디비, 샘플디비 하나 생성
        // 영속성 컨텍스트 = 중간 저장소, 1차 캐시 테이블
        Member savedMember = memberRepository.save(member);
        // H2, 실제 파일기반으로 내용을 저장이 아니라.
        // 메모리에 임시로 작업을 해서, 저장이 안됨.
        System.out.print(savedMember.toString());
    }

    public void createMemberList(){
        for(int i=1;i<=10;i++){
            Member member = new Member();
            member.setUserNm("이상용"+i);
            member.setUserDescription("실습 풀이중" +i);
            member.setUserEmail("lsy"+i+"@naver.com");
            member.setUserRole(UserRole.ADMIN);
            member.setRegTime(LocalDateTime.now());
            Member savedMember = memberRepository.save(member);

        }
    }

    @Test
    void findByUserNm() {

        this.createMemberList();
        List<Member> memberList = memberRepository.findByUserNm("이상용1");
        for(Member member : memberList){
            System.out.println(member.toString());
            System.out.println("이름 확인 해보기 : "+member.getUserNm());
        }
    }

    @Test
    void findByUserDescription() {

        this.createMemberList();
        List<Member> memberList = memberRepository.findByUserDescription("실습 풀이중1");
        for(Member member : memberList){
            System.out.println(member.toString());
            System.out.println("이름 확인 해보기 : "+member.getUserNm());
        }
    }

    @Test
    @DisplayName("유저명, 유저소개 or 테스트")
    // H2 데이터베이스를 이용해서, 메모리 상에서, 단위 테스트 기능을 확인중.
    // 메모리 상에 작업중이니, 당연히 MySQL 디비와는 관계없음.
    public void findByUserNmOrUserDescription(){
        // 매번 테스트 할 때 마다, 더미 데이터 10 개 생성
        this.createMemberList();
        // 확인.  조회 조건 확인 중. or
        List<Member> memberList = memberRepository.findByUserNmOrUserDescription("이상용1", "실습 풀이중3");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 유저 조회 테스트")
    public void findByUserDescription(){
        this.createMemberList();
        List<Member> memberList =
                memberRepository.findByUserDescription("실습 풀이중7");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상유저 조회 테스트 native 속성 사용")
    public void findByUserDescriptionTest(){
        //this.createMemberList();
        List<Member> memberList =
                memberRepository.findByUserDescriptionByNative("실습 풀이중7");
        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createMemberList2();
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember qMember = QMember.member;
        JPAQuery<Member> query  = queryFactory.selectFrom(qMember)
                .where(qMember.userRole.eq(UserRole.USER))
                .where(qMember.userDescription.like("%" + "실습 풀이중" + "%"))
                .orderBy(qMember.regTime.desc());

        List<Member> memberList = query.fetch();

        for(Member member : memberList){
            System.out.println(member.toString());
        }
    }

    public void createMemberList2(){
        for(int i=1;i<=5;i++){
            Member member = new Member();
            member.setUserNm("이상용"+i);
            member.setUserDescription("실습 풀이중" +i);
            member.setUserEmail("lsy"+i+"@naver.com");
            member.setUserRole(UserRole.ADMIN);
            member.setRegTime(LocalDateTime.now());
            memberRepository.save(member);
        }

        for(int i=6;i<=10;i++){
            Member member = new Member();
            member.setUserNm("이상용"+i);
            member.setUserDescription("실습 풀이중" +i);
            member.setUserEmail("lsy"+i+"@naver.com");
            member.setUserRole(UserRole.USER);
            member.setRegTime(LocalDateTime.now());
            memberRepository.save(member);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){

        this.createItemList3();
// 빌드 패턴으로, 쿼리에 관련된 옵션을 담을 인스턴스
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMember member = QMember.member;
        String userDescription = "테스트 상품 상세 설명";
        String memberUserRole = "ADMIN";

        booleanBuilder.and(member.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(member.price.gt(price));
        System.out.println(ItemSellStatus.SELL);
        if(StringUtils.equals(Q, ItemSellStatus.SELL)){
            booleanBuilder.and(member.itemSellStatus.eq(ItemSellStatus.SELL));
        }



        // 부트, 페이징을 처리하기 위해서,
        // 시스템으로 자주 반복되는 기능중에 하나인 페이징 처리 쉽게 해주는 클래스.
        // ex) 0 페이지 -> 1페이지, size : 한 페이지에 보여주는 갯수

        Pageable pageable = PageRequest.of(0, 5);
        Page<Member> itemPagingResult = memberRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult. getTotalElements ());

        List<Member> resultItemList = itemPagingResult.getContent();
        for(Member resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }
    }

}
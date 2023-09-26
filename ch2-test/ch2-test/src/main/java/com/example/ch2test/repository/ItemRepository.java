package com.example.ch2test.repository;


import com.example.ch2test.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
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
    public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {
//
//    여러개의 쿼리 메소드 중에서, 조회 부분 보고,
//    조회하는 옵션을 하나씩 볼 예정.
    List<Item> findByItemNm(String itemNm);
//
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
//
    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    List<Item> findByPriceLessThanOrderByPriceAsc(Integer price);

    @Query("select i from Item i where i.itemDetail like " +
            "%:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
//
    @Query(value="select * from item i where i.item_detail like " +
            "%:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
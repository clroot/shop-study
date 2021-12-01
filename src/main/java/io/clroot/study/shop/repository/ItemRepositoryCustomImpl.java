package io.clroot.study.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.clroot.study.shop.constant.ItemSellStatus;
import io.clroot.study.shop.dto.ItemSearchDTO;
import io.clroot.study.shop.dto.MainItemDTO;
import io.clroot.study.shop.dto.QMainItemDTO;
import io.clroot.study.shop.entity.Item;
import io.clroot.study.shop.entity.QItem;
import io.clroot.study.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDateAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals(searchDateType, "all") || searchDateType == null) {
            return null;
        } else if (StringUtils.equals(searchDateType, "1d")) {
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals(searchDateType, "1w")) {
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals(searchDateType, "1m")) {
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals(searchDateType, "6m")) {
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals(searchBy, "itemName")) {
            return QItem.item.itemName.like("%" + searchQuery + "%");
        } else if (StringUtils.equals(searchBy, "createdBy")) {
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    private BooleanExpression itemNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null :
                QItem.item.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDateAfter(itemSearchDTO.getSearchDateType()),
                        searchSellStatusEq(itemSearchDTO.getSearchSellStatus()),
                        searchByLike(itemSearchDTO.getSearchBy(), itemSearchDTO.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainItemDTO> getMainItemPage(ItemSearchDTO itemSearchDTO, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDTO> results = queryFactory
                .select(new QMainItemDTO(
                        item.id,
                        item.itemName,
                        item.itemDescription,
                        itemImg.imgUrl,
                        item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repImg.isTrue())
                .where(itemNameLike(itemSearchDTO.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDTO> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}

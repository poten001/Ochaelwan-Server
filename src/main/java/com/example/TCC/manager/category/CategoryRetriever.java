package com.example.TCC.manager.category;

import com.example.TCC.domain.Category;
import com.example.TCC.exception.NotFoundException;
import com.example.TCC.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryRetriever {

    private final CategoryRepository categoryRepository;

    public Category findByCategoryName(final String categoryName) {
        return categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new NotFoundException("카테고리를 찾을 수 없습니다."));
    }
}

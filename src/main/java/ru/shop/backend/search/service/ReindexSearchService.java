package ru.shop.backend.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shop.backend.search.model.ItemElastic;
import ru.shop.backend.search.repository.ItemDbRepository;
import ru.shop.backend.search.repository.ItemRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReindexSearchService {

    private final ItemDbRepository dbRepository;
    private final ItemRepository searchRepository;

    @Scheduled(fixedDelay = 43200000)
    @Transactional
    public void reindex() {
        log.info("Генерация индексов по товарам запущена");

        dbRepository.findAllInStream()
                .parallel()
                .map(ItemElastic::new)
                .forEach(searchRepository::save);

        log.info("Генерация индексов по товарам завершена");
    }
}

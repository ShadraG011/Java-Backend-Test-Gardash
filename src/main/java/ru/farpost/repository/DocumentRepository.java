package ru.farpost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.farpost.model.Document;

/**
 * Репозиторий для работы с сущностью {@link Document}.
 * Обеспечивает доступ к данным документов в базе данных через {@link JpaRepository}.
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}

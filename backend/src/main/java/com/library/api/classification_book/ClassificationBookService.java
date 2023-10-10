package com.library.api.classification_book;

import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ClassificationBookService {

    private final ClassificationBookRepository classificationBookRepository;

    public ClassificationBookResponse createClassificationBook(ClassificationBookRequest request) {

        Optional<ClassificationBook> optionalClassificationBook = classificationBookRepository
                .findByName(request.name());

        if (optionalClassificationBook.isPresent()) {
            throw new ObjectAlreadyExistsWithException("classification book", "name", request.name());
        }

        ClassificationBook classificationBook = ClassificationBook.builder()
                .name(request.name())
                .build();

        classificationBook = classificationBookRepository.save(classificationBook);

        return ClassificationBookResponse.builder()
                .key(classificationBook.getId())
                .name(classificationBook.getName())
                .build();
    }
}

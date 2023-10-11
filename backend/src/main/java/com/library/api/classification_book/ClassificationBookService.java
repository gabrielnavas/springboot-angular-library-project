package com.library.api.classification_book;

import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import com.library.api.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    public List<ClassificationBookResponse> getAllClassificationBooks(Map<String, Object> filters, Pageable pageable) {
        String name = ((String) filters.get("name"));

        if (name.length() > 0) {
            return classificationBookRepository.findAllByName(name, pageable)
                    .map(cb -> ClassificationBookResponse.builder()
                            .name(cb.getName())
                            .key(cb.getId())
                            .build()
                    )
                    .toList();
        } else {
            return classificationBookRepository.findAll(pageable)
                    .stream()
                    .map(cb -> ClassificationBookResponse.builder()
                            .name(cb.getName())
                            .key(cb.getId())
                            .build()
                    )
                    .toList();
        }
    }


    public ClassificationBookResponse getClassificationBookById(UUID id) {
        Optional<ClassificationBook> optionalClassificationBook = classificationBookRepository.findById(id);
        if (optionalClassificationBook.isEmpty()) {
            throw new ObjectNotFoundException("classification book");
        }

        ClassificationBook classificationBook = optionalClassificationBook.get();

        return ClassificationBookResponse.builder()
                .key(classificationBook.getId())
                .name(classificationBook.getName())
                .build();
    }


    public void updatePartialsClassificationBookById(UUID id, ClassificationBookRequest request) {
        Optional<ClassificationBook> optionalClassificationBookExists = classificationBookRepository.findById(id);
        if (optionalClassificationBookExists.isEmpty()) {
            throw new ObjectNotFoundException("classification book");
        }

        Optional<ClassificationBook> optionalClassificationBook =
                classificationBookRepository.findByName(request.name());
        if (optionalClassificationBook.isPresent() && !optionalClassificationBook.get().getId().equals(id)) {
            throw new ObjectAlreadyExistsWithException("classification book", "name", request.name());
        }

        ClassificationBook classificationBook = optionalClassificationBookExists.get();
        classificationBook.setName(request.name());

        classificationBookRepository.save(classificationBook);
    }
}

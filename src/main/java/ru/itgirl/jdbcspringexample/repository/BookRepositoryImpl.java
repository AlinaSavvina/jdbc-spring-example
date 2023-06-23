package ru.itgirl.jdbcspringexample.repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.itgirl.jdbcspringexample.model.Book;
import java.util.List;
import java.util.Optional;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> findAllBooks() {
        String query = "SELECT id, name FROM books";
        return jdbcTemplate.query(query, (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Book(id, name);
        });
    }

    @Override
    public Optional<Book> findById(Long id) {
        String query = "SELECT id, name FROM books WHERE id = ?";
        Object[] params = { id };
        RowMapper<Book> rowMapper = (resultSet, rowNum) -> {
            Long bookId = resultSet.getLong("id");
            String bookName = resultSet.getString("name");
            return new Book(bookId, bookName);
        };
        List<Book> books = jdbcTemplate.query(query, params, rowMapper);
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }
}

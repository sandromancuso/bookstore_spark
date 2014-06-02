package unit.core.com.codurance.infrastructure.repository;

import com.codurance.infrastructure.repositories.InMemoryLibrary;
import com.codurance.model.book.Book;
import org.junit.Before;
import org.junit.Test;
import org.omg.PortableInterceptor.NON_EXISTENT;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static unit.core.com.codurance.model.book.BookBuilder.aBook;

public class InMemoryLibraryShould {

	public static final Book BOOK_A = aBook().withId(1).withName("Book A").build();
	public static final Book BOOK_B = aBook().withId(2).withName("Book B").build();
	public static final Book BOOK_BLAH = aBook().withName("Blah").build();
	private static final Integer NON_EXISTENT_ID = -1;
	private InMemoryLibrary library;

	@Before
	public void initialise() {
		library = new InMemoryLibrary();
	}

	@Test public void
	add_a_new_book() {
		Book book = aBook().build();

		library.add(book);

	    assertThat(library.allBooks().size(), is(1));
		assertThat(library.allBooks().get(0), is(book));
	}
	
	@Test public void
	not_return_book_when_there_is_no_book_matching_specified_id() {
		Optional<Book> book = library.bookById(NON_EXISTENT_ID);

	    assertThat(book.isPresent(), is(false));
	}

	@Test public void
	return_a_book_with_a_matching_id() {
		library.add(BOOK_A);
		library.add(BOOK_B);

		Optional<Book> book = library.bookById(BOOK_B.getId());

		assertThat(book.get(), is(BOOK_B));
	}

	@Test public void
	return_1_as_next_book_id_when_there_are_no_books() {
	    assertThat(library.nextBookId(), is(1));
	}

	@Test public void
	return_the_max_id_plus_one_as_next_book_id() {
		library.add(BOOK_A);
		library.add(BOOK_B);

	    assertThat(library.nextBookId(), is(3));
	}

	@Test public void
	return_an_empty_list_when_no_books_match_specified_name() {
		library.add(BOOK_A);

		List<Book> result = library.booksWhichNameContains("NON_EXISTENT_TITLE");

	    assertThat(result.isEmpty(), is(true));
	}

	@Test public void
	return_books_that_match_specified_name() {
		library.add(BOOK_A);
		library.add(BOOK_BLAH);
		library.add(BOOK_B);

		List<Book> result = library.booksWhichNameContains("ook");

	    assertThat(result.size(), is(2));
	    assertThat(result.contains(BOOK_A), is(true));
	    assertThat(result.contains(BOOK_B), is(true));
	}


}

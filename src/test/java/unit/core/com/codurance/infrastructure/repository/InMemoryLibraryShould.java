package unit.core.com.codurance.infrastructure.repository;

import com.codurance.infrastructure.repositories.InMemoryLibrary;
import com.codurance.model.book.Book;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static unit.core.com.codurance.model.book.BookBuilder.aBook;

public class InMemoryLibraryShould {

	public static final Book BOOK_A = aBook().withId(1).withName("Book A").build();
	public static final Book BOOK_B = aBook().withId(2).withName("Book B").build();
	public static final Book BOOK_BLAH = aBook().withName("Blah").build();
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

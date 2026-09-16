package com.mxraven.admin;

import java.io.UncheckedIOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A cursor-paginated collection that hides pagination from the caller. It is an
 * {@link Iterable}, so it can be used directly in a for-each loop, and it also
 * exposes {@link #stream()} and {@link #toList()}:
 *
 * <pre>{@code
 * for (Domain domain : ws.domains().list()) {
 *     System.out.println(domain.domainName());
 * }
 *
 * List<Domain> all = ws.domains().list().toList();
 * }</pre>
 *
 * <p>The first page is fetched when the owning {@code list(...)} call is made.
 * Subsequent pages are fetched lazily, only as the iterator advances, so a large
 * collection costs nothing beyond the pages you actually consume. A transport
 * failure while advancing pages is thrown as {@link UncheckedIOException}; the
 * initial page keeps the method's checked {@code IOException}.
 *
 * <p>The raw first page and its opaque tokens remain available through
 * {@link #firstPage()} for callers that need page-level control.
 */
public final class Paged<T> implements Iterable<T> {
    private final Page<T> firstPage;
    private final Function<String, Page<T>> fetchNext;

    Paged(Page<T> firstPage, Function<String, Page<T>> fetchNext) {
        this.firstPage = firstPage;
        this.fetchNext = fetchNext;
    }

    /**
     * The eagerly fetched first page, including its items and opaque pagination
     * tokens. This is the escape hatch for callers that need page-level control;
     * prefer {@link #iterator()}, {@link #stream()}, or {@link #toList()}.
     *
     * @return the first page
     */
    public Page<T> firstPage() {
        return firstPage;
    }

    /**
     * Whether the collection is empty, without fetching further pages.
     *
     * @return {@code true} if the first page has no items and no next page
     */
    public boolean isEmpty() {
        return firstPage.items().isEmpty() && !firstPage.hasNext();
    }

    /** Walk every item across all pages. */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private final Set<String> seenTokens = new HashSet<>();
            private Page<T> page = firstPage;
            private int index = 0;

            @Override
            public boolean hasNext() {
                while (index >= page.items().size()) {
                    if (!page.hasNext()) {
                        return false;
                    }
                    String token = page.nextPageToken();
                    if (!seenTokens.add(token)) {
                        throw new IllegalStateException(
                                "control plane returned a repeated page token: " + token);
                    }
                    page = fetchNext.apply(token);
                    index = 0;
                }
                return true;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return page.items().get(index++);
            }
        };
    }

    /**
     * Returns a lazy stream over every item across all pages.
     *
     * @return a sequential stream of all items
     */
    public Stream<T> stream() {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator(), 0), false);
    }

    /**
     * Eagerly fetch every page and return all items.
     *
     * @return an immutable list of all items
     */
    public List<T> toList() {
        return stream().toList();
    }

    /**
     * Lazily transform every item across all pages.
     *
     * @param mapper function applied to each item
     * @param <R> mapped element type
     * @return a lazily mapped collection
     */
    public <R> Paged<R> map(Function<? super T, ? extends R> mapper) {
        return new Paged<>(firstPage.map(mapper), token -> fetchNext.apply(token).map(mapper));
    }
}

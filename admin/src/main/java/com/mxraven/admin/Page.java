package com.mxraven.admin;

import java.util.List;
import java.util.function.Function;

/**
 * One page of a v2 cursor-paginated collection.
 *
 * <p>Most callers never touch this type: {@link Paged} hides pagination behind
 * iteration. This is the low-level escape hatch, reached through
 * {@link Paged#firstPage()}, for callers that want to walk pages deliberately.
 * A page returned from a {@code list(...)} call can fetch its neighbours:
 *
 * <pre>{@code
 * Page<Domain> first = ws.domains().list().firstPage();
 * Page<Domain> second = first.nextPage();
 * Page<Domain> back = second.previousPage();
 * }</pre>
 *
 * <p>Tokens are opaque. Never parse, construct, or persist them as durable
 * resource identifiers.
 */
public final class Page<T> {
    private final List<T> items;
    private final String nextPageToken;
    private final String previousPageToken;
    private final String lastPageToken;
    private final Function<String, Page<T>> fetchPage;

    Page(List<T> items, String nextPageToken, String previousPageToken, String lastPageToken) {
        this(items, nextPageToken, previousPageToken, lastPageToken, null);
    }

    Page(List<T> items, String nextPageToken, String previousPageToken, String lastPageToken,
         Function<String, Page<T>> fetchPage) {
        this.items = items == null ? List.of() : List.copyOf(items);
        this.nextPageToken = nextPageToken;
        this.previousPageToken = previousPageToken;
        this.lastPageToken = lastPageToken;
        this.fetchPage = fetchPage;
    }

    Page<T> withFetcher(Function<String, Page<T>> fetchPage) {
        return new Page<>(items, nextPageToken, previousPageToken, lastPageToken, fetchPage);
    }

    /**
     * Items on this page.
     *
     * @return an immutable list of the items on this page
     */
    public List<T> items() {
        return items;
    }

    /**
     * Returns the opaque token for the next page, when one exists.
     *
     * @return the next page token, or {@code null} when there is no next page
     */
    public String nextPageToken() {
        return nextPageToken;
    }

    /**
     * Returns the opaque token for the previous page, when one exists.
     *
     * @return the previous page token, or {@code null} when there is no
     *         previous page
     */
    public String previousPageToken() {
        return previousPageToken;
    }

    /**
     * Returns the opaque token for the last page, when known.
     *
     * @return the last page token, or {@code null} when unavailable
     */
    public String lastPageToken() {
        return lastPageToken;
    }

    /**
     * Returns whether a next page is available.
     *
     * @return {@code true} if {@link #nextPageToken()} is present
     */
    public boolean hasNext() {
        return nextPageToken != null && !nextPageToken.isBlank();
    }

    /**
     * Returns whether a previous page is available.
     *
     * @return {@code true} if {@link #previousPageToken()} is present
     */
    public boolean hasPrevious() {
        return previousPageToken != null && !previousPageToken.isBlank();
    }

    /**
     * Fetch the next page.
     *
     * @return the next page
     * @throws IllegalStateException if there is no next page, or this page came
     *                               from a source that cannot fetch pages
     */
    public Page<T> nextPage() {
        if (!hasNext()) {
            throw new IllegalStateException("no next page");
        }
        return fetch(nextPageToken);
    }

    /**
     * Fetch the previous page.
     *
     * @return the previous page
     * @throws IllegalStateException if there is no previous page, or this page
     *                               came from a source that cannot fetch pages
     */
    public Page<T> previousPage() {
        if (!hasPrevious()) {
            throw new IllegalStateException("no previous page");
        }
        return fetch(previousPageToken);
    }

    private Page<T> fetch(String token) {
        if (fetchPage == null) {
            throw new IllegalStateException("this page cannot fetch other pages");
        }
        return fetchPage.apply(token);
    }

    /**
     * Transform every item on this page, preserving its pagination tokens.
     *
     * @param mapper function applied to each item
     * @param <R> mapped element type
     * @return a new page with mapped items and the same tokens, without a fetcher
     */
    public <R> Page<R> map(Function<? super T, ? extends R> mapper) {
        List<R> mapped = items.stream().<R>map(mapper).toList();
        return new Page<>(mapped, nextPageToken, previousPageToken, lastPageToken, null);
    }

    @Override
    public String toString() {
        return "Page[items=" + items + ", nextPageToken=" + nextPageToken
                + ", previousPageToken=" + previousPageToken + ", lastPageToken=" + lastPageToken + "]";
    }
}

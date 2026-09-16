package com.mxraven.admin.examples;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Paged;
import com.mxraven.admin.Workspace;
import com.mxraven.admin.model.Domain;
import com.mxraven.admin.model.DomainStatus;

import java.util.List;

/**
 * Demonstrates lazy cursor pagination and typed filters.
 *
 * <p>Run with:
 * {@code ./gradlew :admin:runExample -Pexample=com.mxraven.admin.examples.PaginateAndFilterExample}
 */
public final class PaginateAndFilterExample {

    private PaginateAndFilterExample() {
    }

    public static void main(String[] args) throws Exception {
        try (AdminClient admin = new AdminClient("your-access-token")) {
            Workspace ws = admin.workspace("my-workspace");

            Paged<Domain> verified = ws.domains().query()
                    .status(DomainStatus.VERIFIED)
                    .pageSize(100)
                    .list();

            // Iteration follows cursor pages lazily; only consumed pages are fetched.
            for (Domain domain : verified) {
                System.out.println(domain.domainName());
            }

            List<Domain> firstPage = verified.firstPage().items();
            System.out.println("first page size=" + firstPage.size());

            // Or pull a specific page and walk forwards.
            if (verified.firstPage().hasNext()) {
                List<Domain> second = verified.firstPage().nextPage().items();
                System.out.println("second page size=" + second.size());
            }
        }
    }
}

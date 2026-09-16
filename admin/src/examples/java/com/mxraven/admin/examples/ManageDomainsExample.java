package com.mxraven.admin.examples;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Workspace;
import com.mxraven.admin.model.Domain;
import com.mxraven.admin.model.DomainListenerGrant;

/**
 * Creates, reads, updates, and deletes sending domains for a workspace.
 *
 * <p>Run with:
 * {@code ./gradlew :admin:runExample -Pexample=com.mxraven.admin.examples.ManageDomainsExample}
 */
public final class ManageDomainsExample {

    private ManageDomainsExample() {
    }

    public static void main(String[] args) throws Exception {
        try (AdminClient admin = new AdminClient("your-access-token")) {
            Workspace ws = admin.workspace("my-workspace");

            Domain domain = ws.domains().create("example.com");
            domain.replace("dmarc@example.com");

            for (DomainListenerGrant grant : domain.listenerGrants()) {
                System.out.println("listener=" + grant.listenerId()
                        + " scope=" + grant.subdomainScope());
            }

            for (Domain d : ws.domains().list()) {
                System.out.println(d.domainName() + " status=" + d.status()
                        + " dkimVerified=" + d.dkimVerified());
            }

            domain.delete();
        }
    }
}

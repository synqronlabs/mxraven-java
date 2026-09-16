package com.mxraven.admin.examples;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.Workspace;
import com.mxraven.admin.model.Tenant;
import com.mxraven.admin.model.TenantLoginContext;

/**
 * Reads workspace identity and the public login context.
 *
 * <p>Run with:
 * {@code ./gradlew :admin:runExample -Pexample=com.mxraven.admin.examples.WorkspaceOverviewExample}
 */
public final class WorkspaceOverviewExample {

    private WorkspaceOverviewExample() {
    }

    public static void main(String[] args) throws Exception {
        try (AdminClient admin = new AdminClient("your-access-token")) {
            Workspace ws = admin.workspace("my-workspace");

            Tenant tenant = ws.tenant();
            System.out.println("slug=" + tenant.slug()
                    + " status=" + tenant.status()
                    + " provisioning=" + tenant.provisioningState());

            TenantLoginContext login = admin.auth().loginContext("my-workspace");
            System.out.println("workspaceRef=" + login.workspaceRef()
                    + " displayName=" + login.displayName());
        }
    }
}

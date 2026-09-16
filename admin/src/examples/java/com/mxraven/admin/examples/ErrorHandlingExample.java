package com.mxraven.admin.examples;

import com.mxraven.admin.AdminClient;
import com.mxraven.admin.ProblemError;
import com.mxraven.admin.exception.ApiException;
import com.mxraven.admin.exception.NotFoundException;
import com.mxraven.admin.exception.RateLimitException;
import com.mxraven.admin.exception.ValidationException;

/**
 * Shows how control-plane failures surface as typed exceptions.
 *
 * <p>Run with:
 * {@code ./gradlew :admin:runExample -Pexample=com.mxraven.admin.examples.ErrorHandlingExample}
 */
public final class ErrorHandlingExample {

    private ErrorHandlingExample() {
    }

    public static void main(String[] args) throws Exception {
        try (AdminClient admin = new AdminClient("your-access-token")) {
            admin.workspace("my-workspace").domains().get("missing-id");
        } catch (NotFoundException e) {
            System.out.println("not found: " + e.detail() + " (trace " + e.traceId() + ")");
        } catch (ValidationException e) {
            for (ProblemError error : e.errors()) {
                System.out.println(error.pointer() + ": " + error.detail());
            }
        } catch (RateLimitException e) {
            System.out.println("rate limited, retry after " + e.retryAfter());
        } catch (ApiException e) {
            System.out.println("HTTP " + e.status() + " " + e.code() + ": " + e.detail());
        }
    }
}

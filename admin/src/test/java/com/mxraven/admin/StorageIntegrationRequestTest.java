package com.mxraven.admin;

import com.mxraven.admin.model.CreateStorageIntegrationRequest;
import com.mxraven.admin.model.UpdateStorageIntegrationRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StorageIntegrationRequestTest {
    private static CreateStorageIntegrationRequest.Builder validCreate() {
        return CreateStorageIntegrationRequest.builder()
                .storageRef("archive")
                .displayName("Archive")
                .accessKey("access")
                .secretKey("secret")
                .bucketName("bucket")
                .region("us-east-1")
                .endpointUrl("")
                .forcePathStyle(false);
    }

    @Test
    void createRequiresEverySchemaRequiredField() {
        assertThrows(IllegalArgumentException.class, () -> CreateStorageIntegrationRequest.builder()
                .displayName("Archive")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateStorageIntegrationRequest.builder()
                .storageRef("_bad")
                .displayName("Archive")
                .accessKey("access")
                .secretKey("secret")
                .bucketName("bucket")
                .region("us-east-1")
                .endpointUrl("")
                .build());
        assertThrows(IllegalArgumentException.class, () -> CreateStorageIntegrationRequest.builder()
                .storageRef("archive")
                .displayName("Archive")
                .accessKey("access")
                .secretKey("secret")
                .bucketName("bucket")
                .region("us-east-1")
                .build());
    }

    @Test
    void createValidatesEndpointUrl() {
        assertThrows(IllegalArgumentException.class, () -> validCreate()
                .endpointUrl("ftp://s3.example.com")
                .build());
        assertThrows(IllegalArgumentException.class, () -> validCreate()
                .endpointUrl("https://s3.example.com/path?query=1")
                .build());
        assertDoesNotThrow(() -> validCreate().endpointUrl("https://s3.example.com").build());
        assertDoesNotThrow(validCreate()::build);
    }

    @Test
    void updateRequiresEverySchemaRequiredField() {
        assertThrows(IllegalArgumentException.class, () -> UpdateStorageIntegrationRequest.builder()
                .displayName("Archive")
                .build());
        assertDoesNotThrow(() -> UpdateStorageIntegrationRequest.builder()
                .displayName("Archive")
                .accessKey("access")
                .secretKey("secret")
                .bucketName("bucket")
                .region("us-east-1")
                .endpointUrl("")
                .forcePathStyle(false)
                .build());
    }
}

package io.goboolean.streams.etcd;

import com.google.protobuf.ByteString;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.Txn;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.TxnResponse;
import io.etcd.jetcd.maintenance.StatusResponse;
import io.etcd.jetcd.op.Op;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EtcdService {

    private static final Logger logger = LogManager.getLogger(EtcdService.class);

    private final Client client;
    private final String endpoint;

    private final EtcdSerde<Product> productSerde = new EtcdSerde<>(Product.class);

    @Autowired
    public EtcdService(Properties props) {
        this.endpoint = props.getProperty("endpoints");
        this.client = Client.builder()
                .target(endpoint)
                .build();
    }

    @PostConstruct
    public void ping() {
        try {
            StatusResponse status = client.getMaintenanceClient().statusMember(endpoint).get();

            ThreadContext.put("status", status.toString());
        } catch (Exception e) {
            ThreadContext.put("error", e.toString());
            logger.error("Etcd ping failed");

            throw new RuntimeException(e);
        } finally {
            logger.info("Etcd ping successfully completed");
        }
    }

    @PreDestroy
    public void close() {
        try {
            client.close();
        } catch (Exception e) {
            logger.error("Etcd close failed");
        } finally {
            logger.info("Etcd successfully closed");
        }
    }

    public Product getProduct(String id) {
        String prefix = String.format("/product/%s", id);

        CompletableFuture<GetResponse> getFuture = client.getKVClient().get(
                ByteSequence.from(prefix.getBytes(StandardCharsets.UTF_8)),
                GetOption.builder().withRange(
                        ByteSequence.from(ByteString.copyFromUtf8(prefix).concat(ByteString.copyFrom(new byte[]{0}))))
                        .build()
                );

        try {
            Map<String, String> result = getFuture.get()
                    .getKvs().stream()
                    .collect(
                            Collectors.toMap(
                                    kv -> kv.getKey().toString(StandardCharsets.UTF_8),
                                    kv -> kv.getValue().toString(StandardCharsets.UTF_8)
                            )
                    );

            return productSerde.deserialize(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getAllProducts() {
        String prefix = "/product/";
        CompletableFuture<GetResponse> getFuture = client.getKVClient().get(
                ByteSequence.from(prefix.getBytes(StandardCharsets.UTF_8)),
                GetOption.builder().withPrefix(
                        ByteSequence.from(prefix.getBytes(StandardCharsets.UTF_8)))
                        .build());

        try {
            Map<String, String> result = getFuture.get()
                    .getKvs().stream()
                    .collect(
                            Collectors.toMap(
                                    kv -> kv.getKey().toString(StandardCharsets.UTF_8),
                                    kv -> kv.getValue().toString(StandardCharsets.UTF_8)));

            return productSerde.groupByPrefix(result).stream()
                    .map(product -> {
                        try {
                            return productSerde.deserialize(product);
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void insertProduct(Product product) {

        Txn txn = client.getKVClient().txn();

        try {
            Map<String, String> kvPair =  productSerde.serialize(product);
            kvPair.forEach((key, value) -> {
                txn.Then(Op.put(
                        ByteSequence.from(key.getBytes(StandardCharsets.UTF_8)),
                        ByteSequence.from(value.getBytes(StandardCharsets.UTF_8)),
                        PutOption.DEFAULT));
            });

            CompletableFuture<TxnResponse> txnResponse = txn.commit();
            TxnResponse response = txnResponse.get();

            if (!response.isSucceeded()) {
                throw new RuntimeException("Failed to insert product");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllProducts() {
        String prefix = "/product/";

        CompletableFuture<DeleteResponse> getFuture = client.getKVClient().delete(
                ByteSequence.from(prefix.getBytes(StandardCharsets.UTF_8)),
                DeleteOption.newBuilder().withPrefix(
                        ByteSequence.from(prefix.getBytes(StandardCharsets.UTF_8)))
                        .build()
        );

        try {
            DeleteResponse response = getFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

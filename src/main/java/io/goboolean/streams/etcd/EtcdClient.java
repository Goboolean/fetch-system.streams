package io.goboolean.streams.etcd;

import com.google.protobuf.ByteString;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.Txn;
import io.etcd.jetcd.kv.DeleteResponse;
import io.etcd.jetcd.kv.GetResponse;
import io.etcd.jetcd.kv.TxnResponse;
import io.etcd.jetcd.op.Op;
import io.etcd.jetcd.options.DeleteOption;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EtcdClient {

    private final Client client;

    private EtcdSerde<Product> productSerde = new EtcdSerde<>(Product.class);

    @Autowired
    public EtcdClient(Properties props) {
        String[] connections = {props.getProperty("endpoints")};

        this.client = Client.builder()
                .target(props.getProperty("endpoints"))
                //.endpoints(connections)
                .build();
    }

    public Product getProduct(String id) {
        String prefix = String.format("/product/%s", id);

        CompletableFuture<GetResponse> getFuture = client.getKVClient().get(
                ByteSequence.from(prefix.getBytes(StandardCharsets.UTF_8)),
                GetOption.newBuilder().withRange(
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
                GetOption.newBuilder().withPrefix(
                        ByteSequence.from(prefix.getBytes(StandardCharsets.UTF_8)))
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
                        PutOption.DEFAULT)
                );
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

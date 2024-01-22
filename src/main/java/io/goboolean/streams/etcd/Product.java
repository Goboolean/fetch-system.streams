package io.goboolean.streams.etcd;

import lombok.Getter;

public class Product implements Model {
    @Etcd("id") @Getter
    private String id;

    @Etcd("platform") @Getter
    private String platform;

    @Etcd("symbol") @Getter
    private String symbol;

    @Etcd("locale") @Getter
    private String locale;

    @Etcd("market") @Getter
    private String market;

    public Product() {
    }

    public Product(String id, String platform, String symbol, String locale, String market) {
        this.id = id;
        this.platform = platform;
        this.symbol = symbol;
        this.locale = locale;
        this.market = market;
    }

    @Override
    public String getName() {
        return "product";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Product)) return false;

        Product other = (Product) obj;
        return this.id.equals(other.id) &&
                this.platform.equals(other.platform) &&
                this.symbol.equals(other.symbol) &&
                this.locale.equals(other.locale) &&
                this.market.equals(other.market);
    }
}


package com.evensel.swyftr.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "id",
    "product_name",
    "stock_threshold",
    "category_name",
    "category_id",
    "brand_id",
    "product_image",
    "product_code",
    "product_amount",
    "product_price",
    "bar_code",
    "favourite",
    "promotional",
    "image"
})
public class Datum {


    @JsonProperty("id")
    private Integer id;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("stock_threshold")
    private Integer stockThreshold;
    @JsonProperty("category_name")
    private String categoryName;
    @JsonProperty("category_id")
    private Integer categoryId;
    @JsonProperty("brand_id")
    private String brandId;
    @JsonProperty("product_image")
    private String productImage;
    @JsonProperty("product_code")
    private String productCode;
    @JsonProperty("product_amount")
    private Integer productAmount;
    @JsonProperty("product_price")
    private Integer productPrice;
    @JsonProperty("bar_code")
    private String barCode;
    @JsonProperty("favourite")
    private Object favourite;
    @JsonProperty("promotional")
    private String promotional;
    @JsonProperty("image")
    private String image;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }
    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("stock_threshold")
    public Integer getStockThreshold() {
        return stockThreshold;
    }

    @JsonProperty("stock_threshold")
    public void setStockThreshold(Integer stockThreshold) {
        this.stockThreshold = stockThreshold;
    }

    @JsonProperty("category_id")
    public Integer getCategoryId() {
        return categoryId;
    }

    @JsonProperty("category_id")
    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @JsonProperty("category_name")
    public String getCategoryName() {
        return categoryName;
    }

    @JsonProperty("category_name")
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @JsonProperty("brand_id")
    public String getBrandId() {
        return brandId;
    }

    @JsonProperty("brand_id")
    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    @JsonProperty("product_image")
    public String getProductImage() {
        return productImage;
    }

    @JsonProperty("product_image")
    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @JsonProperty("product_code")
    public String getProductCode() {
        return productCode;
    }

    @JsonProperty("product_code")
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @JsonProperty("product_amount")
    public Integer getProductAmount() {
        return productAmount;
    }

    @JsonProperty("product_amount")
    public void setProductAmount(Integer productAmount) {
        this.productAmount = productAmount;
    }

    @JsonProperty("product_price")
    public Integer getProductPrice() {
        return productPrice;
    }

    @JsonProperty("product_price")
    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    @JsonProperty("bar_code")
    public String getBarCode() {
        return barCode;
    }

    @JsonProperty("bar_code")
    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @JsonProperty("favourite")
    public Object getFavourite() {
        return favourite;
    }

    @JsonProperty("favourite")
    public void setFavourite(Object favourite) {
        this.favourite = favourite;
    }

    @JsonProperty("promotional")
    public String getPromotional() {
        return promotional;
    }

    @JsonProperty("promotional")
    public void setPromotional(String promotional) {
        this.promotional = promotional;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

}

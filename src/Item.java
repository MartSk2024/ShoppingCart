import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Item {
    private String description;
    private Category category;
    private BigDecimal price;
    private LocalDateTime reservationTime;
    private boolean isOnStock;
    private int quantity;

    public Item(LocalDateTime reservationTime, BigDecimal price,
                int quantity, Category category, String description,
                boolean isOnStock) throws ShoppingCartException {
        this.reservationTime = reservationTime;
        this.setPrice(price);
        this.quantity = quantity;
        this.category = category;
        this.description = description;
        this.isOnStock = isOnStock;
    }

    public Item(BigDecimal price, String description) throws ShoppingCartException {
        this(LocalDateTime.now(), price, 1, Category.FOOD, description,true);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) throws ShoppingCartException {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ShoppingCartException("Cena položky nemůže být záporná! Zadal jsi: "+price);
        }
        this.price = price;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public boolean isOnStock() {
        return isOnStock;
    }

    public void setOnStock(boolean onStock) {
        isOnStock = onStock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}//

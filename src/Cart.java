import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cart {
    private final List<Item> listOfItemsInCart = new ArrayList<>();

    public List<Item> getListOfItemsInCart() {
        return listOfItemsInCart;
    }

    public void addItem(Item item) {
        listOfItemsInCart.add(item);
    }

    public void removeItem(Item item) {
        listOfItemsInCart.remove(item);
    }

    public void addItems(List<Item> listOfItemsInCart) {
        this.listOfItemsInCart.addAll(listOfItemsInCart);
    }

    public void getItemsOfOverPrice() {
        BigDecimal limitPrice = new BigDecimal("15.00");
        System.out.println("\nSeznam položek v košíku s cenou" +
                " za položku přesahující částku "+limitPrice+" Kč:");
        for (Item cartItem : getListOfItemsInCart()) {
            if (cartItem.getPrice().compareTo(limitPrice) > 0) {
                System.out.println(cartItem.getDescription());
            }
        }
    }

    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = new BigDecimal("0.00");
        for (Item cartItem : getListOfItemsInCart()) {
            totalPrice = totalPrice.add(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }
        return totalPrice;
    }

    public void getAveragePriceAndSum() {
        int sum = 0;
        BigDecimal averagePrice;
        for (Item cartItem : getListOfItemsInCart()) {
            sum = sum + cartItem.getQuantity();
        }
        if (sum == 0) {
            System.out.println("Košík je prázdný (průměrná cena položky neexistuje)");
        }
        if (sum != 0) {
            averagePrice = getTotalPrice().divide(BigDecimal.valueOf(sum), RoundingMode.HALF_UP);
            System.out.println("Průměrná cena položky v košíku: "+averagePrice+" Kč");
            System.out.println("Celkový počet položek v košíku: "+sum);
        }
    }

    public void readFromFile(String filename,String delimiter) throws ShoppingCartException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNumber = lineNumber + 1;
                Item item = parse(line, lineNumber, delimiter);
                listOfItemsInCart.add(item);
                System.out.println(item.getReservationTime()+", "+item.getPrice()+
                        ", "+item.getQuantity()+", "+item.getCategory()+
                        ", "+item.getDescription()+", "+(item.isOnStock() ? "ano" : "ne"));
            }
        } catch (FileNotFoundException e) {
            throw new ShoppingCartException("Soubor "+filename+ " nebyl nalezen!");
        }
    }

    public static Item parse(String line, int lineNumber, String delimiter) throws ShoppingCartException {
        int numberOfItemsRequired = 6;
        String[] parts = line.split(delimiter);
        if (parts.length != numberOfItemsRequired) {
            throw new ShoppingCartException("Chybný počet položek na řádku číslo " +
                    lineNumber + ". Na řádku se očekává " + numberOfItemsRequired +
                    " položek.");
        }

        LocalDateTime reservationTime;
        try {
            reservationTime = LocalDateTime.parse(parts[0].trim());
        } catch (DateTimeParseException exd) {
            throw new ShoppingCartException("Chybný formát položky " + parts[0].trim() +
                    " na řádku " + lineNumber + "." );
        }

        BigDecimal price;
        try {
            price = new BigDecimal(parts[1].trim());
            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new ShoppingCartException("záporná cena položky");
            }
        } catch (Exception e) {
            throw new ShoppingCartException("Chybný formát položky " + parts[1].trim() +
                    " na řádku " + lineNumber + " ("+e.getMessage()+").");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(parts[2].trim());
            if (quantity < 0) {
                throw new ShoppingCartException("záporné množství položky");
            }
        } catch (Exception e) {
            throw new ShoppingCartException("Chybný formát položky " + parts[2].trim() +
                    " na řádku " + lineNumber + " ("+e.getMessage()+").");
        }

        Category category;
        try {
            category = Category.valueOf(parts[3].trim());
        } catch (IllegalArgumentException exl) {
            throw new ShoppingCartException("Chybný formát položky " + parts[3].trim() +
                    " na řádku " + lineNumber + ".");
        }

        String description = parts[4].trim();
        boolean isOnStock = Boolean.parseBoolean(parts[5].trim());

        return new Item(reservationTime, price, quantity, category, description, isOnStock);
    }

    public void totalContentOfCart() {
        System.out.println("\nCelkový obsah košíku:");
        for (Item cartItem : getListOfItemsInCart()) {
            System.out.println(cartItem.getReservationTime()+", "+cartItem.getPrice()+
                    ", "+cartItem.getQuantity()+", "+cartItem.getCategory()+
                    ", "+cartItem.getDescription()+", "+(cartItem.isOnStock() ? "ano" : "ne"));
        }
    }

    public void writeToFile(String filename,String delimiter) throws ShoppingCartException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {
            for (Item cartItem : getListOfItemsInCart()){
                writer.println(cartItem.getReservationTime() + delimiter + " " +
                               cartItem.getPrice() + delimiter + " " +
                               cartItem.getQuantity() + delimiter + " " +
                               cartItem.getCategory() + delimiter + " " +
                               cartItem.getDescription() + delimiter + " " +
                               (cartItem.isOnStock() ? "ano" : "ne"));
            }
        } catch (IOException e) {
            throw new ShoppingCartException("Soubor "+filename+ " nebyl nalezen!");
        }
    }
}//

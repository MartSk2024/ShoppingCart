import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

public class Main {
    private static final String FILENAME = "resources/Cart_Contents.txt";
    private static final String FILENAME2 = "resources/Cart_Contents2.txt";
    private static final String DELIMITER = ";";

    public static void main(String[] args) throws ShoppingCartException {
        Cart cart = new Cart();
        readFile(cart);
        fillCart(cart);
        statisticsOnCart(cart);
        writeFile(cart);
    }

    private static void readFile(Cart cart) {
        System.out.println("\nVýpis zvoleného souboru (košíku):");
        try {
            cart.readFromFile(FILENAME, DELIMITER);
        } catch (ShoppingCartException ex) {
            System.err.println("Chyba při načtení souboru: "+ex.getMessage());
        }
    }

    private static void fillCart(Cart cart) {
        try {
            Item item1 = new Item(new BigDecimal("14.90"), "kobliha s marmeládou");
            item1.setQuantity(3);
            item1.setCategory(Category.FOOD);
            cart.addItem(item1);
            Item item2 = new Item(new BigDecimal("95.00"), "tekuté mýdlo (250 ml)");
            item2.setCategory(Category.CONSUMABLES);
            cart.addItem(item2);
        } catch (ShoppingCartException ex) {
            System.err.println("Chyba v ceně přidané položky: "+ex.getMessage());
        }
    }

    private static void statisticsOnCart(Cart cart) {
        cart.totalContentOfCart();
        cart.getItemsOfOverPrice();
        System.out.println("\nCelková cena všech položek v košíku: "+cart.getTotalPrice()+" Kč");
        cart.getAveragePriceAndSum();
    }

    private static void writeFile(Cart cart) {
        System.out.println("\nVýpis košíku do zvoleného souboru: viz soubor "+FILENAME2);
        try {
            cart.writeToFile(FILENAME2, DELIMITER);
        } catch (ShoppingCartException ex) {
            System.err.println("Chyba při zápisu do souboru: "+ex.getMessage());
        }
        System.out.println("\nKonec programu");
    }
}//
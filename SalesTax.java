// Problem 1: Sales Tax 
// Bikramjit Roy

import java.util.*;

public enum Category {
BOOK, FOOD, MEDICAL, OTHER
}

public class Item {
public final String name;
public final Category cat;
public final boolean isImported;
public final float basePrice;

public Item(String name, Category cat, boolean isImported, float basePrice) {
    this.name = name;
    this.cat = cat;
    this.isImported = isImported;
    this.basePrice = basePrice;
}
}

public interface TaxCalculator {
Item getItem();
float calc();
}

public class BaseTaxCalculator implements TaxCalculator {

protected Item item;

public BaseTaxCalculator(Item item) {
    this.item = item;
}

@Override
public Item getItem() {
    return item;
}

@Override
public float calc() {
    return 0;
}
}

public class SalesTaxCalculator implements TaxCalculator {

private TaxCalculator calculator;

public SalesTaxCalculator(TaxCalculator calculator) {
    this.calculator = calculator;
}

@Override
public Item getItem() {
    return calculator.getItem();
}

@Override
public float calc() {
    return calculator.calc() + getItem().basePrice * 0.1f;
}
}

public class ImportTaxCalculator implements TaxCalculator {

private TaxCalculator calculator;

public ImportTaxCalculator(TaxCalculator calculator) {
    this.calculator = calculator;
}

@Override
public Item getItem() {
    return calculator.getItem();
}

@Override
public float calc() {
    return calculator.calc() + getItem().basePrice * 0.05f;
}
}

public class Rounder implements TaxCalculator {
private TaxCalculator calculator;

public Rounder(TaxCalculator calculator) {
    this.calculator = calculator;
}

@Override
public Item getItem() {
    return calculator.getItem();
}

@Override
public float calc() {
    int val = 2 + (int)(calculator.calc() * 100);
    int remainder = val % 5;
    return ((float)(val - remainder)) / 100f;
}
}

public class Receipt {

private List<Item> items;

public Receipt() {
    items = new ArrayList<>();
}

public Receipt addItem(Item item) {
    items.add(item);
    return this;
}

public void print() {
    for (Item item: items) {
        TaxCalculator calc = new BaseTaxCalculator(item);
        if (item.cat == Category.OTHER)
            calc = new SalesTaxCalculator(calc);
        if (item.isImported)
            calc = new ImportTaxCalculator(calc);
        calc = new Rounder(calc);
        float salesTax = calc.calc();
        System.out.println(item.name + " " + item.basePrice + " " + salesTax);
    }
}
}

public class SalesTaxClient {

public static void main(String[] args) {

    Item book = new Item("book", Category.BOOK, false, 12.49f);
    Item cd = new Item("Music CD", Category.OTHER, true, 14.99f);
    Item bar = new Item("Chocolate Bar", Category.FOOD, true, 0.85f);

    Receipt receipt = new Receipt();
    receipt.addItem(book).addItem(cd).addItem(bar);

    receipt.print();
}
}


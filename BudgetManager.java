package budget;

import budget.products.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

public class BudgetManager {
    private final ArrayList<Product> products = new ArrayList<>();
    private double money = 0.0;

    private void saveToFile(){
        try (FileWriter fl = new FileWriter(new File("purchases.txt"))){
            fl.write(new DecimalFormat("0.00").format(money) + "\n");
            for (var product : products){
                if (product instanceof Food) {
                    fl.write(product.getName()+"~-~"+product.getPrice()+"~-~"+"FOOD\n");
                } else if (product instanceof Clothes) {
                    fl.write(product.getName()+"~-~"+product.getPrice()+"~-~"+"CLOTHES\n");
                } else if (product instanceof Entertainment) {
                    fl.write(product.getName()+"~-~"+product.getPrice()+"~-~"+"ENTERTAINMENT\n");
                } else if (product instanceof Other) {
                    fl.write(product.getName()+"~-~"+product.getPrice()+"~-~"+"OTHER\n");
                } else {
                    fl.write(product.getName()+"~-~"+product.getPrice()+"~-~"+"PRODUCT\n");
                }
            }
            System.out.println("Purchases were saved!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error =(!");
        }
    }
    private void loadFromFile(){
        products.clear();
        try (var scanner = new Scanner(new File("purchases.txt"))){
            if (scanner.hasNextDouble()){
                this.money = scanner.nextDouble();
            }
            scanner.nextLine();
            while (scanner.hasNextLine()){
                var data = scanner.nextLine().split("~-~");
                if (data.length < 3) {
                    break;
                }
                switch (data[2]){
                    case "FOOD": {
                        products.add(new Food(data[0], Double.parseDouble(data[1])));
                        break;
                    }
                    case "CLOTHES": {
                        products.add(new Clothes(data[0], Double.parseDouble(data[1])));
                        break;
                    }
                    case "ENTERTAINMENT": {
                        products.add(new Entertainment(data[0], Double.parseDouble(data[1])));
                        break;
                    }
                    case "OTHER": {
                        products.add(new Other(data[0], Double.parseDouble(data[1])));
                        break;
                    }
                    case "PRODUCT": {
                        products.add(new Product(data[0], Double.parseDouble(data[1])));
                        break;
                    }
                }
            }
            System.out.println("Purchases were loaded!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error =(!");
        }
    }

    private ArrayList<Product> sortByType(int type){
        var result = new ArrayList<Product>();
        sortProducts(type, result);
        return result;
    }

    private void sortProducts(int type, ArrayList<Product> result) {
        for (var product : products){
            if (type == 1 && product instanceof Food){
                result.add(product);
            }
            if (type == 2 && product instanceof Clothes){
                result.add(product);
            }
            if (type == 3 && product instanceof Entertainment){
                result.add(product);
            }
            if (type == 4 && product instanceof Other){
                result.add(product);
            }
            if (type == 5){
                result.add(product);
            }
        }
    }

    private void sortByTypes(){
        var res = new HashMap<String, Double>();
        var formatter = new DecimalFormat("#0.00");
        res.put("Food", 0.0);
        res.put("Entertainment", 0.0);
        res.put("Clothes", 0.0);
        res.put("Other", 0.0);
        for (var product : products) {
            if (product instanceof Food){
                res.replace("Food", res.get("Food") + product.getPrice());
            }
            if (product instanceof Entertainment){
                   res.replace("Entertainment", res.get("Entertainment") + product.getPrice());
            }
            if (product instanceof Clothes){
                res.replace("Clothes", res.get("Clothes") + product.getPrice());
            }
            if (product instanceof Other){
                res.replace("Other", res.get("Other") + product.getPrice());
            }
        }
        System.out.println("Types:");
        System.out.println("Food - $" + formatter.format(res.get("Food")));
        System.out.println("Entertainment - $" + formatter.format(res.get("Entertainment")));
        System.out.println("Clothes - $" + formatter.format(res.get("Clothes")));
        System.out.println("Other - $" + formatter.format(res.get("Other")));
        System.out.println("Total sum: $" + formatter.format(res.get("Food") + res.get("Entertainment") + res.get("Clothes") + res.get("Other")));
        System.out.println();
    }

    private void sort(){
        loop:while (true) {
            System.out.println("How do you want to sort?\n" +
                    "1) Sort all purchases\n" +
                    "2) Sort by type\n" +
                    "3) Sort certain type\n" +
                    "4) Back");
            var scan = new Scanner(System.in);
            var choose = scan.nextInt();
            switch (choose){
                case 1: {
                    if (products.size() < 1) {
                        System.out.println();
                        System.out.println("Purchase list is empty!");
                        System.out.println();
                        continue;
                    }
                    products.sort(Comparator.comparing(Product::getPrice).reversed());
                    System.out.println();
                    printAndCount(5, products);
                    System.out.println();
                    break;
                }
                case 2: {
                    System.out.println();
                    sortByTypes();
                    break;
                }
                case 3: {
                    System.out.println();
                    System.out.println("Choose the type of purchase\n" +
                            "1) Food\n" +
                            "2) Clothes\n" +
                            "3) Entertainment\n" +
                            "4) Other");
                    var type = scan.nextInt();
                    var res = sortByType(type);
                    if (res.size() < 1){
                        System.out.println();
                        System.out.println("Purchase list is empty!");
                        System.out.println();
                        break;
                    }
                    res.sort(Comparator.comparing(Product::getPrice).reversed());
                    System.out.println();
                    printAndCount(type, res);
                    System.out.println();
                    break;
                }
                case 4: {
                    System.out.println();
                    break loop;
                }
            }
        }
    }

    private void printAndCount(int type, ArrayList<Product> p){
        var typeS = "";
        switch (type){
            case 1: typeS = "Food"; break;
            case 2: typeS = "Clothes"; break;
            case 3: typeS = "Entertainment"; break;
            case 4: typeS = "Other"; break;
            case 5: typeS = "All"; break;
        }
        System.out.println(typeS + ":");
        var totalPrice = 0.0;
        var formatter = new DecimalFormat("#0.00");
        for (var pr : p){
            System.out.println(pr.getName() + " $" + formatter.format(pr.getPrice()));
            totalPrice+=pr.getPrice();
        }
        System.out.println("Total: $" + formatter.format(totalPrice));
    }

    private void addProduct(int id){
        var scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter purchase name: ");
        var name = scanner.nextLine();
        System.out.println("Enter its price: ");
        var price = scanner.nextDouble();
        switch (id){
            case 1: products.add(new Food(name, price)); break;
            case 2: products.add(new Clothes(name, price)); break;
            case 3: products.add(new Entertainment(name, price)); break;
            case 4: products.add(new Other(name, price)); break;
        }
        money -= price;
        System.out.println("Purchase was added!");
        System.out.println();
    }

    private void menu() {
        loop:while (true){
            System.out.println("Choose your action:\n" +
                    "1) Add income\n" +
                    "2) Add purchase\n" +
                    "3) Show list of purchases\n" +
                    "4) Balance\n" +
                    "5) Save\n" +
                    "6) Load\n" +
                    "7) Analyze (Sort)\n" +
                    "0) Exit");
            var scanner = new Scanner(System.in);
            var choose = scanner.nextInt();
            System.out.println();
            switch (choose){
                case 1: {
                    System.out.println("Enter income: ");
                    money += scanner.nextDouble();
                    System.out.println("Income was added!");
                    System.out.println();
                    break;
                }
                case 2: {
                    while (true){
                        System.out.println("Choose the type of purchase\n" +
                                "1) Food\n" +
                                "2) Clothes\n" +
                                "3) Entertainment\n" +
                                "4) Other\n" +
                                "5) Back");
                        var prodChoose = scanner.nextInt();
                        if (prodChoose > 4) {
                            break;
                        }
                        else {
                            addProduct(prodChoose);
                        }
                    }
                    System.out.println();
                    break;
                }
                case 3: {
                    while (true) {
                        System.out.println("Choose the type of purchases\n" +
                                "1) Food\n" +
                                "2) Clothes\n" +
                                "3) Entertainment\n" +
                                "4) Other\n" +
                                "5) All\n" +
                                "6) Back");
                        var type = scanner.nextInt();
                        if (type > 5) {
                            break;
                        }
                        var chooseList = new ArrayList<Product>();
                        var totalPrice = 0.0;
                        var formatter = new DecimalFormat("#0.00");
                        sortProducts(type, chooseList);
                        if (chooseList.size() < 1) {
                            System.out.println();
                            System.out.println("Purchase list is empty");
                            System.out.println();
                            break;
                        }
                        System.out.println();
                        switch (type){
                            case 1: System.out.println("Food: "); break;
                            case 2: System.out.println("Clothes: "); break;
                            case 3: System.out.println("Entertainment: "); break;
                            case 4: System.out.println("Other: "); break;
                            case 5: System.out.println("All: "); break;
                        }
                        for (var product : chooseList){
                            totalPrice += product.getPrice();
                            System.out.println(product.getName() + " $" + formatter.format(product.getPrice()));
                        }
                        System.out.println("Total sum: $" + formatter.format(totalPrice));
                        System.out.println();
                    }
                    System.out.println();
                    break;
                }
                case 4: {
                    System.out.println("Balance: $" + money);
                    System.out.println();
                    break;
                }
                case 5: {
                    saveToFile();
                    System.out.println();
                    break;
                }
                case 6: {
                    loadFromFile();
                    System.out.println();
                    break;
                }
                case 7: {
                    sort();
                    break;
                }
                case 0: {
                    return;
                }
                default: {
                    System.out.println("There is not action with number " + choose + " !");
                    System.out.println();
                    break;
                }
            }
        }
    }

    public void init(){
        menu();
        System.out.println("Bye!");
    }

}

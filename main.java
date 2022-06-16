import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    //final static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        String mode = new String();
        int key = 0;
        String data = new String();
        String in = new String();
        String out = new String();
        String alg = new String();
        int[] check = {0, 0, 0, 0, 0, 0}; //mode key data in out alg

        for(int i = 0; i < args.length; i += 2) {
            if(args[i].equals("-mode")) {
                mode = args[i + 1];
                check[0] = 1;
            } else if(args[i].equals("-key")) {
                key = Integer.parseInt(args[i + 1]);
                check[1] = 1;
            } else if(args[i].equals("-data")) {
                data = args[i + 1];
                check[2] = 1;
            } else if(args[i].equals("-in")) {
                in = args[i + 1];
                check[3] = 1;
            } else if(args[i].equals("-out")) {
                out = args[i + 1];
                check[4] = 1;
            } else if(args[i].equals("-alg")) {
                alg = args[i + 1];
                check[5] = 1;
            }
        }

        if(check[0] == 0) { mode = "enc"; }
        if(check[1] == 0) { key = 0; }
        if(check[2] == 0) {
            if(check[3] == 0) { data = "";
            } else { data = processFile(in); }
        }
        if(check[5] == 0) { alg = "shift"; }

        if(mode.equals("enc")) {
            if(alg.equals("shift")) {
                data = encMethodShift(data, key);
            } else if(alg.equals("unicode")) {
                data = encMethodUni(data, key);
            }
        } else if(mode.equals("dec")) {
            if(alg.equals("shift")) {
                data = decMethodShift(data, key);
            } else if(alg.equals("unicode")) {
                data = decMethodUni(data, key);
            }
        }

        if(check[4] == 0) {
            System.out.println(data);
        } else {
            writeFile(out, data);
        }
    }

    static void writeFile(String fileName, String data) {
        try {
            FileWriter text = new FileWriter(fileName);
            text.write(data);
            text.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String processFile(String data) {
        File file = new File(data);
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine()) {
                data = scanner.nextLine();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

    static String encMethodUni(String text, int key) {
        String newString = new String();
        for(int place = 0; place < text.length(); place++) {
            char temp = text.charAt(place);
            temp += key;
            newString += temp;
        }
        return newString;
    }

    static String decMethodUni(String text, int key) {
        String newString = new String();
        for(int place = 0; place < text.length(); place++) {
            char temp = text.charAt(place);
            temp -= key;
            newString += temp;
        }
        return newString;
    }

    private static String encMethodShift(String data, int key) {
        String newString = new String();
        for(int place = 0; place < data.length(); place++) {
            char temp = data.charAt(place);
            if(temp == ' ' || temp == '!') {
                newString += temp;
                continue;
            } else if(temp > 95) {
                temp += key;
                if(temp > 122) { temp %= 122; temp += ('a' - 1); }
            } else if(temp < 95) {
                temp += key;
                if(temp > 90) { temp %= 90; temp += ('A' - 1); }
            }
            newString += temp;
        }
        return newString;
    }

    private static String decMethodShift(String data, int key) {
        String newString = new String();
        for(int place = 0; place < data.length(); place++) {
            char temp = data.charAt(place);
            if(temp == ' ' || temp == '!') {
                newString += temp;
                continue;
            } else if(temp > 95) {
                temp -= key;
                if(temp < 97) {
                    temp = (char) ('a' - temp);
                    temp = (char) ((char) ('z' + 1) - temp);
                }
            } else if(temp < 95) {
                temp -= key;
                if(temp < 65) {
                    temp = (char) ('A' - temp);
                    temp = (char) ((char) ('Z' + 1) - temp);
                }
            }
            newString += temp;
        }
        return newString;
    }
}

package uk.co.biddell;


public class PropertyFileGenerator {
    
    public static void main(String[] args) {
        final Dictionary d = new Dictionary(args[0]);
        System.out.println(d.getNumberOfThrowsRequired());
    }
}

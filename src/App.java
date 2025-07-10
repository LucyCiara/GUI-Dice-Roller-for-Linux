public class App {
    public static void main(String[] args) throws Exception {
        int diceNumber = 0;
        int diceType = 0;

        for (int i = 0; i<10; i++){
            diceNumber = i;
            diceType = i;
        }
        
        System.out.println(diceNumber*diceType);
    }
}

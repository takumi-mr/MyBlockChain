public class Main {
    public static void main(String[] args) {
        Chain bc = new Chain();

        bc.addBlock("Send 1 BTC to Ivan");
        bc.addBlock("Send 2 more BTC to Ivan");

        bc.plotChain();

    }
}

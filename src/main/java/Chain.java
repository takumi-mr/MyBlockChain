import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Chain {
    private BigInteger target;
    private List<Block> blocks;
    static final int targetBits = 24;
    Chain(){
        blocks = new ArrayList<Block>();

        this.target = BigInteger.ONE;
        this.target = this.target.shiftLeft(256 - targetBits);

        createGenesisBlock();
    }
    private void createGenesisBlock(){
        blocks.add(new Block("Genesis Block", new byte[0], this.target));
    }
    public void addBlock(String data){
        Block prevBlock = blocks.get(blocks.size() - 1);
        Block b = new Block(data, prevBlock.getHash(), this.target);
        blocks.add(b);
    }
    public void plotChain(){
        for (Block b : blocks) {
            ProofOfWork pow = new ProofOfWork(b, b.getTarget());
            System.out.println("Prev hash: " + new String(Hex.encodeHex(b.getPrevBlockHash())));
            System.out.println("Data: " + new String(b.getData(), StandardCharsets.UTF_8));
            System.out.println("Nonce: " + b.getNonce());
            System.out.println("Target: " + String.format("%064x", b.getTarget()));
            System.out.println("Hash: " + new String(Hex.encodeHex(b.getHash())));
            System.out.println("Proof of Work: " + pow.isValid(b.getNonce()));
            System.out.println();
        }
    }
}

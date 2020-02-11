import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProofOfWork {
    static final int BUFFERS = 128;
    static final long maxNonce = Long.MAX_VALUE;
    private BigInteger target;
    private byte[] BLOCK;

    ProofOfWork(Block b, BigInteger t){
        this.target = t;
        ByteBuffer buffer = ByteBuffer.allocate(BUFFERS);
        buffer.put(b.getPrevBlockHash());
        buffer.put(b.getData());
        buffer.putLong(b.getTimeStamp());
        buffer.putLong(b.getTarget().longValue());
        BLOCK = buffer.array();
    }
    public long run(){
        BigInteger hashInt = null;
        long nonce = 0;
        System.out.println("Searching nonce...");
        while(nonce < maxNonce){
            byte[] hash, data = prepareData(nonce);
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(data);
                hash = md.digest();
                hashInt = bytesToPositiveBigInteger(hash);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            assert hashInt != null;
            if(hashInt.compareTo(this.target) < 0){
                System.out.println("Found! nonce = " + nonce);
                break;
            }else{
                nonce++;
            }
        }
        System.out.println();
        return nonce;
    }
    public byte[] prepareData(long nonce){
        ByteBuffer buffer = ByteBuffer.allocate(BUFFERS * 2);
        buffer.put(this.BLOCK);
        buffer.putLong(nonce);
        return buffer.array();
    }
    public byte[] blockHash(long nonce){
        byte[] data = this.prepareData(nonce);
        byte[] hash = new byte[32];
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }
    public boolean isValid(long nonce){
        BigInteger hashInt = bytesToPositiveBigInteger(blockHash(nonce));
        return hashInt.compareTo(this.target) < 0;
    }
    private BigInteger bytesToPositiveBigInteger(byte[] in) {
        byte[] bytes = new byte[in.length + 1];
        for (int i = 0; i < bytes.length; i++) {
            if (i == 0) {
                bytes[i] = 0;
            }
            else {
                bytes[i] = in[i - 1];
            }
        }
        return new BigInteger(bytes);
    }
}

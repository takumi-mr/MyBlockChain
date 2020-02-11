import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
// import java.nio.ByteBuffer;
// import java.security.MessageDigest;
// import java.security.NoSuchAlgorithmException;

public class Block implements Serializable{
    private long TimeStamp;
    private byte[] Data;
    private byte[] PrevBlockHash;
    private byte[] Hash;
    private long Nonce;
    private BigInteger target;
    // private static final int BUFFERS = 64;
    Block(String data, byte[] prevHash, BigInteger target){
        this.TimeStamp = System.currentTimeMillis() / 1000L;
        this.Data = data.getBytes(StandardCharsets.UTF_8);
        this.PrevBlockHash = prevHash;
        this.target = target;
        ProofOfWork pow = new ProofOfWork(this, target);
        System.out.println("Mining the block containing \" " + new String(this.Data, StandardCharsets.UTF_8) + " \"");
        this.Nonce = pow.run();
        this.Hash = pow.blockHash(this.Nonce);
    }
    /*
    private void setHash(){
        ByteBuffer buffer = ByteBuffer.allocate(BUFFERS);
        buffer.put(this.PrevBlockHash);
        buffer.put(this.Data);
        buffer.putLong(this.TimeStamp);

        byte[] header = buffer.array();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(header);
            this.Hash = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    */
    public byte[] serialize(){
        return SerializationUtils.serialize(this);
    }
    public static Block deSerialize(byte[] data){
        return SerializationUtils.deserialize(data);
    }
    public byte[] getData() {
        return Data;
    }
    public byte[] getPrevBlockHash() {
        return PrevBlockHash;
    }
    public byte[] getHash(){
        return this.Hash;
    }
    public long getTimeStamp() {
        return TimeStamp;
    }
    public long getNonce() {
        return Nonce;
    }
    public BigInteger getTarget() {
        return target;
    }
}

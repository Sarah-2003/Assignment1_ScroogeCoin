import java.util.Arrays;

public class UTXO implements Comparable<UTXO> {
    private byte[] txHash;
    private int index;

    public UTXO(byte[] txHash, int index) {
        this.txHash = Arrays.copyOf(txHash, txHash.length);
        this.index = index;
    }

    public byte[] getTxHash() {
        return Arrays.copyOf(txHash, txHash.length);
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UTXO utxo = (UTXO) o;
        return index == utxo.index && Arrays.equals(txHash, utxo.txHash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(txHash) ^ index;
    }

    @Override
    public int compareTo(UTXO other) {
        // Compare by txHash
        for (int i = 0; i < Math.min(this.txHash.length, other.txHash.length); i++) {
            int comparison = Byte.compare(this.txHash[i], other.txHash[i]);
            if (comparison != 0) {
                return comparison; // Return the result of the first differing byte
            }
        }
        // If all compared bytes are equal, compare by length
        return Integer.compare(this.txHash.length, other.txHash.length);
    }
}

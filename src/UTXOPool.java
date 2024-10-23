import java.util.HashMap;
import java.util.Map;

public class UTXOPool {
    private Map<UTXO, Transaction.Output> pool;

    public UTXOPool() {
        pool = new HashMap<>();
    }

    public UTXOPool(UTXOPool utxoPool) {
        pool = new HashMap<>(utxoPool.pool);
    }

    public void addUTXO(UTXO utxo, Transaction.Output output) {
        pool.put(utxo, output);
    }

    public void removeUTXO(UTXO utxo) {
        pool.remove(utxo);
    }

    public Transaction.Output getTxOutput(UTXO utxo) {
        return pool.get(utxo);
    }

    public boolean contains(UTXO utxo) {
        return pool.containsKey(utxo);
    }
}

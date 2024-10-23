import java.util.*;
import java.security.PublicKey;

public class TxHandler {

    protected UTXOPool utxoPool;

    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = new UTXOPool(utxoPool);
    }

    public boolean isValidTx(Transaction tx) {
        double inputSum = 0;
        double outputSum = 0;
        Set<UTXO> claimedUTXOs = new HashSet<>();

        for (int i = 0; i < tx.numInputs(); i++) {
            Transaction.Input input = tx.getInput(i);
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);

            if (!utxoPool.contains(utxo)) return false;

            Transaction.Output output = utxoPool.getTxOutput(utxo);
            // Ensure output.address is a PublicKey instance
            if (!Crypto.verifySignature(output.address, tx.getRawDataToSign(i), input.signature)) return false;

            if (claimedUTXOs.contains(utxo)) return false;
            claimedUTXOs.add(utxo);

            inputSum += output.value;
        }

        for (Transaction.Output output : tx.getOutputs()) {
            if (output.value < 0) return false;
            outputSum += output.value;
        }

        return inputSum >= outputSum;
    }

    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        List<Transaction> validTxs = new ArrayList<>();

        for (Transaction tx : possibleTxs) {
            if (isValidTx(tx)) {
                validTxs.add(tx);
                for (Transaction.Input input : tx.getInputs()) {
                    UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
                    utxoPool.removeUTXO(utxo);
                }
                for (int i = 0; i < tx.numOutputs(); i++) {
                    UTXO utxo = new UTXO(tx.getHash(), i);
                    utxoPool.addUTXO(utxo, tx.getOutput(i));
                }
            }
        }

        return validTxs.toArray(new Transaction[validTxs.size()]);
    }
}

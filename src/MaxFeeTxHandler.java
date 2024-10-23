import java.util.*;

public class MaxFeeTxHandler extends TxHandler {

    public MaxFeeTxHandler(UTXOPool utxoPool) {
        super(utxoPool);  // Call the constructor of TxHandler
    }

    @Override
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        List<Transaction> validTxs = new ArrayList<>();
        Arrays.sort(possibleTxs, (tx1, tx2) -> {
            double fee1 = getFee(tx1);
            double fee2 = getFee(tx2);
            return Double.compare(fee2, fee1); // Sort by max fees first
        });

        for (Transaction tx : possibleTxs) {
            if (isValidTx(tx)) {
                validTxs.add(tx);
                updateUTXOPool(tx);
            }
        }

        return validTxs.toArray(new Transaction[0]);
    }

    // Compute the transaction fee
    private double getFee(Transaction tx) {
        double inputSum = 0;
        double outputSum = 0;

        for (Transaction.Input input : tx.getInputs()) {
            UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
            if (utxoPool.contains(utxo)) {
                inputSum += utxoPool.getTxOutput(utxo).value;
            }
        }
        for (Transaction.Output output : tx.getOutputs()) {
            outputSum += output.value;
        }

        return inputSum - outputSum;  // Transaction fee is inputSum - outputSum
    }

    // Update the UTXOPool for accepted transactions
    private void updateUTXOPool(Transaction tx) {
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

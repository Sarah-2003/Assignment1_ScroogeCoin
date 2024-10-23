import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;

public class Main {
    public static void main(String[] args) {
        UTXOPool utxoPool = new UTXOPool();
        MaxFeeTxHandler txHandler = new MaxFeeTxHandler(utxoPool);
        
        // Generate key pairs for two users
        KeyPair keyPair1 = generateKeyPair();
        KeyPair keyPair2 = generateKeyPair();
        
        // Create a sample output transaction to add to the UTXO pool
        Transaction outputTx = new Transaction();
        outputTx.addOutput(10, keyPair1.getPublic()); // Output of 10 for user 1
        outputTx.addOutput(5, keyPair2.getPublic());  // Output of 5 for user 2
        
        // Adding the output transaction to the UTXO pool
        UTXO outputUTXO1 = new UTXO(outputTx.getHash(), 0); // UTXO for user 1
        UTXO outputUTXO2 = new UTXO(outputTx.getHash(), 1); // UTXO for user 2
        utxoPool.addUTXO(outputUTXO1, outputTx.getOutput(0));
        utxoPool.addUTXO(outputUTXO2, outputTx.getOutput(1));

        System.out.println("Created output transaction and added to UTXO pool.");

        // Create a transaction (tx1) that spends the above outputs
        Transaction tx1 = new Transaction();
        
        // User 1 spends their UTXO
        tx1.addInput(outputUTXO1.getTxHash(), outputUTXO1.getIndex()); // Add input for user 1
        
        // Ensure that the input was added successfully
        if (tx1.getNumInputs() > 0) {
            byte[] rawDataToSign1 = tx1.getRawDataToSign(0); // This will now work
            byte[] signature1 = signTransaction(rawDataToSign1, keyPair1.getPrivate());
            tx1.getInput(0).signature = signature1;
        } else {
            System.err.println("Failed to add input to tx1.");
        }

        // User 1 creates an output transaction
        tx1.addOutput(5, keyPair2.getPublic()); // Send 5 to user 2

        System.out.println("Created transaction tx1.");

        // Handle the transactions
        Transaction[] possibleTxs = new Transaction[]{tx1};
        Transaction[] handledTxs = txHandler.handleTxs(possibleTxs);

        System.out.println("Handled Transactions: " + handledTxs.length);
    }

    // Method to generate a key pair for testing
    private static KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Error generating key pair", e);
        }
    }

    // Method to sign the transaction's raw data
    private static byte[] signTransaction(byte[] rawData, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(rawData);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("Error signing transaction", e);
        }
    }
}

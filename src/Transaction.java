import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;  // Import PublicKey class
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    public class Input {
        public byte[] prevTxHash;  // The hash of the transaction containing the UTXO
        public int outputIndex;     // The index of the UTXO in the transaction
        public byte[] signature;    // The signature for this input
    }

    public class Output {
        public double value;        // The value of the transaction output
        public PublicKey address;   // The public key to which this output is paid
    }

    private List<Input> inputs;
    private List<Output> outputs;
    private byte[] hash;

    public Transaction() {
        inputs = new ArrayList<>();
        outputs = new ArrayList<>();
    }

    public void addInput(byte[] prevTxHash, int outputIndex) {
        Input in = new Input();
        in.prevTxHash = prevTxHash;
        in.outputIndex = outputIndex;
        inputs.add(in);
    }

    public void addOutput(double value, PublicKey address) {
        Output out = new Output();
        out.value = value;
        out.address = address;
        outputs.add(out);
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public List<Output> getOutputs() {
        return outputs;
    }

    public int numInputs() {
        return inputs.size();  // Method to get the number of inputs
    }

    public int numOutputs() {
        return outputs.size();
    }

    public Output getOutput(int index) {
        return outputs.get(index);
    }

    public Input getInput(int index) {
        return inputs.get(index);  // Method to get a specific input by index
    }

    public byte[] getHash() {
        if (hash == null) {
            hash = computeHash();
        }
        return hash;
    }

    private byte[] computeHash() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Include inputs in the hash
            for (Input input : inputs) {
                if (input.prevTxHash != null) {
                    md.update(input.prevTxHash);
                }
                md.update(intToBytes(input.outputIndex));
                if (input.signature != null) {
                    md.update(input.signature);
                }
            }

            // Include outputs in the hash
            for (Output output : outputs) {
                md.update(doubleToBytes(output.value));
                if (output.address != null) {
                    md.update(output.address.getEncoded());  // Ensure PublicKey has this method
                }
            }

            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to get raw data to sign for a specific input
    public byte[] getRawDataToSign(int index) {
        if (index < 0 || index >= inputs.size()) {
            throw new IndexOutOfBoundsException("Invalid input index");
        }
        
        // Create a new MessageDigest instance
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            // Add previous transaction hash and output index
            Input input = inputs.get(index);
            md.update(input.prevTxHash);
            md.update(intToBytes(input.outputIndex));
            
            // Add all outputs to the hash
            for (Output output : outputs) {
                md.update(doubleToBytes(output.value));
                if (output.address != null) {
                    md.update(output.address.getEncoded());
                }
            }
            
            return md.digest();  // Return the hashed value
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] intToBytes(int value) {
        return new byte[] {
            (byte) (value >> 24),
            (byte) (value >> 16),
            (byte) (value >> 8),
            (byte) value
        };
    }

    private byte[] doubleToBytes(double value) {
        long bits = Double.doubleToLongBits(value);
        return new byte[] {
            (byte) (bits >> 56),
            (byte) (bits >> 48),
            (byte) (bits >> 40),
            (byte) (bits >> 32),
            (byte) (bits >> 24),
            (byte) (bits >> 16),
            (byte) (bits >> 8),
            (byte) bits
        };
    }
}

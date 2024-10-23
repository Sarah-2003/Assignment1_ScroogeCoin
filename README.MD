# ScroogeCoin

## Overview
ScroogeCoin is a simple implementation of a cryptocurrency transaction handler. This project simulates the processing of transactions by a central authority (Scrooge) and ensures the integrity of the ledger by validating each transaction based on a set of predefined rules. The primary goal is to create a robust `TxHandler` class that processes transactions, validates them, and maintains an unspent transaction output (UTXO) pool.

## Features
- **Transaction Validation**: Ensures that all transactions meet certain criteria before they are accepted.
- **UTXO Management**: Keeps track of unspent outputs and updates the UTXO pool as transactions are processed.
- **Handling Multiple Transactions**: Processes an unordered array of proposed transactions and returns a mutually valid set.

## Project Structure
- **`Transaction.java`**: Defines the structure of a transaction, including inputs, outputs, and methods for adding/removing them.
- **`UTXO.java`**: Represents an unspent transaction output.
- **`UTXOPool.java`**: Manages the collection of UTXOs.
- **`Crypto.java`**: Contains utility methods for verifying digital signatures.
- **`TxHandler.java`**: The main class responsible for handling transactions.

## Implementation Details

### `TxHandler` Class

#### Constructor
```java
public TxHandler(UTXOPool utxoPool);
```
- **Purpose**: Initializes the transaction handler with a given UTXO pool. This constructor creates a defensive copy of the UTXO pool.

#### Methods

1. **`isValidTx(Transaction tx)`**
   - **Purpose**: Validates a transaction based on the following criteria:
     - All outputs claimed by the transaction are present in the UTXO pool.
     - The signatures on each input of the transaction are valid.
     - No UTXO is claimed multiple times by the transaction.
     - All of the transaction's output values are non-negative.
     - The sum of the transaction's input values is greater than or equal to the sum of its output values.
   - **Returns**: `true` if the transaction is valid; `false` otherwise.

2. **`handleTxs(Transaction[] possibleTxs)`**
   - **Purpose**: Processes an unordered array of proposed transactions, checking each transaction for correctness and returning a mutually valid array of accepted transactions. It also updates the current UTXO pool accordingly.
   - **Returns**: An array of accepted transactions that form a mutually valid set.

### Extra Credit: `MaxFeeTxHandler`
A secondary class, `MaxFeeTxHandler`, implements a modified version of `handleTxs()` that maximizes the total transaction fees for the accepted transactions.

## How to Run the Project

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd ScroogeCoin
   ```

2. Compile the Java files:
   ```bash
   javac *.java
   ```

3. Run the test cases or main driver class to simulate transaction handling:
   ```bash
   java DriverClass
   ```

## Usage
- Create instances of `Transaction`, add inputs and outputs, and use the `TxHandler` class to validate and handle transactions.
- For maximizing transaction fees, utilize the `MaxFeeTxHandler`.

## Testing
- Various test cases are provided to validate the functionality of `TxHandler` and `MaxFeeTxHandler`.
- Ensure all tests pass before submitting your assignment.

## Conclusion
This project provides a foundational understanding of how cryptocurrency transaction handlers operate. The ScroogeCoin implementation offers insights into transaction validation, UTXO management, and efficient transaction processing in a decentralized environment.
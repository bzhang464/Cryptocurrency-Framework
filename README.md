# Cryptocurrency-Framework
This program simulates the transfer of funds through a cryptocurrency. It does so by implementing a blockchain ledger with RSA-signature secured transactions.

## Background
Here's a brief description of what the program does.

The backbone of the cryptocurrency framework is the blockchain. The blockchain records the the current UTXOs (funds) available to be moved as well as
transaction data. For a user to send coins, they must first allocate a sufficient number of UTXOs and indicate the public RSA key of the recipient.
The transaction is then signed with the private key of the donor. Once the signature is verified using the donor's public key, the transaction can be added
to a block. But before a block can be added to the blockchain, it must be "mined" to have a hash with a certain prefix. Once this is done, the block can be added
to the chain and the UTXO's get updated.

## Usage

Before using, make sure to have Kotlin and Java installed on your computer.

To run, compile the source code to a jar file, and run it on the JVM. 
For more details on this, see here: https://www.codexpedia.com/kotlin/install-compile-and-run-kotlin-from-command-line/

The main function currently only simulates the transfer of 5 coins from account a1 to a2. So feel free to play around and simulate your own transactions with
the framework.

```
a1's balance is 10
a2's balance is 0
Transaction signed
Signature verified
Transaction successfully added to the block
Block successfully added to the chain
a1's balance is 5
a2's balance is 5
```





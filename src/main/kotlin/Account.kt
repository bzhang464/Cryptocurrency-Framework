import java.security.Signature
import java.security.PublicKey
import java.security.PrivateKey
import java.security.KeyPairGenerator
import java.util.Base64

data class Account(val chain: BlockChain, val initialAmount: Int) {
    val publicKey: PublicKey
    val privateKey: PrivateKey
    init {
        val keypairgenerator = KeyPairGenerator.getInstance("RSA")
        keypairgenerator.initialize(2048)
        val keypair = keypairgenerator.generateKeyPair()
        publicKey = keypair.public
        privateKey = keypair.private
        val initial = Output(publicKey, initialAmount, "0")
        chain.UTXO.put(initial.hash, initial)
    }

    private fun myOutputs(): List<Output> {
        var result: MutableList<Output> = mutableListOf()
        for (output in chain.UTXO.values) {
            if (output.yours(publicKey)){
                result.add(output)
            }
        }
        return result
    }

    fun getBalance(): Int {
        var outputs = myOutputs()
        var result = 0
        for (output in outputs) {
            result += output.amount
        }
        return result
    }

    fun send(receiver: PublicKey, amount: Int): Transaction {
        val transaction = Transaction(publicKey, receiver, amount)
        val temp = Output(receiver, amount, transaction.hash)
        transaction.outputs.add(temp)
        var gathered = 0
        for (output in myOutputs()){
            gathered += output.amount
            transaction.inputs.add(output)
            if (gathered > amount) {
                val change = gathered - amount
                val temp = Output(publicKey, change, transaction.hash)
                transaction.outputs.add(temp)
                break
            }
            if (gathered == amount) {
                break
            }
        }
        println("Transaction signed")
        return RSASignature().sign(transaction, privateKey)

    }
}

class RSASignature {
    private fun sign(data: String, privateKey: PrivateKey): ByteArray {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initSign(privateKey)
        val bytes = data.toByteArray(charset("UTF-8"))
        signature.update(bytes)
        return signature.sign()
    }

    fun sign(transaction: Transaction, privateKey: PrivateKey): Transaction {
        val mystr = transaction.hash
        transaction.signature = sign(mystr, privateKey)
        return transaction
    }

    fun verify(transaction: Transaction, publicKey: PublicKey): Boolean {
        val signature = Signature.getInstance("SHA256withRSA")
        signature.initVerify(publicKey)
        val bytes = transaction.hash.toByteArray(charset("UTF-8"))
        signature.update(bytes)
        return signature.verify(transaction.signature)
    }

    fun encode(key: PublicKey): String {
        return Base64.getEncoder().encodeToString(key.encoded)
    }
}






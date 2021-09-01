import java.security.PublicKey

data class Output (val receiver: PublicKey, val amount: Int, var transactionHash: String,
                   var hash: String = "") {
    init{
        hash = Hashing().hash("$amount${RSASignature().encode(receiver)}$transactionHash")
    }

    fun yours(address: PublicKey): Boolean {
        return receiver == address
    }
}

data class Transaction(val sender: PublicKey, val receiver: PublicKey, val amount: Int, var hash: String = "",
                       var inputs: MutableList<Output> = mutableListOf(),
                       var outputs: MutableList<Output> = mutableListOf(), var signature: ByteArray = ByteArray(0)) {
    init {
        hash = Hashing().hash("${RSASignature().encode(receiver)}${RSASignature().encode(sender)}$amount")
    }
}
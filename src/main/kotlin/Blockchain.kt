import java.security.MessageDigest
import java.util.Base64
import java.time.Instant

class Hashing {
    fun hash(inputString: String): String {
        val digester = MessageDigest.getInstance("SHA-256")
        val hash = digester.digest(inputString.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(hash)
    }
}

data class Block (val prevHash: String, val time: Long = Instant.now().toEpochMilli(),
                  var hash: String = "", val nonce: Long = 0,
                  var data: MutableList<Transaction> = mutableListOf()) {
    init {
        hash = makeHash()
    }

    fun makeHash(): String {
        return Hashing().hash("$prevHash$data$time$nonce")
    }

    fun addTransaction(transaction: Transaction) {
        if (RSASignature().verify(transaction, transaction.sender)) {
            data.add(transaction)
            println("Signature verified")
            println("Transaction successfully added to the block")
        }
    }
}

class BlockChain {
    private var blockchain: MutableList<Block> = mutableListOf()
    var UTXO: MutableMap<String, Output> = mutableMapOf()

    fun append(block: Block){
        if (Mining().mined(block)){
            blockchain.add(block)
        } else {
            blockchain.add(Mining().mine(block))
        }
        update(block)
        println("Block successfully added to the chain")
    }

    fun valid(): Boolean {
        if (blockchain.size == 0) {
            return true
        }
        if (blockchain.size == 1) {
            return (blockchain[0].hash == blockchain[0].makeHash()) and (Mining().mined(blockchain[0]))
        } else {
            for(x in 1..blockchain.size-1) {
                if (!(Mining().mined(blockchain[x]))) {
                    return false
                } else if (blockchain[x].hash != blockchain[x].makeHash()) {
                    return false
                } else if (blockchain[x].prevHash != blockchain[x-1].hash) {
                    return false
                }
            }
        }
        return true
    }

    fun update(block: Block) {
         for (transaction in block.data) {
            for (input in transaction.inputs) {
                UTXO.remove(input.hash)
            }
             for (output in transaction.outputs) {
                 UTXO.put(output.hash, output)
             }
         }
    }
}

class Mining {
    private val prefix = "000"

    fun mined(block: Block): Boolean {
        return block.hash.startsWith(prefix)
   }

    fun mine(block: Block): Block {
        var testblock = block.copy()
        while(!(mined(testblock))) {
            testblock = testblock.copy(nonce = testblock.nonce + 1)
        }

        return testblock
    }
}




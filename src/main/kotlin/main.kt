fun main() {
    var chain = BlockChain()
    val a1 = Account(chain, 10)
    val a2 = Account(chain, 0)
    println("a1's balance is ${a1.getBalance()}")
    println("a2's balance is ${a2.getBalance()}")
    val transaction = a1.send(a2.publicKey, 5)
    var block1 = Block("0")
    block1.addTransaction(transaction)
    chain.append(block1)
    println("a1's balance is ${a1.getBalance()}")
    println("a2's balance is ${a2.getBalance()}")
}


script {
use 0x1::ViolasBank;

fun main<Token>(account: &signer, amount: u64, data: vector<u8>) {
    ViolasBank::enter_bank<Token>(account, amount);
    ViolasBank::repay_borrow<Token>(account, amount, data);
}
}

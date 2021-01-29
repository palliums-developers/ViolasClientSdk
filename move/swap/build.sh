echo "biulding swap.move ..."
../build.sh exchange.move 0x0
mv build/modules/0_Exchange.mv exchange.mv

echo "biulding verify_sqrt.move ..."
../build.sh verify_sqrt.move 0x0 exchange.move
mv build/scripts/verify_sqrt.mv .

echo "biulding initialize.move ..."
../build.sh initialize.move 0x0 exchange.move
mv build/scripts/initialize.mv .

echo "biulding add_reserve.move ..."
../build.sh add_reserve.move 0x0 exchange.move
mv build/scripts/add_reserve.mv .

echo "biulding deposit_liquidity.move ..."
../build.sh deposit_liquidity.move 0x0 exchange.move
mv build/scripts/deposit_liquidity.mv .

echo "biulding withdraw_liquidity.move ..."
../build.sh withdraw_liquidity.move 0x0 exchange.move
mv build/scripts/withdraw_liquidity.mv .

echo "biulding swap.move ..."
../build.sh swap.move 0x0 exchange.move
mv build/scripts/swap.mv .
mv build/scripts/swap3.mv .
mv build/scripts/swap4.mv .

rm -rf build
echo "cleaned ./build"
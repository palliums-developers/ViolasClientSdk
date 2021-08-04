#ifndef NFT_HPP
#define NFT_HPP
#include <string>
#include <array>
#include "utils.hpp"
//#include "violas_sdk2.hpp"

namespace violas
{
    using TokenId = std::array<uint8_t, 32>;

    struct NftInfo
    {
        bool limited;
        uint64_t total;
        uint64_t amount;
        violas::Address admin;
        std::map<std::vector<uint8_t>, std::vector<violas::Address>> owners;
        violas::EventHandle mint_event;
        violas::EventHandle burn_event;

        BcsSerde &serde(BcsSerde &bs)
        {
            return bs && limited && total && amount && admin && owners && mint_event && burn_event;
        }
    };

    // std::ostream &operator<<(std::ostream &os, const NftInfo &nft_info)
    // {
    //     os << "Global Info { \n"
    //        << "\t"
    //        << "total : " << nft_info.total << "\n"
    //        << "\t"
    //        << "amount : " << nft_info.amount << "\n"
    //        << "\t"
    //        //<< "admin : " << nft_info.admin << "\n"
    //        << "\t"
    //        << "minted amount : " << nft_info.mint_event.counter << "\n"
    //        << "\t"
    //        << "burned amount : " << nft_info.burn_event.counter << "\n"
    //        << "}";

    //     return os;
    // }
    template <typename T>
    TokenId compute_token_id(const T &t)
    {
        BcsSerde serde;
        auto temp = t;

        serde &&temp;

        auto bytes = serde.bytes();

        auto token_id = sha3_256(bytes.data(), bytes.size());

        return token_id;
    }
    //
    //      NonFungibleToken
    //
    template <typename T>
    class NonFungibleToken
    {
    protected:
        violas::client_ptr _client;

    public:
        NonFungibleToken(client_ptr client);

        virtual ~NonFungibleToken() {}

        void deploy();

        void register_instance(uint64_t total_number);

        void accept(size_t account);

        //void mint();

        void burn(TokenId token_id);

        void transfer(uint64_t account_index, Address recevier, uint64_t token_index);

        template <typename RESOURCE>
        std::optional<RESOURCE> get_nfts(std::string url, Address addr);

        std::optional<std::vector<Address>> get_owners(std::string url, const TokenId &token_id);

        std::optional<NftInfo> get_nft_info(std::string url);
    };

    template <typename T>
    using nft_ptr = std::shared_ptr<NonFungibleToken<T>>;

}

#include "nft.cpp"

#endif